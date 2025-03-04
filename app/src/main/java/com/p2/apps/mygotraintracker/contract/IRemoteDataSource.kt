package com.p2.apps.mygotraintracker.contract

import arrow.core.Either
import com.p2.apps.mygotraintracker.data.api.dto.Journey
import com.p2.apps.mygotraintracker.data.api.dto.Service
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation

interface IRemoteDataSource {
    suspend fun getAllStations(): Either<List<GOTrainStation>, Throwable>
    suspend fun getScheduleBetweenStations(
        date: String,
        fromStopCode: String,
        toStopCode: String,
        startTime: String,
        maxJourney: Int
    ): Either<List<Service>, Throwable>
}
