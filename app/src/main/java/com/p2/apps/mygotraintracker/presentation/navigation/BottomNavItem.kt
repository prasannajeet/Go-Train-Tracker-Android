package com.p2.apps.mygotraintracker.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a bottom navigation item
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    // Schedule tab with calendar icon
    object Schedule : BottomNavItem(
        route = "schedule",
        title = "Schedule",
        icon = Icons.Default.DateRange
    )
    
    // Station tab with home icon
    object Station : BottomNavItem(
        route = "station",
        title = "Station",
        icon = Icons.Default.Home
    )
    
    // Union Station tab with train icon
    object Union : BottomNavItem(
        route = "union",
        title = "Union",
        icon = Icons.Default.LocationOn
    )
    
    // Settings tab with settings icon
    object Settings : BottomNavItem(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
    
    // List of all bottom navigation items
    companion object {
        val items = listOf(
            Schedule,
            Station,
            Union,
            Settings
        )
    }
} 