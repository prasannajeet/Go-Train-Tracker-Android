package com.p2.apps.mygotraintracker.presentation.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.p2.apps.mygotraintracker.presentation.feature.settings.SettingsScreen
import com.p2.apps.mygotraintracker.presentation.feature.stations.TrainsFromStationScreen
import com.p2.apps.mygotraintracker.presentation.feature.trains.ScheduleTabScreen
import com.p2.apps.mygotraintracker.presentation.feature.union.UnionStationDeparturesScreen
import com.p2.apps.mygotraintracker.presentation.navigation.BottomNavItem

/**
 * Main screen with bottom tab navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabScreen() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                BottomNavItem.items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Schedule.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Schedule.route) {
                ScheduleTabScreen()
            }
            
            composable(BottomNavItem.Station.route) {
                TrainsFromStationScreen()
            }
            
            composable(BottomNavItem.Union.route) {
                UnionStationDeparturesScreen()
            }
            
            composable(BottomNavItem.Settings.route) {
                SettingsScreen()
            }
        }
    }
} 