package com.example.prismfit.core.session

import androidx.datastore.preferences.core.stringPreferencesKey

object TokenPreferencesKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}