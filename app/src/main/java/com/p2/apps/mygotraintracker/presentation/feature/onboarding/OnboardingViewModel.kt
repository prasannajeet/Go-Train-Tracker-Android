package com.p2.apps.mygotraintracker.presentation.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation
import com.p2.apps.mygotraintracker.domain.usecase.GetAllGoTrainStationsUseCase
import com.p2.apps.mygotraintracker.domain.usecase.ManageHomeStationUseCase
import com.p2.apps.mygotraintracker.presentation.utils.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val stationsUseCase: GetAllGoTrainStationsUseCase,
    private val manageHomeStationUseCase: ManageHomeStationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<List<GOTrainStation>>>(ViewState.Loading)
    val state = _state.asStateFlow()
    
    // Union Station is always the destination
    val unionStation = GOTrainStation(
        publicStopId = "UN",
        locationCode = "UN",
        stationName = "Union Station GO"
    )
    
    // MVI state for confirmation dialog
    sealed class DialogState {
        object Hidden : DialogState()
        data class Visible(val station: GOTrainStation) : DialogState()
    }
    
    private val _dialogState = MutableStateFlow<DialogState>(DialogState.Hidden)
    val dialogState = _dialogState.asStateFlow()
    
    // Store the selected home station
    private var selectedHomeStation: GOTrainStation? = null
    
    // New state for initial app state check
    sealed class AppState {
        object Initializing : AppState()
        object NeedsOnboarding : AppState()
        data class HasHomeStation(val fromStationCode: String, val toStationCode: String) : AppState()
    }
    
    private val _appState = MutableStateFlow<AppState>(AppState.Initializing)
    val appState = _appState.asStateFlow()
    
    // MVI events
    sealed class Event {
        data class ShowConfirmationDialog(val station: GOTrainStation) : Event()
        object DismissDialog : Event()
        data class ConfirmHomeStation(val station: GOTrainStation) : Event()
        object RefreshStations : Event()
    }

    init {
        checkHomeStation()
        loadStations()
        observeStations()
    }
    
    private fun checkHomeStation() {
        viewModelScope.launch {
            val hasHomeStation = manageHomeStationUseCase.hasHomeStation()
            
            if (hasHomeStation) {
                // If home station exists, observe it to get the details
                manageHomeStationUseCase.observeHomeStation().collect { result ->
                    result.onSuccess { homeStation ->
                        if (homeStation != null) {
                            // User has a home station, no need for onboarding
                            _appState.value = AppState.HasHomeStation(
                                fromStationCode = homeStation.locationCode,
                                toStationCode = "UN" // Union Station code
                            )
                        } else {
                            // If somehow home station is null, go to onboarding
                            _appState.value = AppState.NeedsOnboarding
                        }
                    }.onFailure {
                        // On error, go to onboarding
                        _appState.value = AppState.NeedsOnboarding
                    }
                }
            } else {
                // If no home station, go to onboarding
                _appState.value = AppState.NeedsOnboarding
            }
        }
    }
    
    fun handleEvent(event: Event) {
        when (event) {
            is Event.ShowConfirmationDialog -> {
                _dialogState.value = DialogState.Visible(event.station)
            }
            is Event.DismissDialog -> {
                _dialogState.value = DialogState.Hidden
            }
            is Event.ConfirmHomeStation -> {
                saveHomeStation(event.station)
                selectedHomeStation = event.station
                _dialogState.value = DialogState.Hidden
            }
            is Event.RefreshStations -> {
                refreshStations()
            }
        }
    }
    
    fun getSelectedHomeStation(): GOTrainStation? {
        return selectedHomeStation
    }
    
    private fun saveHomeStation(station: GOTrainStation) {
        viewModelScope.launch {
            manageHomeStationUseCase.saveHomeStation(station)
        }
    }

    private fun loadStations() {
        viewModelScope.launch {
            _state.value = ViewState.Loading
            stationsUseCase.execute(forceRefresh = false)
                .onFailure { error ->
                    _state.value = ViewState.RenderFailure(error)
                }
        }
    }
    
    private fun observeStations() {
        viewModelScope.launch {
            stationsUseCase.observeStations().collect { result ->
                result.fold(
                    onSuccess = { stations ->
                        // Filter out Union Station from the list if it exists
                        // as it's always the destination and not selectable as home
                        val filteredStations = stations.filter { it.locationCode != unionStation.locationCode }
                        _state.value = ViewState.RenderSuccess(filteredStations)
                    },
                    onFailure = { error ->
                        _state.value = ViewState.RenderFailure(error)
                    }
                )
            }
        }
    }
    
    private fun refreshStations() {
        viewModelScope.launch {
            _state.value = ViewState.Loading
            stationsUseCase.execute(forceRefresh = true)
                .onFailure { error ->
                    _state.value = ViewState.RenderFailure(error)
                }
        }
    }
} 