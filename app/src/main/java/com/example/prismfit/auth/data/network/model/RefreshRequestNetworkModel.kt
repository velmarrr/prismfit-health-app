package com.example.prismfit.auth.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequestNetworkModel(
    val refreshToken: String
)