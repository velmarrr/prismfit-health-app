package com.example.prismfit.auth.data.model

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String, val field: Field? = null) : AuthResult()

    enum class Field {
        EMAIL, PASSWORD, GENERAL
    }
}