package com.p2.apps.mygotraintracker.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.p2.apps.mygotraintracker.domain.model.TrainTrip
import com.p2.apps.mygotraintracker.domain.model.TripStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainCard(
    trainTrip: TrainTrip,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Train number, type and departure time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Train type indicator (line)
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = trainTrip.line,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    // Train number
                    Text(
                        text = trainTrip.trainNumber,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Departure time
                Text(
                    text = trainTrip.departureTime,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Status indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = when(trainTrip.status) {
                        TripStatus.ON_TIME -> Color(0xFF4CAF50) // Green
                        TripStatus.DELAYED -> Color(0xFFFFC107) // Yellow
                        TripStatus.CANCELLED -> Color(0xFFF44336) // Red
                        TripStatus.UNKNOWN -> Color(0xFF9E9E9E) // Gray
                    },
                    contentColor = Color.White
                ) {
                    Text(
                        text = when(trainTrip.status) {
                            TripStatus.ON_TIME -> "On Time"
                            TripStatus.DELAYED -> "Delayed"
                            TripStatus.CANCELLED -> "Cancelled"
                            TripStatus.UNKNOWN -> "Unknown"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                // Platform information if available
                trainTrip.platform?.let { platform ->
                    Text(
                        text = "Platform: $platform",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            
            // Route information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Duration information
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Duration",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = trainTrip.duration,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Stops information
            if (trainTrip.stops.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Stops:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = trainTrip.getFormattedStops(),
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Visible
                        )
                    }
                }
            }
            
            // Accessibility information if available
            if (trainTrip.isAccessible) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "♿ Accessible",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
} 