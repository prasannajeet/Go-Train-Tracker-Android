package com.p2.apps.mygotraintracker.domain.repository

import com.p2.apps.mygotraintracker.contract.ILocalDataSource
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Repository for accessing station information
 */
class StationRepository(private val localDataSource: ILocalDataSource) {
    
    /**
     * Get all stations from the local database
     */
    fun getAllStations(): Flow<List<GOTrainStation>> {
        return localDataSource.getAllStations()
    }
    
    /**
     * Get a station by its code
     */
    suspend fun getStationByCode(stationCode: String): GOTrainStation? {
        return localDataSource.getAllStations().first().find { 
            it.locationCode == stationCode 
        }
    }
    
    /**
     * Get a map of station codes to station names
     */
    suspend fun getStationCodeToNameMap(): Map<String, String> {
        return localDataSource.getAllStations().first().associate { 
            it.locationCode to it.stationName 
        }
    }
} 