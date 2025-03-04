package com.p2.apps.mygotraintracker.presentation.feature.trains

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p2.apps.mygotraintracker.domain.model.TrainTrip
import com.p2.apps.mygotraintracker.domain.usecase.GetScheduleBetweenStopsUseCase
import com.p2.apps.mygotraintracker.presentation.utils.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TrainScheduleViewModel(
    private val fromStationCode: String,
    private val toStationCode: String,
    private val scheduleUseCase: GetScheduleBetweenStopsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<List<TrainTrip>>>(ViewState.Loading)
    val state = _state.asStateFlow()

    init {
        loadSchedule()
    }

    private fun loadSchedule() {
        viewModelScope.launch {
            _state.value = ViewState.Loading
            
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val formattedDate = "${today.year}${today.monthNumber.toString().padStart(2, '0')}${today.dayOfMonth.toString().padStart(2, '0')}"
            val currentTime = "${today.hour.toString().padStart(2, '0')}${today.minute.toString().padStart(2, '0')}"
            
            scheduleUseCase.execute(
                GetScheduleBetweenStopsUseCase.Params(
                    date = formattedDate,
                    fromStopCode = fromStationCode,
                    toStopCode = toStationCode,
                    startTime = currentTime,
                    maxJourney = 10
                )
            ).fold(
                onSuccess = { trainTrips ->
                    _state.value = ViewState.RenderSuccess(trainTrips)
                },
                onFailure = { error ->
                    _state.value = ViewState.RenderFailure(error)
                }
            )
        }
    }
    
    fun refreshSchedule() {
        loadSchedule()
    }
} 