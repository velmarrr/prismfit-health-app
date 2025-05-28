package com.example.prismfit.auth.data.repository

import com.example.prismfit.R
import com.example.prismfit.auth.domain.model.AuthResult
import com.example.prismfit.auth.data.network.model.RefreshRequestNetworkModel
import com.example.prismfit.auth.data.network.mapper.toDomain
import com.example.prismfit.auth.data.network.mapper.toDto
import com.example.prismfit.auth.data.network.model.ApiError
import com.example.prismfit.auth.data.network.model.ApiResult
import com.example.prismfit.auth.data.network.source.AuthDataSource
import com.example.prismfit.auth.domain.model.UserCredentials
import com.example.prismfit.core.session.TokenStorage
import com.example.prismfit.core.ui.utils.UiText
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val dataSource: AuthDataSource,
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
        return when (val result = dataSource.register(UserCredentials(email, password, nickname, dateOfBirth).toDto())) {
            is ApiResult.Success -> {
                tokenStorage.saveTokens(result.data.accessToken, result.data.refreshToken)
                AuthResult.Success
            }

            is ApiResult.Failure -> mapError(result.error)
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        return when (val result = dataSource.login(UserCredentials(email, password, "", 0).toDto())) {
            is ApiResult.Success -> {
                tokenStorage.saveTokens(result.data.accessToken, result.data.refreshToken)
                AuthResult.Success
            }

            is ApiResult.Failure -> mapError(result.error)
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

            return when (val result = dataSource.refresh(RefreshRequestNetworkModel(refreshToken))) {
                is ApiResult.Success -> {
                    val tokenPair = result.data.toDomain()
                    tokenStorage.saveTokens(tokenPair.accessToken, tokenPair.refreshToken)
                    deferred.complete(true)
                    true
                }

                is ApiResult.Failure -> {
                    logout()
                    deferred.complete(false)
                    false
                }
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

    private fun mapError(error: ApiError): AuthResult {
        return when (error) {
            ApiError.Conflict -> AuthResult.Error(
                UiText.StringResource(R.string.user_already_exists),
                AuthResult.Field.EMAIL
            )
            ApiError.BadRequest -> AuthResult.Error(
                UiText.StringResource(R.string.invalid_registration_data),
                AuthResult.Field.GENERAL
            )
            ApiError.Unauthorized -> AuthResult.Error(
                UiText.StringResource(R.string.incorrect_password),
                AuthResult.Field.PASSWORD
            )
            ApiError.NotFound -> AuthResult.Error(
                UiText.StringResource(R.string.user_not_found),
                AuthResult.Field.EMAIL
            )
            is ApiError.Unknown -> AuthResult.Error(
                UiText.DynamicString("Error: ${error.code}"),
                AuthResult.Field.GENERAL
            )
            is ApiError.Exception -> AuthResult.Error(
                UiText.DynamicString("Exception: ${error.throwable.localizedMessage}"),
                AuthResult.Field.GENERAL
            )
        }
    }
}
