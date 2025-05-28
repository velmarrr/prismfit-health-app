package com.example.prismfit.auth.domain.model

data class UserCredentials(
    val email: String,
    val password: String,
    val nickname: String,
    val dateOfBirth: Long
)