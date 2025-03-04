package com.p2.apps.mygotraintracker.presentation.feature.journeys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p2.apps.mygotraintracker.domain.model.GOTrainStation
import com.p2.apps.mygotraintracker.domain.usecase.GetAllGOTrainLinesUseCase
import com.p2.apps.mygotraintracker.domain.usecase.GetAllGoTrainStationsUseCase
import com.p2.apps.mygotraintracker.presentation.utils.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


/**
 * ViewModel responsible for managing GO Train station data and UI state for the Journeys screen.
 * It handles loading stations, refreshing data, and maintaining the UI state through a [ViewState] flow.
 *
 * @property stationsUseCase The use case responsible for fetching GO Train stations
 * @property linesUseCase The use case responsible for fetching GO Train lines
 */
class JourneysViewModel(private val stationsUseCase: GetAllGoTrainStationsUseCase, private val linesUseCase: GetAllGOTrainLinesUseCase): ViewModel() {
    
    /**
     * Internal mutable state flow that holds the current UI state.
     * Initially set to [ViewState.Loading] when the ViewModel is created.
     */
    private val _state = MutableStateFlow<ViewState<List<GOTrainStation>>>(ViewState.Loading)
    
    /**
     * Public immutable state flow that can be observed by the UI to react to state changes.
     * Exposes the list of GO Train stations wrapped in a [ViewState].
     */
    val state = _state.asStateFlow()

    /**
     * Sealed class representing all possible events that can be triggered by the UI.
     * Each event corresponds to a specific user action or screen lifecycle event.
     */
    sealed class Event {
        /**
         * Event triggered when the screen is first loaded or becomes visible.
         */
        data object OnScreenLoad : Event()
        
        /**
         * Event triggered when the user requests a data refresh.
         */
        data object OnRefresh : Event()
    }

    /**
     * Handles UI events by delegating to appropriate business logic.
     * Currently, both [Event.OnScreenLoad] and [Event.OnRefresh] trigger station data loading.
     *
     * @param event The UI event to handle
     */
    fun handleEvent(event: Event) {
        when(event) {
            Event.OnScreenLoad, Event.OnRefresh -> {
                loadStations()
            }
        }
    }

    /**
     * Private function that handles the loading of GO Train stations.
     * Updates the UI state accordingly:
     * 1. Sets state to Loading
     * 2. Executes the use case to fetch stations
     * 3. Updates state with either Success (with stations) or Failure (with error)
     */
    private fun loadStations() {
        _state.value = ViewState.Loading
        viewModelScope.launch {
            stationsUseCase.execute(false)
                .fold(
                    onSuccess = { stations ->
                        _state.value = ViewState.RenderSuccess(stations)
                    },
                    onFailure = { error ->
                        _state.value = ViewState.RenderFailure(error)
                    }
                )
        }
    }

    /**
     * Private function that handles the loading of GO Train lines.
     * Updates the UI state accordingly:
     * 1. Sets state to Loading
     * 2. Executes the use case to fetch lines
     * 3. Updates state with either Success (with lines) or Failure (with error)
     */
    private fun loadLines() {
        _state.value = ViewState.Loading
        viewModelScope.launch {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val formattedDate = "${today.year}${today.monthNumber.toString().padStart(2, '0')}${today.dayOfMonth.toString().padStart(2, '0')}"
            linesUseCase.execute(formattedDate)
                .fold(
                    onSuccess = { lines ->
                        _state.value = ViewState.RenderFailure(Exception("This is not right state variable lol"))
                    },
                    onFailure = { error ->
                        _state.value = ViewState.RenderFailure(error)
                    }
                )
        }
    }
}