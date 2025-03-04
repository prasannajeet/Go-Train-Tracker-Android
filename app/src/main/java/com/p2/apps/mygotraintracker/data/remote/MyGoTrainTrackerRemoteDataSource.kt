package com.p2.apps.mygotraintracker.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.p2.apps.mygotraintracker.contract.ILogger
import com.p2.apps.mygotraintracker.contract.IRemoteDataSource
import com.p2.apps.mygotraintracker.data.api.ApiPath
import com.p2.apps.mygotraintracker.data.api.HttpWebServiceHandler
import com.p2.apps.mygotraintracker.data.api.dto.AllStopsResponse
import com.p2.apps.mygotraintracker.data.api.dto.Journey
import com.p2.apps.mygotraintracker.data.api.dto.ScheduledJourneysResponse
import com.p2.apps.mygotraintracker.data.api.dto.Service
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation
import io.ktor.http.HttpMethod
import kotlin.math.log

class MyGoTrainTrackerRemoteDataSource(
    private val apiClient: HttpWebServiceHandler,
    private val logger: ILogger
) : IRemoteDataSource {
    
    override suspend fun getAllStations(): Either<List<GOTrainStation>, Throwable> =
        apiClient.request<AllStopsResponse>(HttpMethod.Get, ApiPath.AllStops.path).fold(
            { response ->
                Either.Left(
                    response.stations.station
                        .map { station ->
                            logger.logInfo("Station Name: ${station.locationName} :: Station Type: ${station.locationType}")
                            station
                        }
                        .filter { it.locationType == "Train Station" || it.locationType == "Train & Bus Station" }
                        .map { station ->
                            GOTrainStation(
                                locationCode = station.locationCode,
                                publicStopId = station.publicStopId,
                                stationName = station.locationName
                            )
                        }
                )
            },
            { error ->
                logger.logError("Something went wrong: ${error.message}")
                Either.Right(Throwable(error.message))
            }
        )
    
    override suspend fun getScheduleBetweenStations(
        date: String,
        fromStopCode: String,
        toStopCode: String,
        startTime: String,
        maxJourney: Int
    ): Either<List<Service>, Throwable> =
        apiClient.request<ScheduledJourneysResponse>(
            HttpMethod.Get,
            ApiPath.ScheduleBetweenStops(
                date = date,
                fromStopCode = fromStopCode,
                toStopCode = toStopCode,
                startTime = startTime,
                maxJourney = maxJourney
            ).path
        ).fold(
            { response ->
                if(response.schJourneys.isEmpty()) {
                    Either.Right(Exception("No journeys"))
                } else {
                    Either.Left(response.schJourneys.first().services)
                }
            },
            { error ->
                Either.Right(Throwable(error.message))
            }
        )
} 