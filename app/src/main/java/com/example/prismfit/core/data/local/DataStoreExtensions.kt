package com.example.prismfit.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")
val Context.tokensDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_tokens")