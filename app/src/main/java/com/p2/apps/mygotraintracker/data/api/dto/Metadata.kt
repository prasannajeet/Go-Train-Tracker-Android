package com.p2.apps.mygotraintracker.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    @SerialName("TimeStamp") val timeStamp: String,
    @SerialName("ErrorCode") val errorCode: String,
    @SerialName("ErrorMessage") val errorMessage: String
)
