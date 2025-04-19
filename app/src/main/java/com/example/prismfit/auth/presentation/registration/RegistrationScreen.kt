package com.example.prismfit.auth.presentation.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.prismfit.R
import com.example.prismfit.auth.presentation.components.DatePickerModal
import com.example.prismfit.auth.presentation.components.InputField
import com.example.prismfit.navigation.HomeGraph.HomeRoute
import com.example.prismfit.navigation.LoginGraph.LoginRoute
import com.example.prismfit.navigation.RegisterGraph.RegisterRoute
import com.example.prismfit.core.ui.theme.AppTheme

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = hiltViewModel()
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val email by viewModel.email.collectAsStateWithLifecycle(lifecycleOwner)
    val password by viewModel.password.collectAsStateWithLifecycle(lifecycleOwner)
    val isPasswordVisible by viewModel.isPasswordVisible.collectAsStateWithLifecycle(lifecycleOwner)
    val nickname by viewModel.nickname.collectAsStateWithLifecycle(lifecycleOwner)
    val dateOfBirthMs by viewModel.dateOfBirthMs.collectAsStateWithLifecycle(lifecycleOwner)
    val formattedDateOfBirth by viewModel.formattedDateOfBirth.collectAsStateWithLifecycle(lifecycleOwner)
    val showDatePickerModal by viewModel.showDatePickerModal.collectAsStateWithLifecycle(lifecycleOwner)
    val emailError by viewModel.emailError.collectAsStateWithLifecycle(lifecycleOwner)
    val passwordError by viewModel.passwordError.collectAsStateWithLifecycle(lifecycleOwner)
    val nicknameError by viewModel.nicknameError.collectAsStateWithLifecycle(lifecycleOwner)
    val dateOfBirthError by viewModel.dateOfBirthError.collectAsStateWithLifecycle(lifecycleOwner)
    val isSignupEnabled by viewModel.isSignupEnabled.collectAsStateWithLifecycle(lifecycleOwner)
    val registrationSuccess by viewModel.registrationSuccess.collectAsStateWithLifecycle(lifecycleOwner)

    val navigateToLogin = { navController.navigate(LoginRoute) }

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            navController.navigate(HomeRoute) {
                popUpTo(RegisterRoute) { inclusive = true }
            }
            viewModel.resetRegistrationSuccess()
        }
    }

    RegistrationContent(
        email = email,
        password = password,
        isPasswordVisible = isPasswordVisible,
        nickname = nickname,
        dateOfBirth = formattedDateOfBirth,
        emailError = emailError,
        passwordError = passwordError,
        nicknameError = nicknameError,
        dateOfBirthError = dateOfBirthError,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onNicknameChange = viewModel::onNicknameChanged,
        onDateOfBirthClick = viewModel::onDateOfBirthClicked,
        onPasswordVisibilityClick = viewModel::onPasswordVisibilityClicked,
        isSignupEnabled = isSignupEnabled,
        onSignupClick = viewModel::onSignupButtonClicked,
        navigateToLogin = navigateToLogin
    )

    if(showDatePickerModal) {
        DatePickerModal(
            selectedDateMs = dateOfBirthMs,
            onDateSelected = viewModel::onDateSelected,
            onDismiss = viewModel::onDatePickerDissmissed
        )
    }
}

@Composable
fun RegistrationContent(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    nickname: String,
    dateOfBirth: String,
    emailError: String?,
    passwordError: String?,
    nicknameError: String?,
    dateOfBirthError: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onDateOfBirthClick: () -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    isSignupEnabled: Boolean,
    onSignupClick: () -> Unit,
    navigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(48.dp))
        InputField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.email),
            icon = Icons.Filled.Email,
            isError = emailError != null,
            errorMessage = emailError
        )
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.password),
                icon = Icons.Filled.Lock,
                visualTransformation =
                    if (isPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                isError = passwordError != null,
                errorMessage = passwordError
            )
            IconButton(
                onClick = onPasswordVisibilityClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 6.dp)
            ) {
                Icon(
                    imageVector =
                        if (isPasswordVisible) Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                    contentDescription =
                        if (isPasswordVisible) stringResource(R.string.hide_password)
                        else stringResource(R.string.show_password)
                )
            }
        }
        InputField(
            value = nickname,
            onValueChange = onNicknameChange,
            label = stringResource(R.string.nickname),
            icon = Icons.Filled.Person,
            isError = nicknameError != null,
            errorMessage = nicknameError,
            maxLength = 20
        )
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputField(
                value = dateOfBirth,
                onValueChange = {},
                label = stringResource(R.string.date_of_birth),
                readOnly = true,
                isError = dateOfBirthError != null,
                errorMessage = dateOfBirthError
            )
            IconButton(
                onClick = onDateOfBirthClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 6.dp)
            ) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = stringResource(R.string.select_date_of_birth))
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = onSignupClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            enabled = isSignupEnabled
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = navigateToLogin
        ) {
            Row {
                Text(
                    text = stringResource(R.string.already_have_an_account),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = stringResource(R.string.login),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegistrationPreview() {
    AppTheme {
        RegistrationContent(
            email = "",
            password = "",
            isPasswordVisible = false,
            nickname = "",
            dateOfBirth = "09/02/2025",
            emailError = null,
            passwordError = null,
            nicknameError = null,
            dateOfBirthError = null,
            onEmailChange = {},
            onPasswordChange = {},
            onNicknameChange = {},
            onDateOfBirthClick = {},
            onPasswordVisibilityClick = {},
            isSignupEnabled = true,
            onSignupClick = {},
            navigateToLogin = {}
        )
    }
}