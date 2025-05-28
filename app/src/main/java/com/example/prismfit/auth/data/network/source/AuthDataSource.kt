package com.example.prismfit.auth.data.network.source

import com.example.prismfit.auth.data.network.api.AuthApiService
import com.example.prismfit.auth.data.network.api.handleApiCall
import com.example.prismfit.auth.data.network.model.ApiResult
import com.example.prismfit.auth.data.network.model.AuthRequestNetworkModel
import com.example.prismfit.auth.data.network.model.RefreshRequestNetworkModel
import com.example.prismfit.auth.data.network.model.TokenPairNetworkModel
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val api: AuthApiService
) {
    suspend fun register(request: AuthRequestNetworkModel): ApiResult<TokenPairNetworkModel> {
        return handleApiCall { api.register(request) }
    }

    suspend fun login(request: AuthRequestNetworkModel): ApiResult<TokenPairNetworkModel> {
        return handleApiCall { api.login(request) }
    }

    suspend fun refresh(request: RefreshRequestNetworkModel): ApiResult<TokenPairNetworkModel> {
        return handleApiCall { api.refresh(request) }
    }
}
