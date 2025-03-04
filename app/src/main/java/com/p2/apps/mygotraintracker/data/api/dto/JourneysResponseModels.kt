package com.p2.apps.mygotraintracker.data.api.dto

import com.p2.apps.mygotraintracker.contract.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduledJourneysResponse(
    @SerialName("Metadata") override val metadata: Metadata,
    @SerialName("SchJourneys") val schJourneys: List<Journey>
): ApiResponse

@Serializable
data class Journey(
    @SerialName("Date") val date: String,
    @SerialName("Time") val time: String,
    @SerialName("To") val to: String,
    @SerialName("From") val from: String,
    @SerialName("Services") val services: List<Service>
)

@Serializable
data class Service(
    @SerialName("Colour") val colour: String,
    @SerialName("Type") val type: String,
    @SerialName("Direction") val direction: String,
    @SerialName("Code") val code: String,
    @SerialName("StartTime") val startTime: String,
    @SerialName("EndTime") val endTime: String,
    @SerialName("Duration") val duration: String,
    @SerialName("Accessible") val accessible: String,
    @SerialName("Trips") val trips: Trips,
    @SerialName("StartSortTime") val startSortTime: String,
    @SerialName("EndSortTime") val endSortTime: String,
    @SerialName("tripHash") val tripHash: String,
    @SerialName("transferCount") val transferCount: Int
)

@Serializable
data class Trips(
    @SerialName("Trip") val trip: List<Trip>
)


@Serializable
data class Trip(
    @SerialName("Number") val number: String,
    @SerialName("Display") val display: String,
    @SerialName("Line") val line: String,
    @SerialName("Direction") val direction: String,
    @SerialName("LineVariant") val lineVariant: String,
    @SerialName("Type") val type: String,
    @SerialName("Stops") val stops: Stops,
    @SerialName("destinationStopCode") val destinationStopCode: String,
    @SerialName("departFromCode") val departFromCode: String,
    @SerialName("departFromAlternativeCode") val departFromAlternativeCode: String?,
    @SerialName("departFromTimingPoint") val departFromTimingPoint: String,
    @SerialName("tripPatternId") val tripPatternId: Int
)


@Serializable
data class Stops(
    @SerialName("Stop") val stop: List<Stop>
)

@Serializable
data class Stop(
    @SerialName("Code") val code: String,
    @SerialName("Order") val order: Int,
    @SerialName("Time") val time: String,
    @SerialName("sortingTime") val sortingTime: String,
    @SerialName("IsMajor") val isMajor: Boolean
)
