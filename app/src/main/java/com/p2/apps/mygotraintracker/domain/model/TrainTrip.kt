package com.p2.apps.mygotraintracker.domain.model

/**
 * Domain model class representing a train trip to be displayed in the UI
 */
data class TrainTrip(
    val trainNumber: String,
    val line: String,
    val status: TripStatus,
    val departureTime: String,
    val platform: String?,
    val stops: List<StationInfo>,
    val duration: String,
    val isAccessible: Boolean
) {
    /**
     * Returns a formatted string of stops separated by "->"
     */
    fun getFormattedStops(): String = stops.joinToString(" -> ") { it.stationName ?: it.stationCode }
}

/**
 * Represents information about a station
 */
data class StationInfo(
    val stationCode: String,
    val stationName: String? = null
)

/**
 * Represents the status of a train trip
 */
enum class TripStatus {
    ON_TIME,
    DELAYED,
    CANCELLED,
    UNKNOWN;
    
    companion object {
        /**
         * Determines the status based on the trip information
         */
        fun fromStatusString(status: String?): TripStatus {
            return when {
                status == null -> UNKNOWN
                status.contains("Delayed", ignoreCase = true) -> DELAYED
                status.contains("Cancelled", ignoreCase = true) || 
                status.contains("Annulé", ignoreCase = true) -> CANCELLED
                status.contains("On Time", ignoreCase = true) -> ON_TIME
                else -> UNKNOWN
            }
        }
    }
} 