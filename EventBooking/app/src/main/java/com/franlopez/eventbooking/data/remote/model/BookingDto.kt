package com.franlopez.eventbooking.data.remote.model

data class BookingDto(
    val id: String = "",
    val ownerName: String = "",
    val assistants: Int = 0,
    val eventId: String = ""
)
