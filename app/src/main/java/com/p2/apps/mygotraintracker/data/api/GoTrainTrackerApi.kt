package com.p2.apps.mygotraintracker.data.api

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.p2.apps.mygotraintracker.contract.ApiResponse
import com.p2.apps.mygotraintracker.contract.ConnectivityManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

class HttpWebServiceHandler(
    val httpClient: HttpClient,
    val connectivityManager: ConnectivityManager
) {
    suspend inline fun <reified T: ApiResponse> request(
        method: HttpMethod,
        url: String,
    ): Either<T, ApiError> = withContext(Dispatchers.IO) {
        if (!connectivityManager.isNetworkAvailable()) {
            return@withContext ApiError.NetworkError("No internet connection").right()
        }

        try {
            val response = httpClient.request(url) {
                this.method = method
            }

            if (response.status.isSuccess()) {
                response.body<T>().left()
            } else {
                ApiError.ServerError("Server returned error: ${response.status.value}").right()
            }
        } catch (e: Exception) {
            e.toApiError().right()
        }
    }
}

fun Exception.toApiError(): ApiError = when (this) {
    is ClientRequestException -> ApiError.HttpError(response.status.value, message)
    is ServerResponseException -> ApiError.ServerError(message)
    is IOException -> ApiError.NetworkError(message ?: "Network error")
    is SerializationException -> ApiError.SerializationError(message ?: "Serialization error")
    else -> ApiError.UnknownError(message ?: "Unknown error")
}

sealed class ApiError(val message: String) {
    class HttpError(val code: Int, message: String) : ApiError(message)
    class ServerError(message: String) : ApiError(message)
    class NetworkError(message: String) : ApiError(message)
    class SerializationError(message: String) : ApiError(message)
    class UnknownError(message: String) : ApiError(message)
}