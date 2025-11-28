package com.franlopez.eventbooking.ui.createBooking

import androidx.compose.runtime.Immutable

@Immutable
data class CreateBookingState(
    val isLoading: Boolean = true,
    val assistants: Int = 1,
    val totalCapacity: Int = 0,
    val availableCapacity: Int = 0
)
