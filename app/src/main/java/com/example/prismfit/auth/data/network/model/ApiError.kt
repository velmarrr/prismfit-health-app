package com.example.prismfit.auth.data.network.model

sealed class ApiError {
    object Conflict : ApiError()
    object BadRequest : ApiError()
    object Unauthorized : ApiError()
    object NotFound : ApiError()
    data class Unknown(val code: Int) : ApiError()
    data class Exception(val throwable: Throwable) : ApiError()
}