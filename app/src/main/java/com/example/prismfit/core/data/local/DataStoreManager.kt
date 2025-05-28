package com.example.prismfit.core.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.prismfit.core.ui.theme.ThemePreference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val preferredLanguageKey = stringPreferencesKey("preferred_language")
    private val themePreferenceKey = stringPreferencesKey("theme_preference")

    suspend fun savePreferredLanguage(languageCode: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[preferredLanguageKey] = languageCode
        }
    }

    fun getPreferredLanguage(): Flow<String> {
        return context.settingsDataStore.data
            .map { preferences ->
                preferences[preferredLanguageKey] ?: ""
            }
    }

    suspend fun ensurePreferredLanguageInitialized() {
        val current = context.settingsDataStore.data.first()[preferredLanguageKey]
        if (current.isNullOrEmpty()) {
            val systemLang = getSystemLocale().language
            savePreferredLanguage(systemLang)
        }
    }

    private fun getSystemLocale(): Locale {
        return context.resources.configuration.locales.get(0) ?: Locale.getDefault()
    }

    suspend fun saveThemePreference(theme: ThemePreference) {
        context.settingsDataStore.edit { preferences ->
            preferences[themePreferenceKey] = theme.name
        }
    }

    fun getThemePreference(): Flow<ThemePreference> {
        return context.settingsDataStore.data
            .map { preferences ->
                val value = preferences[themePreferenceKey]
                ThemePreference.entries.find { it.name == value } ?: ThemePreference.SYSTEM
            }
    }
}
