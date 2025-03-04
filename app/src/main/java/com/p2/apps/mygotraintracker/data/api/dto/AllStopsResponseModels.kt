package com.p2.apps.mygotraintracker.data.api.dto

import com.p2.apps.mygotraintracker.contract.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllStopsResponse (
    @SerialName("Metadata" ) override val metadata : Metadata,
    @SerialName("Stations" ) val stations : Stations
) : ApiResponse

@Serializable
data class Stations (
    @SerialName("Station" ) val station : ArrayList<Station>
)
@Serializable
data class Station (
    @SerialName("LocationCode") val locationCode : String,
    @SerialName("PublicStopId") val publicStopId : String,
    @SerialName("LocationName") val locationName : String,
    @SerialName("LocationType") val locationType : String
)
