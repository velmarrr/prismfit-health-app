package com.example.prismfit.auth.data.repository

import android.content.Context
import com.example.prismfit.R
import com.example.prismfit.auth.data.model.AuthRequest
import com.example.prismfit.auth.data.model.AuthResult
import com.example.prismfit.auth.data.model.RefreshRequest
import com.example.prismfit.auth.data.remote.AuthApi
import com.example.prismfit.core.session.TokenStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage,
    @ApplicationContext private val context: Context
) {

    suspend fun register(
        email: String,
        password: String,
        nickname: String,
        dateOfBirth: Long
    ): AuthResult {
        return try {
            val res = api.register(AuthRequest(email, password, nickname, dateOfBirth))
            if (res.isSuccessful) {
                res.body()?.let {
                    tokenStorage.saveTokens(it.accessToken, it.refreshToken)
                    AuthResult.Success
                } ?: AuthResult.Error(
                    context.getString(R.string.empty_server_response),
                    field = AuthResult.Field.GENERAL
                )
            } else {
                when (res.code()) {
                    409 -> AuthResult.Error(
                        context.getString(R.string.user_already_exists),
                        field = AuthResult.Field.EMAIL
                    )

                    400 -> AuthResult.Error(
                        context.getString(R.string.invalid_registration_data),
                        field = AuthResult.Field.GENERAL
                    )

                    else -> AuthResult.Error(
                        "Error: ${res.code()}",
                        field = AuthResult.Field.GENERAL
                    )
                }
            }
        } catch (e: HttpException) {
            AuthResult.Error("HttpException: ${e.message()}", field = AuthResult.Field.GENERAL)
        } catch (e: Exception) {
            AuthResult.Error("Exception: ${e.localizedMessage}", field = AuthResult.Field.GENERAL)
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val res = api.login(AuthRequest(email, password, "", 0))
            if (res.isSuccessful) {
                res.body()?.let {
                    tokenStorage.saveTokens(it.accessToken, it.refreshToken)
                    AuthResult.Success
                } ?: AuthResult.Error(
                    context.getString(R.string.empty_server_response),
                    field = AuthResult.Field.GENERAL
                )
            } else {
                when (res.code()) {
                    401 -> AuthResult.Error(context.getString(R.string.incorrect_password), field = AuthResult.Field.PASSWORD)
                    404 -> AuthResult.Error(context.getString(R.string.user_not_found), field = AuthResult.Field.EMAIL)
                    else -> AuthResult.Error(
                        "Error: ${res.code()}",
                        field = AuthResult.Field.GENERAL
                    )
                }
            }
        } catch (e: HttpException) {
            AuthResult.Error("HttpException: ${e.message()}", field = AuthResult.Field.GENERAL)
        } catch (e: Exception) {
            AuthResult.Error("Exception: ${e.localizedMessage}", field = AuthResult.Field.GENERAL)
        }
    }

    suspend fun refresh(): Boolean {
        val refreshToken = tokenStorage.refreshTokenFlow.firstOrNull() ?: return false
        val res = api.refresh(RefreshRequest(refreshToken))
        return if (res.isSuccessful) {
            res.body()?.let {
                tokenStorage.saveTokens(it.accessToken, it.refreshToken)
                true
            } ?: false
        } else {
            logout()
            false
        }
    }

    suspend fun logout() {
        tokenStorage.clearTokens()
    }
}
