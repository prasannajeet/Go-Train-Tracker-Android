package com.p2.apps.mygotraintracker.presentation.feature.trains

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.p2.apps.mygotraintracker.domain.model.TrainTrip
import com.p2.apps.mygotraintracker.domain.model.TripStatus
import com.p2.apps.mygotraintracker.presentation.components.LoadingContent
import com.p2.apps.mygotraintracker.presentation.components.SectionHeader
import com.p2.apps.mygotraintracker.presentation.components.StationInfoHeader
import com.p2.apps.mygotraintracker.presentation.components.TrainCard
import com.p2.apps.mygotraintracker.presentation.utils.DateFormatter
import com.p2.apps.mygotraintracker.presentation.utils.ViewState
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainScheduleScreen(
    fromStationCode: String,
    fromStationName: String,
    toStationCode: String,
    toStationName: String,
    onBackClick: () -> Unit,
    onTrainClick: (TrainTrip) -> Unit = {},
    viewModel: TrainScheduleViewModel = koinViewModel { parametersOf(fromStationCode, toStationCode) }
) {
    val state by viewModel.state.collectAsState()
    
    // Format current date for display
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val formattedDate = DateFormatter.formatToReadableDate(today)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Train Schedule") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshSchedule() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LoadingContent(
            state = state,
            emptyContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StationInfoHeader(
                            fromStation = fromStationName,
                            toStation = toStationName,
                            date = formattedDate,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        
                        Text(
                            text = "No trains available for this route",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            }
        ) { trainTrips ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Station info header
                StationInfoHeader(
                    fromStation = fromStationName,
                    toStation = toStationName,
                    date = formattedDate,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                // Train schedules
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Departing soon section - all on-time trains
                    val departingSoon = trainTrips.filter { it.status == TripStatus.ON_TIME }
                    if (departingSoon.isNotEmpty()) {
                        item {
                            SectionHeader(title = "Departing Soon")
                        }
                        
                        items(departingSoon) { trainTrip ->
                            TrainCard(
                                trainTrip = trainTrip,
                                onClick = { onTrainClick(trainTrip) }
                            )
                        }
                    }
                    
                    // Delayed trains section
                    val delayedTrains = trainTrips.filter { it.status == TripStatus.DELAYED }
                    if (delayedTrains.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Delayed Trains",
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                        
                        items(delayedTrains) { trainTrip ->
                            TrainCard(
                                trainTrip = trainTrip,
                                onClick = { onTrainClick(trainTrip) }
                            )
                        }
                    }
                    
                    // Cancelled trains section
                    val cancelledTrains = trainTrips.filter { it.status == TripStatus.CANCELLED }
                    if (cancelledTrains.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Cancelled Trains",
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                        
                        items(cancelledTrains) { trainTrip ->
                            TrainCard(
                                trainTrip = trainTrip,
                                onClick = { onTrainClick(trainTrip) }
                            )
                        }
                    }
                    
                    // Other trains section
                    val otherTrains = trainTrips.filter { 
                        it.status == TripStatus.UNKNOWN
                    }
                    
                    if (otherTrains.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Other Trains",
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                        
                        items(otherTrains) { trainTrip ->
                            TrainCard(
                                trainTrip = trainTrip,
                                onClick = { onTrainClick(trainTrip) }
                            )
                        }
                    }
                }
            }
        }
    }
} 