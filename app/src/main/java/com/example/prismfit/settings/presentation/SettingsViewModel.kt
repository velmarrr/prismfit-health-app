package com.example.prismfit.settings.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.core.data.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val dataStoreManager = DataStoreManager(appContext)

    private val _currentLanguage = MutableStateFlow(getCurrentLocale(appContext).language)
    val currentLanguage: StateFlow<String> = _currentLanguage

    init {
        viewModelScope.launch {
            dataStoreManager.getPreferredLanguage().collect { preferredLanguage ->
                if (preferredLanguage.isNotEmpty()) {
                    _currentLanguage.value = preferredLanguage
                } else {
                    _currentLanguage.value = getCurrentLocale(appContext).language
                }
            }
        }
    }

    fun onLanguageChanged(languageCode: String) {
        viewModelScope.launch {
            if (_currentLanguage.value != languageCode) {
                dataStoreManager.savePreferredLanguage(languageCode)
                _currentLanguage.value = languageCode
            }
        }
    }

    private fun getCurrentLocale(context: Context): Locale {
        return context.resources.configuration.locales.get(0) ?: Locale.getDefault()
    }
}
