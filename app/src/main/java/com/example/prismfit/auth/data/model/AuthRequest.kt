package com.example.prismfit.auth.data.model

data class AuthRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val dateOfBirth: Long
)