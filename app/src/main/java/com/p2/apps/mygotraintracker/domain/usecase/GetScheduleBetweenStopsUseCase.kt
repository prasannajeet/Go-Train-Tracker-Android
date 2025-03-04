package com.p2.apps.mygotraintracker.domain.usecase

import com.p2.apps.mygotraintracker.contract.IRemoteDataSource
import com.p2.apps.mygotraintracker.data.api.dto.Service
import com.p2.apps.mygotraintracker.domain.mapper.TrainTripMapper
import com.p2.apps.mygotraintracker.domain.model.TrainTrip
import com.p2.apps.mygotraintracker.domain.repository.StationRepository

class GetScheduleBetweenStopsUseCase(
    private val remoteDataSource: IRemoteDataSource,
    private val stationRepository: StationRepository
) {
    
    data class Params(
        val date: String,
        val fromStopCode: String,
        val toStopCode: String,
        val startTime: String,
        val maxJourney: Int
    )
    
    suspend fun execute(params: Params): Result<List<TrainTrip>> {
        // Get station code to name mapping
        val stationCodeToNameMap = stationRepository.getStationCodeToNameMap()
        
        return remoteDataSource.getScheduleBetweenStations(
            date = params.date,
            fromStopCode = params.fromStopCode,
            toStopCode = params.toStopCode,
            startTime = params.startTime,
            maxJourney = params.maxJourney
        ).fold(
            ifLeft = { services -> 
                // Map services to train trips with station names
                val trainTrips = services.map { service ->
                    TrainTripMapper.mapToTrainTrip(service, stationCodeToNameMap)
                }
                Result.success(trainTrips)
            },
            ifRight = { error -> Result.failure(error) }
        )
    }
} 