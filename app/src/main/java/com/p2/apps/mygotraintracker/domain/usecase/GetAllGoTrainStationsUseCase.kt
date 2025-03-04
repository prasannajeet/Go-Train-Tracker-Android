package com.p2.apps.mygotraintracker.domain.usecase

import com.p2.apps.mygotraintracker.contract.ILocalDataSource
import com.p2.apps.mygotraintracker.contract.IRemoteDataSource
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetAllGoTrainStationsUseCase(
    private val remoteDataSource: IRemoteDataSource,
    private val localDataSource: ILocalDataSource
) {
    suspend fun execute(forceRefresh: Boolean = false): Result<List<GOTrainStation>> {
        return try {
            // If not forcing refresh, try to get from local first
            if (!forceRefresh) {
                val localStations = localDataSource.getAllStations().first()
                if (localStations.isNotEmpty()) {
                    return Result.success(localStations)
                }
            }
            
            // If local is empty or forcing refresh, get from remote
            remoteDataSource.getAllStations().fold(
                ifLeft = { stations ->
                    // Save to local database
                    localDataSource.saveStations(stations)
                    Result.success(stations)
                },
                ifRight = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun observeStations(): Flow<Result<List<GOTrainStation>>> = flow {
        try {
            // Emit from local database and keep observing
            localDataSource.getAllStations().collect { stations ->
                emit(Result.success(stations))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
