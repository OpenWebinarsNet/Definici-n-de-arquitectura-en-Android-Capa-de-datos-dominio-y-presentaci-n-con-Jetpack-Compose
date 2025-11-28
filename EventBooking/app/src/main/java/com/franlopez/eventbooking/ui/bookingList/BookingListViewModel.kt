package com.franlopez.eventbooking.ui.bookingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franlopez.eventbooking.domain.model.BookingModel
import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.usecase.CancelBookingUseCase
import com.franlopez.eventbooking.domain.usecase.GetBookingsUseCase
import com.franlopez.eventbooking.domain.usecase.SyncBookingUseCase
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

class BookingListViewModel(
    private val getBookingsUseCase: GetBookingsUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase,
    private val syncBookingUseCase: SyncBookingUseCase
): ViewModel() {
    var needUpdate = false

    private val mutableState = MutableStateFlow(BookingListState())
    val state: StateFlow<BookingListState> = mutableState.asStateFlow()

    private val mutableEvents = Channel<BookingListEvent>()
    val events = mutableEvents.receiveAsFlow()

    fun loadBookings(eventId: String) {
        getBookingsUseCase.invoke(eventId = eventId)
            .flowOn(Dispatchers.IO)
            .onStart { mutableState.update { it.copy(isLoading = true) } }
            .onEach { response ->
                when (response) {
                    is Either.Failure -> {
                        mutableEvents.send(BookingListEvent.LoadBookingsFailure)
                    }

                    is Either.Success -> {
                        mutableState.update {
                            it.copy(
                                isLoading = false,
                                bookings = response.data.bookings.toImmutableList(),
                                pendingSyncBookings = response.data.pendingSyncBookings.toImmutableList()
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun cancelBooking(eventId: String, booking: BookingModel) {
        cancelBookingUseCase
            .invoke(
                eventId = booking.eventId,
                bookingId = booking.id,
                assistants = booking.assistants
            )
            .flowOn(Dispatchers.IO)
            .onStart {
                mutableState.update {
                    it.copy(isLoading = true)
                }
            }
            .onEach { response ->
                when (response) {
                    is Either.Failure -> {
                        mutableEvents.send(BookingListEvent.CancelBookingFailure)
                    }
                    is Either.Success -> {
                        needUpdate = true
                        loadBookings(eventId = eventId)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun syncBooking(eventId: String, booking: BookingModel) {
        syncBookingUseCase
            .invoke(bookingId = booking.id)
            .flowOn(Dispatchers.IO)
            .onStart {
                mutableState.update {
                    it.copy(isLoading = true)
                }
            }
            .onEach { response ->
                when (response) {
                    is Either.Failure -> {
                        mutableEvents.send(BookingListEvent.SyncBookingFailure)
                    }
                    is Either.Success -> {
                        needUpdate = true
                        loadBookings(eventId = eventId)
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}