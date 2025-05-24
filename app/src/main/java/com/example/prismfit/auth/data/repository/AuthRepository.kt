package com.example.prismfit.auth.data.repository

import com.example.prismfit.R
import com.example.prismfit.auth.data.model.AuthRequest
import com.example.prismfit.auth.data.model.AuthResult
import com.example.prismfit.auth.data.model.RefreshRequest
import com.example.prismfit.auth.data.remote.AuthApi
import com.example.prismfit.core.session.TokenStorage
import com.example.prismfit.core.ui.utils.UiText
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) {
    private val mutex = Mutex()
    private var refreshInProgress: CompletableDeferred<Boolean>? = null

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
                    UiText.StringResource(R.string.empty_server_response),
                    field = AuthResult.Field.GENERAL
                )
            } else {
                when (res.code()) {
                    409 -> AuthResult.Error(
                        UiText.StringResource(R.string.user_already_exists),
                        field = AuthResult.Field.EMAIL
                    )

                    400 -> AuthResult.Error(
                        UiText.StringResource(R.string.invalid_registration_data),
                        field = AuthResult.Field.GENERAL
                    )

                    else -> AuthResult.Error(
                        UiText.DynamicString("Error: ${res.code()}"),
                        field = AuthResult.Field.GENERAL
                    )
                }
            }
        } catch (e: HttpException) {
            AuthResult.Error(
                UiText.DynamicString("HttpException: ${e.message()}"),
                field = AuthResult.Field.GENERAL
            )
        } catch (e: Exception) {
            AuthResult.Error(
                UiText.DynamicString("Exception: ${e.localizedMessage}"),
                field = AuthResult.Field.GENERAL
            )
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
                    UiText.StringResource(R.string.empty_server_response),
                    field = AuthResult.Field.GENERAL
                )
            } else {
                when (res.code()) {
                    401 -> AuthResult.Error(UiText.StringResource(R.string.incorrect_password), field = AuthResult.Field.PASSWORD)
                    404 -> AuthResult.Error(UiText.StringResource(R.string.user_not_found), field = AuthResult.Field.EMAIL)
                    else -> AuthResult.Error(
                        UiText.DynamicString("Error: ${res.code()}"),
                        field = AuthResult.Field.GENERAL
                    )
                }
            }
        } catch (e: HttpException) {
            AuthResult.Error(
                UiText.DynamicString("HttpException: ${e.message()}"),
                field = AuthResult.Field.GENERAL
            )
        } catch (e: Exception) {
            AuthResult.Error(
                UiText.DynamicString("Exception: ${e.localizedMessage}"),
                field = AuthResult.Field.GENERAL
            )
        }
    }

    suspend fun refresh(): Boolean {
        mutex.lock()
        try {
            refreshInProgress?.let {
                mutex.unlock()
                return it.await()
            }

            val deferred = CompletableDeferred<Boolean>()
            refreshInProgress = deferred
            mutex.unlock()

            val refreshToken = tokenStorage.refreshTokenFlow.firstOrNull()
            if (refreshToken == null) {
                deferred.complete(false)
                refreshInProgress = null
                return false
            }

            return try {
                val res = api.refresh(RefreshRequest(refreshToken))
                if (res.isSuccessful) {
                    res.body()?.let {
                        tokenStorage.saveTokens(it.accessToken, it.refreshToken)
                        deferred.complete(true)
                        true
                    } ?: run {
                        deferred.complete(false)
                        false
                    }
                } else {
                    logout()
                    deferred.complete(false)
                    false
                }
            } catch (e: Exception) {
                logout()
                deferred.complete(false)
                false
            }
        } finally {
            mutex.withLock {
                refreshInProgress = null
            }
        }
    }

    suspend fun logout() {
        tokenStorage.clearTokens()
    }
}
