package com.example.prismfit.core.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_tokens")

@Singleton
class TokenStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val accessTokenFlow: Flow<String?> = context.dataStore.data.map { it[TokenPreferencesKeys.ACCESS_TOKEN] }
    val refreshTokenFlow: Flow<String?> = context.dataStore.data.map { it[TokenPreferencesKeys.REFRESH_TOKEN] }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit {
            it[TokenPreferencesKeys.ACCESS_TOKEN] = accessToken
            it[TokenPreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit {
            it.remove(TokenPreferencesKeys.ACCESS_TOKEN)
            it.remove(TokenPreferencesKeys.REFRESH_TOKEN)
        }
    }
}