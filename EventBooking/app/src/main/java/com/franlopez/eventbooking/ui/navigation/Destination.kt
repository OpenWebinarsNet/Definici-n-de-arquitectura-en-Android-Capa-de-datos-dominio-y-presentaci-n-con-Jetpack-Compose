package com.franlopez.eventbooking.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destination {

    @Serializable
    data object EventList : Destination()

    @Serializable
    data class CreateBooking(
        val eventId: String,
        val title: String,
        val imgUrl: String,
        val totalCapacity: Int,
        val availableCapacity: Int
    ) : Destination()

    @Serializable
    data class BookingList(val eventId: String) : Destination()
}