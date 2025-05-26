package com.example.prismfit.auth.presentation.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.prismfit.auth.presentation.components.DatePickerModal
import com.example.prismfit.navigation.HomeGraph.HomeRoute
import com.example.prismfit.navigation.LoginGraph.LoginRoute
import com.example.prismfit.navigation.RegisterGraph.RegisterRoute

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
    val context = LocalContext.current

    val emailErrorText = emailError?.asString(context)
    val passwordErrorText = passwordError?.asString(context)
    val nicknameErrorText = nicknameError?.asString(context)
    val dateOfBirthErrorText = dateOfBirthError?.asString(context)

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
        emailError = emailErrorText,
        passwordError = passwordErrorText,
        nicknameError = nicknameErrorText,
        dateOfBirthError = dateOfBirthErrorText,
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
