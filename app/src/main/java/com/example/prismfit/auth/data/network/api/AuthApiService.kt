package com.example.prismfit.auth.data.network.api

import com.example.prismfit.auth.data.network.model.AuthRequestNetworkModel
import com.example.prismfit.auth.data.network.model.RefreshRequestNetworkModel
import com.example.prismfit.auth.data.network.model.TokenPairNetworkModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body request: AuthRequestNetworkModel): Response<TokenPairNetworkModel>

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequestNetworkModel): Response<TokenPairNetworkModel>

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequestNetworkModel): Response<TokenPairNetworkModel>
}
