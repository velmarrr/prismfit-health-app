package com.example.prismfit.auth.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenPairNetworkModel(
    val accessToken: String,
    val refreshToken: String
)