package com.example.prismfit.auth.presentation.login

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.prismfit.auth.presentation.components.InputField
import com.example.prismfit.navigation.HomeGraph.HomeRoute
import com.example.prismfit.navigation.LoginGraph.LoginRoute
import com.example.prismfit.navigation.RegisterGraph.RegisterRoute
import com.example.prismfit.core.ui.theme.AppTheme
import com.example.prismfit.core.ui.theme.ThemePreference

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val email by viewModel.email.collectAsStateWithLifecycle(lifecycleOwner)
    val password by viewModel.password.collectAsStateWithLifecycle(lifecycleOwner)
    val isPasswordVisible by viewModel.isPasswordVisible.collectAsStateWithLifecycle(lifecycleOwner)
    val emailError by viewModel.emailError.collectAsStateWithLifecycle(lifecycleOwner)
    val passwordError by viewModel.passwordError.collectAsStateWithLifecycle(lifecycleOwner)
    val isLoginEnabled by viewModel.isLoginEnabled.collectAsStateWithLifecycle(lifecycleOwner)
    val loginSuccess by viewModel.loginSuccess.collectAsStateWithLifecycle(lifecycleOwner)

    val navigateToRegister = { navController.navigate(RegisterRoute) }

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.navigate(HomeRoute) {
                popUpTo(LoginRoute) { inclusive = true }
            }
            viewModel.resetLoginSuccess()
        }
    }

    LoginContent(
        email = email,
        password = password,
        isPasswordVisible = isPasswordVisible,
        emailError = emailError,
        passwordError = passwordError,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onPasswordVisibilityClick = viewModel::onPasswordVisibilityClicked,
        isLoginEnabled = isLoginEnabled,
        onLoginClick = viewModel::onLoginButtonClicked,
        navigateToRegister = navigateToRegister
    )
}

@Composable
fun LoginContent(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    emailError: String?,
    passwordError: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    isLoginEnabled: Boolean,
    onLoginClick: () -> Unit,
    navigateToRegister: () -> Unit
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
            text = stringResource(R.string.login),
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
        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            enabled = isLoginEnabled
        ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = navigateToRegister
        ) {
            Row {
                Text(
                    text = stringResource(R.string.dont_have_an_account),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = stringResource(R.string.sign_up),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    AppTheme(themePreference = ThemePreference.SYSTEM) {
        LoginContent(
            email = "",
            password = "",
            isPasswordVisible = false,
            emailError = null,
            passwordError = null,
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityClick = {},
            isLoginEnabled = true,
            onLoginClick = {},
            navigateToRegister = {}
        )
    }
}