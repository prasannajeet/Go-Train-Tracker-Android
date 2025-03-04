package com.p2.apps.mygotraintracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.p2.apps.mygotraintracker.domain.repository.StationRepository
import com.p2.apps.mygotraintracker.presentation.feature.main.MainTabScreen
import com.p2.apps.mygotraintracker.presentation.feature.onboarding.OnboardingScreen
import com.p2.apps.mygotraintracker.presentation.feature.trains.TrainScheduleScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.compose.koinInject

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object MainTab : Screen("main_tab")
    object TrainSchedule : Screen("train_schedule/{fromStationCode}/{toStationCode}") {
        fun createRoute(fromStationCode: String, toStationCode: String) =
            "train_schedule/$fromStationCode/$toStationCode"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    val stationRepository: StationRepository = koinInject()
    
    NavHost(
        navController = navController,
        startDestination = Screen.MainTab.route // Start with MainTab for testing
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onOnboardingComplete = { homeStation ->
                    navController.navigate(Screen.MainTab.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onNavigateToTrainSchedule = { fromStationCode, toStationCode ->
                    navController.navigate(
                        Screen.TrainSchedule.createRoute(
                            fromStationCode,
                            toStationCode
                        )
                    ) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Main tab screen with bottom navigation
        composable(Screen.MainTab.route) {
            MainTabScreen()
        }
        
        // Detailed train schedule screen (when navigating from outside the tab layout)
        composable(
            route = Screen.TrainSchedule.route,
            arguments = listOf(
                navArgument("fromStationCode") { type = NavType.StringType },
                navArgument("toStationCode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val fromStationCode = backStackEntry.arguments?.getString("fromStationCode") ?: ""
            val toStationCode = backStackEntry.arguments?.getString("toStationCode") ?: ""
            
            // Get station names from repository
            val stations by stationRepository.getAllStations().collectAsState(initial = emptyList())
            
            // Find station names
            val fromStation = stations.find { it.locationCode == fromStationCode }
            val toStation = stations.find { it.locationCode == toStationCode }
            
            TrainScheduleScreen(
                fromStationCode = fromStationCode,
                fromStationName = fromStation?.stationName ?: fromStationCode,
                toStationCode = toStationCode,
                toStationName = toStation?.stationName ?: toStationCode,
                onBackClick = { 
                    if (navController.previousBackStackEntry?.destination?.route == Screen.Onboarding.route) {
                        navController.navigate(Screen.MainTab.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
} 