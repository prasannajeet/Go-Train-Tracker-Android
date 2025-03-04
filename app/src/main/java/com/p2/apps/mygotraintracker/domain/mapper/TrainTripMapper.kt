package com.p2.apps.mygotraintracker.domain.mapper

import com.p2.apps.mygotraintracker.data.api.dto.Service
import com.p2.apps.mygotraintracker.domain.model.StationInfo
import com.p2.apps.mygotraintracker.domain.model.TrainTrip
import com.p2.apps.mygotraintracker.domain.model.TripStatus

/**
 * Mapper class to convert Service DTO to TrainTrip domain model
 */
object TrainTripMapper {
    
    /**
     * Converts a Service DTO to a TrainTrip domain model
     */
    fun mapToTrainTrip(service: Service, stationCodeToNameMap: Map<String, String> = emptyMap()): TrainTrip {
        val firstTrip = service.trips.trip.firstOrNull()
        
        // Extract platform information if available
        val platformInfo = extractPlatformInfo(service)
        
        // Determine trip status
        val status = determineTripStatus(service, firstTrip?.stops?.stop)
        
        // Map stop codes to StationInfo objects with names if available
        val stops = firstTrip?.stops?.stop?.map { stop ->
            StationInfo(
                stationCode = stop.code,
                stationName = stationCodeToNameMap[stop.code]
            )
        } ?: emptyList()
        
        return TrainTrip(
            trainNumber = service.trips.trip.first().number,
            line = firstTrip?.line ?: "Unknown",
            status = status,
            departureTime = formatTime(service.startTime),
            platform = platformInfo,
            stops = stops,
            duration = service.duration,
            isAccessible = service.accessible == "1"
        )
    }
    
    /**
     * Formats the time string to hh:mm format
     */
    private fun formatTime(timeString: String): String {
        // If the time is already in hh:mm format, return it
        if (timeString.matches(Regex("\\d{1,2}:\\d{2}"))) {
            return timeString
        }
        
        // Otherwise, try to extract hours and minutes
        val timeRegex = Regex("(\\d{1,2}):(\\d{2}).*")
        val matchResult = timeRegex.find(timeString)
        
        return if (matchResult != null) {
            val (hours, minutes) = matchResult.destructured
            "$hours:$minutes"
        } else {
            timeString // Return original if no match
        }
    }
    
    /**
     * Extracts platform information if available
     */
    private fun extractPlatformInfo(service: Service): String? {
        // Platform information might be in different places depending on the API
        // This is a placeholder - adjust based on actual API response structure
        return null
    }
    
    /**
     * Determines the status of the trip based on service and stop information
     */
    private fun determineTripStatus(service: Service, stops: List<com.p2.apps.mygotraintracker.data.api.dto.Stop>?): TripStatus {
        // Check if any stop has delay information
        val hasDelays = stops?.any { 
            it.time.contains("Delayed", ignoreCase = true) 
        } ?: false
        
        // Check if service is cancelled
        val isCancelled = service.type.contains("Cancelled", ignoreCase = true) ||
                service.type.contains("Annulé", ignoreCase = true)
        
        return when {
            isCancelled -> TripStatus.CANCELLED
            hasDelays -> TripStatus.DELAYED
            else -> TripStatus.ON_TIME
        }
    }
} 