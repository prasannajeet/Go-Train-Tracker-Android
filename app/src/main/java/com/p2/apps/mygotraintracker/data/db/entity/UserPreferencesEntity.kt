package com.p2.apps.mygotraintracker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey
    val id: Int = 1, // Single row for user preferences
    val homeStationCode: String,
    val homeStationName: String,
    val homeStationPublicStopId: String
) 