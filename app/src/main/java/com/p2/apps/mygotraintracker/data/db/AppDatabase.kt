package com.p2.apps.mygotraintracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.p2.apps.mygotraintracker.data.db.dao.StationDao
import com.p2.apps.mygotraintracker.data.db.dao.UserPreferencesDao
import com.p2.apps.mygotraintracker.data.db.entity.StationEntity
import com.p2.apps.mygotraintracker.data.db.entity.UserPreferencesEntity

@Database(entities = [StationEntity::class, UserPreferencesEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
    abstract fun userPreferencesDao(): UserPreferencesDao
} 