package com.p2.apps.mygotraintracker.contract

import kotlinx.coroutines.flow.Flow
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation


interface ILocalDataSource {
    fun getAllStations(): Flow<List<GOTrainStation>>
    suspend fun saveStations(stations: List<GOTrainStation>)
    suspend fun clearStations()
}