package com.p2.apps.mygotraintracker.presentation.feature.union

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun UnionStationDeparturesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Union Station Departures\nComing Soon",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
} 