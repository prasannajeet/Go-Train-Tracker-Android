package com.p2.apps.mygotraintracker.data.api

/**
 * Represents the various API endpoints used in the GO Train Tracker application.
 * This sealed class ensures type-safe API path construction for different endpoints.
 * Each subclass represents a specific API endpoint with its corresponding path structure.
 */
sealed class ApiPath(val path: String) {

    /**
     * Represents an API endpoint for retrieving scheduled trips between two stops.
     *
     * @property date The date for which to retrieve the schedule (format should match API requirements)
     * @property fromStopCode The code of the departure station
     * @property toStopCode The code of the destination station
     * @property startTime The desired start time for the journey
     * @property maxJourney The maximum number of journeys to return
     */
    data class ScheduleBetweenStops(
        private val date: String,
        private val fromStopCode: String,
        private val toStopCode: String,
        private val startTime: String,
        private val maxJourney: Int
    ) : ApiPath("Schedule/Journey/$date/$fromStopCode/$toStopCode/$startTime/$maxJourney")

    /**
     * Represents an API endpoint for retrieving all current departures from Union Station.
     * This endpoint provides real-time departure information for trains leaving Union Station.
     */
    data object AllUnionStationDepartures : ApiPath("ServiceUpdate/UnionDeparturesAll")

    /**
     * Represents an API endpoint for retrieving all available GO Transit stops.
     * This endpoint returns a list of all stations and stops in the GO Transit network.
     */
    data object AllStops: ApiPath("Stop/All")

    /**
     * Represents an API endpoint for retrieving all GO Transit lines for a specific date.
     *
     * @property date The date for which to retrieve the line information (format should match API requirements)
     */
    data class AllLines(private val date: String): ApiPath("Schedule/Line/All/$date")
}
