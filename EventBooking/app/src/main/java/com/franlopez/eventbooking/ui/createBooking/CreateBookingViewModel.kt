package com.franlopez.eventbooking.ui.createBooking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franlopez.eventbooking.domain.usecase.CreateBookingUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateBookingViewModel(
    private val createBookingUseCase: CreateBookingUseCase
) : ViewModel() {
    private val mutableState = MutableStateFlow(CreateBookingState())
    val state: StateFlow<CreateBookingState> = mutableState.asStateFlow()

    private val mutableEvents = Channel<CreateBookingEvent>()
    val events = mutableEvents.receiveAsFlow()

    fun updateState(totalCapacity: Int, availableCapacity: Int) {
        mutableState.update {
            it.copy(
                isLoading = false,
                totalCapacity = totalCapacity,
                availableCapacity = availableCapacity
            )
        }
    }

    fun increaseAssistant() {
        mutableState.update { currentState ->
            var currentAssistants = currentState.assistants
            val pendingCapacity = currentState.totalCapacity - currentState.availableCapacity
            if (currentAssistants < pendingCapacity) {
                currentAssistants++
            }
            currentState.copy(
                assistants = currentAssistants
            )
        }
    }

    fun decreaseAssistant() {
        mutableState.update { currentState ->
            var currentAssistants = currentState.assistants
            if (currentAssistants > 1) {
                currentAssistants--
            }
            currentState.copy(
                assistants = currentAssistants
            )
        }
    }

    fun createBooking(eventId: String, ownerName: String) {
        if (ownerName.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                mutableEvents.send(CreateBookingEvent.EmptyAuthor)
            }
        } else {
            createBookingUseCase.invoke(
                eventId = eventId,
                ownerName = ownerName,
                assistants = state.value.assistants
            ).flowOn(Dispatchers.IO)
                .onStart {
                    mutableState.update { currentState ->
                        currentState.copy(isLoading = true)
                    }
                }.onEach { response ->
                    if (response) {
                        mutableEvents.send(CreateBookingEvent.NavigateBackWithResult)
                    } else {
                        mutableEvents.send(CreateBookingEvent.Failure)
                    }
                }.launchIn(viewModelScope)
        }
    }
}