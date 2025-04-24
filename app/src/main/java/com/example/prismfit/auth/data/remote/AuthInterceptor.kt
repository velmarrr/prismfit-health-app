package com.example.prismfit.auth.data.remote

import com.example.prismfit.auth.data.repository.AuthRepository
import com.example.prismfit.core.session.TokenStorage
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val authRepository: Provider<AuthRepository>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = runBlocking { tokenStorage.accessTokenFlow.firstOrNull() }
        val originalRequest = chain.request()

        val authenticatedRequest = if (accessToken != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else originalRequest

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
            response.close()
            val refreshed = runBlocking { authRepository.get().refresh() }

            if (refreshed) {
                accessToken = runBlocking { tokenStorage.accessTokenFlow.firstOrNull() }
                val retryRequest = originalRequest.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
                return chain.proceed(retryRequest)
            }
        }
        return response
    }
}
