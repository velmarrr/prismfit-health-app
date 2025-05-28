package com.example.prismfit.auth.data.network.api

import com.example.prismfit.auth.data.network.model.ApiError
import com.example.prismfit.auth.data.network.model.ApiResult
import retrofit2.Response

suspend inline fun <T> handleApiCall(
    crossinline call: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Failure(ApiError.Unknown(-1))
        } else {
            val error = when (response.code()) {
                400 -> ApiError.BadRequest
                401 -> ApiError.Unauthorized
                404 -> ApiError.NotFound
                409 -> ApiError.Conflict
                else -> ApiError.Unknown(response.code())
            }
            ApiResult.Failure(error)
        }
    } catch (e: Exception) {
        ApiResult.Failure(ApiError.Exception(e))
    }
}
