package com.p2.apps.mygotraintracker.presentation.feature.trains

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation
import com.p2.apps.mygotraintracker.domain.repository.StationRepository
import org.koin.compose.koinInject

/**
 * A wrapper around TrainScheduleScreen for use in the tab layout
 */
@Composable
fun ScheduleTabScreen(
    onNavigateBack: () -> Unit = {}
) {
    // Get station repository to fetch station information
    val stationRepository: StationRepository = koinInject()
    
    // Get all stations
    val stations by stationRepository.getAllStations().collectAsState(initial = emptyList())
    
    // Default to Union Station if no stations are available
    val fromStation = stations.firstOrNull() ?: GOTrainStation("UN", "UN", "Union Station")
    val toStation = stations.find { it.locationCode == "UN" } ?: GOTrainStation("UN", "UN", "Union Station")
    
    TrainScheduleScreen(
        fromStationCode = fromStation.locationCode,
        fromStationName = fromStation.stationName,
        toStationCode = toStation.locationCode,
        toStationName = toStation.stationName,
        onBackClick = onNavigateBack
    )
} 