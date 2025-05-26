package com.example.prismfit.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.prismfit.navigation.HomeGraph.HomeRoute
import com.example.prismfit.navigation.LoginGraph.LoginRoute
import com.example.prismfit.navigation.RegisterGraph.RegisterRoute

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
    val context = LocalContext.current

    val emailErrorText = emailError?.asString(context)
    val passwordErrorText = passwordError?.asString(context)

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
        emailError = emailErrorText,
        passwordError = passwordErrorText,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onPasswordVisibilityClick = viewModel::onPasswordVisibilityClicked,
        isLoginEnabled = isLoginEnabled,
        onLoginClick = viewModel::onLoginButtonClicked,
        navigateToRegister = navigateToRegister
    )
}
