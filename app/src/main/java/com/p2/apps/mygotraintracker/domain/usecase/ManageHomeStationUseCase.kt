package com.p2.apps.mygotraintracker.domain.usecase

import com.p2.apps.mygotraintracker.data.db.dao.UserPreferencesDao
import com.p2.apps.mygotraintracker.data.db.entity.UserPreferencesEntity
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ManageHomeStationUseCase(
    private val userPreferencesDao: UserPreferencesDao
) {
    suspend fun saveHomeStation(station: GOTrainStation): Result<Unit> {
        return try {
            val preferences = UserPreferencesEntity(
                homeStationCode = station.locationCode,
                homeStationName = station.stationName,
                homeStationPublicStopId = station.publicStopId
            )
            userPreferencesDao.saveUserPreferences(preferences)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun hasHomeStation(): Boolean {
        return userPreferencesDao.hasHomeStation()
    }
    
    fun observeHomeStation(): Flow<Result<GOTrainStation?>> {
        return userPreferencesDao.getUserPreferences().map { entity ->
            try {
                if (entity != null) {
                    val station = GOTrainStation(
                        locationCode = entity.homeStationCode,
                        stationName = entity.homeStationName,
                        publicStopId = entity.homeStationPublicStopId
                    )
                    Result.success(station)
                } else {
                    Result.success(null)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun clearHomeStation(): Result<Unit> {
        return try {
            userPreferencesDao.clearUserPreferences()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 