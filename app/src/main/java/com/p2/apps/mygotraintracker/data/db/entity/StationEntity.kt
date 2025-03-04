package com.p2.apps.mygotraintracker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation

@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey
    val locationCode: String,
    val publicStopId: String,
    val stationName: String
) {
    fun toDomainModel() = GOTrainStation(
        locationCode = locationCode,
        publicStopId = publicStopId,
        stationName = stationName
    )

    companion object {
        fun fromDomainModel(station: GOTrainStation) = StationEntity(
            locationCode = station.locationCode,
            publicStopId = station.publicStopId,
            stationName = station.stationName
        )
    }
} 