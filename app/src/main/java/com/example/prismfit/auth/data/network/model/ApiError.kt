package com.example.prismfit.auth.data.network.model

sealed class ApiError {
    data object Conflict : ApiError()
    data object BadRequest : ApiError()
    data object Unauthorized : ApiError()
    data object NotFound : ApiError()
    data class Unknown(val code: Int) : ApiError()
    data class Exception(val throwable: Throwable) : ApiError()
}