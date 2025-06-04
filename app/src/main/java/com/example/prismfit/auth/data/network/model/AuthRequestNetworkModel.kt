package com.example.prismfit.auth.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequestNetworkModel(
    val email: String,
    val password: String,
    val nickname: String,
    val dateOfBirth: Long
)