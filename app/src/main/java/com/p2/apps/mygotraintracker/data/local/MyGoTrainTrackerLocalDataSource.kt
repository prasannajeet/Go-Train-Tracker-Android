package com.p2.apps.mygotraintracker.data.local

import com.p2.apps.mygotraintracker.contract.ILocalDataSource
import com.p2.apps.mygotraintracker.data.db.dao.StationDao
import com.p2.apps.mygotraintracker.data.db.entity.StationEntity
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyGoTrainTrackerLocalDataSource(
    private val stationDao: StationDao
) : ILocalDataSource {
    
    override fun getAllStations(): Flow<List<GOTrainStation>> =
        stationDao.getAllStations().map { entities ->
            entities.map { it.toDomainModel() }
        }

    override suspend fun saveStations(stations: List<GOTrainStation>) {
        stationDao.insertStations(stations.map { StationEntity.fromDomainModel(it) })
    }

    override suspend fun clearStations() {
        stationDao.deleteAllStations()
    }
} 