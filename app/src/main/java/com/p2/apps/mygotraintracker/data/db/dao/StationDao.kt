package com.p2.apps.mygotraintracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.p2.apps.mygotraintracker.data.db.entity.StationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Query("SELECT * FROM stations ORDER BY stationName ASC")
    fun getAllStations(): Flow<List<StationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)

    @Query("DELETE FROM stations")
    suspend fun deleteAllStations()
} 