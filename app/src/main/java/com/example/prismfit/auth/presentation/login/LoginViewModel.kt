package com.example.prismfit.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.R
import com.example.prismfit.auth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject
import com.example.prismfit.auth.data.model.AuthResult
import com.example.prismfit.core.ui.utils.UiText

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _emailError = MutableStateFlow<UiText?>(null)
    val emailError: StateFlow<UiText?> = _emailError

    private val _passwordError = MutableStateFlow<UiText?>(null)
    val passwordError: StateFlow<UiText?> = _passwordError

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val isLoading = MutableStateFlow(false)
    private val authResult = MutableStateFlow<AuthResult?>(null)

    val isLoginEnabled: StateFlow<Boolean> = combine(
        listOf(
            email,
            password,
            emailError,
            passwordError,
        )
    ) { flows ->
        val currentEmail = flows[0] as String
        val currentPassword = flows[1] as String
        val currentEmailError = flows[2] as UiText?
        val currentPasswordError = flows[3] as UiText?

        currentEmail.isNotEmpty() &&
        currentPassword.isNotEmpty() &&
        currentEmailError == null &&
        currentPasswordError == null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(300),
        initialValue = false
    )

    fun onEmailChanged(newValue: String) {
        _email.update { newValue }
        _emailError.update { validateEmail(newValue) }
    }

    fun onPasswordChanged(newValue: String) {
        _password.update { newValue }
        _passwordError.update { validatePassword(newValue) }
    }

    fun onPasswordVisibilityClicked() {
        _isPasswordVisible.update { !it }
    }

    fun onLoginButtonClicked() {
        _emailError.update { validateEmail(_email.value) }
        _passwordError.update { validatePassword(_password.value) }

        if (_emailError.value == null && _passwordError.value == null) {
            isLoading.value = true
            viewModelScope.launch {
                try {
                    when (val result = authRepository.login(_email.value, _password.value)) {
                        is AuthResult.Success -> {
                            authResult.value = result
                            _loginSuccess.value = true
                        }
                        is AuthResult.Error -> {
                            if (result.field == AuthResult.Field.EMAIL) {
                                _emailError.value = result.message
                            } else {
                                _passwordError.value = UiText.StringResource(R.string.incorrect_password)
                            }
                            authResult.value = result
                            _loginSuccess.value = false
                        }
                    }
                } catch (e: Exception) {
                    authResult.value = AuthResult.Error(
                        UiText.StringResource(R.string.error_during_logging_in)
                    )
                    _loginSuccess.value = false
                } finally {
                    isLoading.value = false
                }
            }
        }
    }

    fun resetLoginSuccess() {
        _loginSuccess.value = false
    }

    private fun validateEmail(email: String): UiText? {
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"
        )
        return if (!emailPattern.matcher(email).matches()) {
            UiText.StringResource(R.string.invalid_email_format)
        } else {
            null
        }
    }

    private fun validatePassword(password: String): UiText? {
        val hasDigit = password.any { it.isDigit() }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        return if (password.length < 9 || !hasDigit || !hasUpperCase || !hasLowerCase) {
            UiText.StringResource(R.string.incorrect_password_format)
        } else {
            null
        }
    }
}