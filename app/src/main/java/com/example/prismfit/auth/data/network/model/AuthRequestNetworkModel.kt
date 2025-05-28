package com.example.prismfit.auth.data.network.model

data class AuthRequestNetworkModel(
    val email: String,
    val password: String,
    val nickname: String,
    val dateOfBirth: Long
)