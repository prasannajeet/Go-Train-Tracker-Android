package com.p2.apps.mygotraintracker.utils

sealed class ApiError(val message: String) {
    data class HttpError(val code: Int, val errorMessage: String) : ApiError(errorMessage)
    data class ServerError(val code: Int, val errorMessage: String) : ApiError(errorMessage)
    data class SerializationError(val errorMessage: String) : ApiError(errorMessage)
    data class UnknownError(val errorMessage: String) : ApiError(errorMessage)
}