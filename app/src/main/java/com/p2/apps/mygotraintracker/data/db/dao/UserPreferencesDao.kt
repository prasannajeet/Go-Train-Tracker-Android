package com.p2.apps.mygotraintracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.p2.apps.mygotraintracker.data.db.entity.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>
    
    @Query("SELECT EXISTS(SELECT 1 FROM user_preferences WHERE id = 1 LIMIT 1)")
    suspend fun hasHomeStation(): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserPreferences(preferences: UserPreferencesEntity)
    
    @Query("DELETE FROM user_preferences")
    suspend fun clearUserPreferences()
} 