package com.p2.apps.mygotraintracker.presentation.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation
import com.p2.apps.mygotraintracker.presentation.utils.ViewState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
    onOnboardingComplete: (homeStation: GOTrainStation) -> Unit,
    onNavigateToTrainSchedule: (fromStationCode: String, toStationCode: String) -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()
    val appState by viewModel.appState.collectAsState()
    
    // Handle app state to determine if we need onboarding or can go directly to train schedule
    LaunchedEffect(appState) {
        when (appState) {
            is OnboardingViewModel.AppState.HasHomeStation -> {
                val hasHomeState = appState as OnboardingViewModel.AppState.HasHomeStation
                onNavigateToTrainSchedule(hasHomeState.fromStationCode, hasHomeState.toStationCode)
            }
            OnboardingViewModel.AppState.NeedsOnboarding -> {
                // Continue with onboarding
            }
            OnboardingViewModel.AppState.Initializing -> {
                // Show splash screen (handled by the UI below)
            }
        }
    }
    
    // If we're still initializing, show splash screen
    if (appState is OnboardingViewModel.AppState.Initializing) {
        SplashContent()
        return
    }
    
    // If we need onboarding, show the onboarding flow
    if (appState is OnboardingViewModel.AppState.NeedsOnboarding) {
        OnboardingContent(
            state = state,
            dialogState = dialogState,
            onStationSelected = { station ->
                viewModel.handleEvent(OnboardingViewModel.Event.ShowConfirmationDialog(station))
            },
            onConfirmStation = { station ->
                viewModel.handleEvent(OnboardingViewModel.Event.ConfirmHomeStation(station))
            },
            onDismissDialog = {
                viewModel.handleEvent(OnboardingViewModel.Event.DismissDialog)
            },
            onOnboardingComplete = {
                viewModel.getSelectedHomeStation()?.let { homeStation ->
                    onOnboardingComplete(homeStation)
                }
            }
        )
    }
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingContent(
    state: ViewState<List<GOTrainStation>>,
    dialogState: OnboardingViewModel.DialogState,
    onStationSelected: (GOTrainStation) -> Unit,
    onConfirmStation: (GOTrainStation) -> Unit,
    onDismissDialog: () -> Unit,
    onOnboardingComplete: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })
    
    // Handle confirmation dialog
    when (val currentDialogState = dialogState) {
        is OnboardingViewModel.DialogState.Visible -> {
            val station = currentDialogState.station
            AlertDialog(
                onDismissRequest = onDismissDialog,
                title = { Text("Confirm Home Station") },
                text = { Text("Are you sure you want to save ${station.stationName} as your home station?") },
                confirmButton = {
                    Button(
                        onClick = {
                            onConfirmStation(station)
                            // Move to the next page after confirmation
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDialog) {
                        Text("Cancel")
                    }
                }
            )
        }
        OnboardingViewModel.DialogState.Hidden -> {
            // No dialog shown
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Pager for onboarding screens
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            userScrollEnabled = false // Disable manual swiping
        ) { page ->
            when (page) {
                0 -> WelcomeScreen(state)
                1 -> StationSelectionScreen(
                    state = state,
                    onStationSelected = onStationSelected
                )
                2 -> CompletionScreen(
                    onGetStarted = onOnboardingComplete
                )
            }
        }
        
        // Page indicator and navigation buttons
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Page indicator dots
            PageIndicator(
                pageCount = 3,
                currentPage = pagerState.currentPage,
                modifier = Modifier.align(Alignment.Center)
            )
            
            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back button (hidden on first page)
                AnimatedVisibility(
                    visible = pagerState.currentPage > 0,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    ) {
                        Text("Back")
                    }
                }
                
                // Next button (only on first page)
                AnimatedVisibility(
                    visible = pagerState.currentPage == 0,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        enabled = state is ViewState.RenderSuccess<*>
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(state: ViewState<List<GOTrainStation>>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to GO Train Tracker",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Your personal assistant for tracking GO Train schedules between your home station and Union Station.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        if (state is ViewState.Loading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Please wait while we configure the app...",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationSelectionScreen(
    state: ViewState<List<GOTrainStation>>,
    onStationSelected: (GOTrainStation) -> Unit
) {
    var selectedHomeStation by remember { mutableStateOf<GOTrainStation?>(null) }
    var isHomeStationExpanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Your Home Station",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Choose the GO Train station closest to your home. We'll show you schedules from there to Union Station.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        when (state) {
            is ViewState.Loading -> {
                CircularProgressIndicator()
            }
            is ViewState.RenderSuccess -> {
                val stations = state.data
                
                // Home Station Dropdown
                ExposedDropdownMenuBox(
                    expanded = isHomeStationExpanded,
                    onExpandedChange = { isHomeStationExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedHomeStation?.stationName ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Home Station") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isHomeStationExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isHomeStationExpanded,
                        onDismissRequest = { isHomeStationExpanded = false }
                    ) {
                        stations.forEach { station ->
                            DropdownMenuItem(
                                text = { Text(station.stationName) },
                                onClick = {
                                    selectedHomeStation = station
                                    isHomeStationExpanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        selectedHomeStation?.let { homeStation ->
                            onStationSelected(homeStation)
                        }
                    },
                    enabled = selectedHomeStation != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Home Station")
                }
            }
            is ViewState.RenderFailure -> {
                Text(
                    text = "Error: ${state.throwable.message}",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CompletionScreen(onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "You're All Set!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Your home station has been saved. You can now start tracking GO Train schedules.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onGetStarted,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Started")
        }
    }
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pageCount) { page ->
            val isSelected = page == currentPage
            val size = animateDpAsState(
                targetValue = if (isSelected) 12.dp else 8.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "Dot size animation"
            )
            
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(size.value)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
            )
        }
    }
} 