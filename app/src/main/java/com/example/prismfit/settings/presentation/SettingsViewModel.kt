package com.example.prismfit.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.R
import com.example.prismfit.auth.data.repository.AuthRepository
import com.example.prismfit.core.data.local.DataStoreManager
import com.example.prismfit.core.ui.theme.ThemePreference
import com.example.prismfit.core.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager,
    @Named("initial_locale") initialLocale: Locale
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(initialLocale.language)
    val currentLanguage: StateFlow<String> = _currentLanguage

    private val _themePreference = MutableStateFlow(ThemePreference.SYSTEM)
    val themePreference: StateFlow<ThemePreference> = _themePreference

    private val _languageChanged = MutableSharedFlow<Unit>()
    val languageChanged: SharedFlow<Unit> = _languageChanged.asSharedFlow()

    private val _errorMessage = MutableStateFlow<UiText?>(null)
    val errorMessage: StateFlow<UiText?> = _errorMessage

    private val handler = CoroutineExceptionHandler { _, exception ->
        val message = exception.message?.takeIf { it.isNotBlank() }
        _errorMessage.value = if (message != null) {
            UiText.StringResource(R.string.error, message)
        } else {
            UiText.StringResource(R.string.unexpected_error)
        }
    }

    init {
        viewModelScope.launch(handler) {
            dataStoreManager.getPreferredLanguage().collect { preferredLanguage ->
                if (preferredLanguage.isNotEmpty()) {
                    _currentLanguage.value = preferredLanguage
                }
            }
        }
        viewModelScope.launch(handler) {
            dataStoreManager.getThemePreference().collect {
                _themePreference.value = it
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun onLanguageChanged(languageCode: String) {
        viewModelScope.launch {
            if (_currentLanguage.value != languageCode) {
                dataStoreManager.savePreferredLanguage(languageCode)
                _currentLanguage.value = languageCode
                _languageChanged.emit(Unit)
            }
        }
    }

    fun onThemeChanged(theme: ThemePreference) {
        viewModelScope.launch {
            dataStoreManager.saveThemePreference(theme)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
