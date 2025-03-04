package com.p2.apps.mygotraintracker.presentation.feature.stations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TrainsFromStationScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Trains from Home Station\nComing Soon",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
} 