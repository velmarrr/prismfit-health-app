package com.example.prismfit.core.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.prismfit.core.data.local.tokensDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class TokenStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val accessTokenFlow: Flow<String?> = context.tokensDataStore.data.map { it[TokenPreferencesKeys.ACCESS_TOKEN] }
    val refreshTokenFlow: Flow<String?> = context.tokensDataStore.data.map { it[TokenPreferencesKeys.REFRESH_TOKEN] }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.tokensDataStore.edit {
            it[TokenPreferencesKeys.ACCESS_TOKEN] = accessToken
            it[TokenPreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clearTokens() {
        context.tokensDataStore.edit {
            it.remove(TokenPreferencesKeys.ACCESS_TOKEN)
            it.remove(TokenPreferencesKeys.REFRESH_TOKEN)
        }
    }
}