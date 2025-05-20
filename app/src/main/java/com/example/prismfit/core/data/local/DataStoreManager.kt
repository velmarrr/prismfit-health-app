package com.example.prismfit.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.prismfit.core.ui.theme.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class DataStoreManager(private val context: Context) {
    private val preferredLanguageKey = stringPreferencesKey("preferred_language")
    private val themePreferenceKey = stringPreferencesKey("theme_preference")

    suspend fun savePreferredLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[preferredLanguageKey] = languageCode
        }
    }

    fun getPreferredLanguage(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[preferredLanguageKey] ?: ""
            }
    }

    suspend fun saveThemePreference(theme: ThemePreference) {
        context.dataStore.edit { preferences ->
            preferences[themePreferenceKey] = theme.name
        }
    }

    fun getThemePreference(): Flow<ThemePreference> {
        return context.dataStore.data
            .map { preferences ->
                val value = preferences[themePreferenceKey]
                ThemePreference.entries.find { it.name == value } ?: ThemePreference.SYSTEM
            }
    }
}