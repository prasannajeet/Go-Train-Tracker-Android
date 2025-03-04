package com.p2.apps.mygotraintracker.data.api.dto

import com.p2.apps.mygotraintracker.contract.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllLinesResponse(
    @SerialName("Metadata") override val metadata: Metadata,
    @SerialName("AllLines") val allLines: AllLines
): ApiResponse

@Serializable
data class AllLines(
    @SerialName("Line") val lines: List<Line>
)

@Serializable
data class Line(
    @SerialName("Name") val name: String,
    @SerialName("Code") val code: String,
    @SerialName("IsBus") val isBus: Boolean,
    @SerialName("IsTrain") val isTrain: Boolean,
    @SerialName("Variant") val variants: List<Variant>
)

@Serializable
data class Variant(
    @SerialName("Code") val code: String,
    @SerialName("Display") val display: String,
    @SerialName("Direction") val direction: String
)
