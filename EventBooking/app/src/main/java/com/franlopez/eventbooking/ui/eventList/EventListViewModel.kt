package com.franlopez.eventbooking.ui.eventList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.usecase.GetEventsUseCase
import com.franlopez.eventbooking.domain.usecase.SyncBookingsUseCase
import kotlinx.collections.immutable.toImmutableList
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

class EventListViewModel(
    private val getEventsUseCase: GetEventsUseCase,
    private val syncBookingsUseCase: SyncBookingsUseCase
) : ViewModel() {
    private val mutableState =  MutableStateFlow(EventListState())
    val state: StateFlow<EventListState> = mutableState.asStateFlow()

    private val mutableEvents = Channel<EventListEvent>()
    val events = mutableEvents.receiveAsFlow()

    fun loadEvents() {
        getEventsUseCase.invoke()
            .flowOn(Dispatchers.IO)
            .onStart { mutableState.update { it.copy(isLoading = true) } }
            .onEach { response ->
                when (response) {
                    is Either.Failure -> {
                        mutableEvents.send(EventListEvent.LoadEventsFailure)
                    }
                    is Either.Success -> {
                        mutableState.update {
                            it.copy(
                                isLoading = false,
                                events = response.data.items.toImmutableList(),
                                needSync = response.data.needSync
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun syncData() {
        syncBookingsUseCase.invoke()
            .flowOn(Dispatchers.IO)
            .onStart { mutableState.update { it.copy(isLoading = true) } }
            .onEach { response ->
                when (response) {
                    is Either.Failure -> {
                        mutableEvents.send(EventListEvent.SyncDataFailure)
                    }

                    is Either.Success -> {
                        mutableState.update {
                            it.copy(
                                isLoading = false,
                                needSync = response.data
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }
}