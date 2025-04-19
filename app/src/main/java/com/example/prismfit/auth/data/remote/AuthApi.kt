package com.example.prismfit.auth.data.remote

import com.example.prismfit.auth.data.model.AuthRequest
import com.example.prismfit.auth.data.model.RefreshRequest
import com.example.prismfit.auth.data.model.TokenPair
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest): Response<TokenPair>

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<TokenPair>

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): Response<TokenPair>
}
