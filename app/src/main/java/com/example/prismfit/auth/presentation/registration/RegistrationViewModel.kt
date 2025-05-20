package com.example.prismfit.auth.presentation.registration

import android.content.Context
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.R
import com.example.prismfit.auth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.prismfit.auth.data.model.AuthResult
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    private val _dateOfBirthMs = MutableStateFlow<Long>(Calendar.getInstance().timeInMillis)
    val dateOfBirthMs: StateFlow<Long> = _dateOfBirthMs

    private val _formattedDateOfBirth = MutableStateFlow(formatDate(_dateOfBirthMs.value))
    val formattedDateOfBirth: StateFlow<String> = _formattedDateOfBirth

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _nicknameError = MutableStateFlow<String?>(null)
    val nicknameError: StateFlow<String?> = _nicknameError

    private val _dateOfBirthError = MutableStateFlow<String?>(null)
    val dateOfBirthError: StateFlow<String?> = _dateOfBirthError

    private val _showDatePickerModal = MutableStateFlow(false)
    val showDatePickerModal: StateFlow<Boolean> = _showDatePickerModal

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess

    val isSignupEnabled: StateFlow<Boolean> = combine(
        listOf(
            email,
            password,
            nickname,
            dateOfBirthMs,
            emailError,
            passwordError,
            nicknameError,
            dateOfBirthError
        )
    ) { flows ->
        val currentEmail = flows[0] as String
        val currentPassword = flows[1] as String
        val currentNickname = flows[2] as String
        val currentDateOfBirthMs = flows[3] as Long?
        val currentEmailError = flows[4] as String?
        val currentPasswordError = flows[5] as String?
        val currentNicknameError = flows[6] as String?
        val currentDateOfBirthError = flows[7] as String?

        currentEmail.isNotEmpty() &&
        currentPassword.isNotEmpty() &&
        currentNickname.isNotEmpty() &&
        currentDateOfBirthMs != null &&
        currentEmailError == null &&
        currentPasswordError == null &&
        currentNicknameError == null &&
        currentDateOfBirthError == null
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

    fun onNicknameChanged(newValue: String) {
        _nickname.update { newValue }
        _nicknameError.update { validateNickname(newValue) }
    }

    fun onDateOfBirthClicked() {
        _showDatePickerModal.update { true }
    }

    fun onDateSelected(dateMs: Long) {
        _dateOfBirthMs.update { dateMs }
        _formattedDateOfBirth.update { formatDate(dateMs) }
        _dateOfBirthError.update { validateDateOfBirth(dateMs) }
        _showDatePickerModal.update { false }
    }

    fun onDatePickerDissmissed() {
        _showDatePickerModal.update { false }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> = _authResult

    fun onSignupButtonClicked() {
        _emailError.update { validateEmail(_email.value) }
        _passwordError.update { validatePassword(_password.value) }
        _nicknameError.update { validateNickname(_nickname.value) }
        _dateOfBirthError.update { validateDateOfBirth(_dateOfBirthMs.value) }

        if (_emailError.value == null && _passwordError.value == null &&
            _nicknameError.value == null && _dateOfBirthError.value == null) {

            _isLoading.value = true
            viewModelScope.launch {
                try {
                    when (val result = authRepository.register(
                        _email.value,
                        _password.value,
                        _nickname.value,
                        _dateOfBirthMs.value
                    )) {
                        is AuthResult.Success -> {
                            _authResult.value = result
                            _registrationSuccess.value = true
                        }
                        is AuthResult.Error -> {
                            if (result.field == AuthResult.Field.EMAIL) {
                                _emailError.value = result.message
                            }
                            _authResult.value = result
                            _registrationSuccess.value = false
                        }
                    }
                } catch (e: Exception) {
                    _authResult.value = AuthResult.Error(context.getString(R.string.error_during_signing_up), field = AuthResult.Field.GENERAL)
                    _registrationSuccess.value = false
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }

    private fun formatDate(ms: Long?): String {
        return ms?.let {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(Date(it))
        } ?: ""
    }

    private fun validateEmail(email: String): String? {
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"
        )
        return if (!emailPattern.matcher(email).matches()) {
            context.getString(R.string.invalid_email_format)
        } else {
            null
        }
    }

    private fun validatePassword(password: String): String? {
        val hasDigit = password.any { it.isDigit() }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        return when {
            password.length < 9 -> context.getString(R.string.password_length_requirement)
            !hasDigit -> context.getString(R.string.password_digit_requirement)
            !hasUpperCase -> context.getString(R.string.password_upper_case_letter_requirement)
            !hasLowerCase -> context.getString(R.string.password_lower_case_letter_requirement)
            else -> null
        }
    }

    private fun validateNickname(nickname: String): String? {
        return if (nickname.length < 3 || nickname.length > 20) {
            context.getString(R.string.nickname_length_requirement)
        } else {
            null
        }
    }

    private fun validateDateOfBirth(selectedDateMs: Long?): String? {
        selectedDateMs?.let {
            val currentDate = Calendar.getInstance()
            val birthDate = Calendar.getInstance()
            birthDate.timeInMillis = it

            val age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
            if (currentDate.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                return if (age - 1 < 12) context.getString(R.string.minimum_age_requirement) else null
            } else {
                return if (age < 12) context.getString(R.string.minimum_age_requirement) else null
            }
        }
        return context.getString(R.string.date_of_birth_selection_requirement)
    }
}