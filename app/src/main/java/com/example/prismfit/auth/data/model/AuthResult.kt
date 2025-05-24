package com.example.prismfit.auth.data.model

import com.example.prismfit.core.ui.utils.UiText

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: UiText, val field: Field? = null) : AuthResult()

    enum class Field {
        EMAIL, PASSWORD, GENERAL
    }
}