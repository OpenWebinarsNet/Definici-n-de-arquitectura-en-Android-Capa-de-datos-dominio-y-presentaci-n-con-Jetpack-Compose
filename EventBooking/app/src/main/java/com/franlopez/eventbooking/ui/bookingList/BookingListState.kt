package com.franlopez.eventbooking.ui.bookingList

import androidx.compose.runtime.Immutable
import com.franlopez.eventbooking.domain.model.BookingModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class BookingListState(
    val isLoading: Boolean = true,
    val bookings: ImmutableList<BookingModel> = persistentListOf(),
    val pendingSyncBookings: ImmutableList<BookingModel> = persistentListOf()
)