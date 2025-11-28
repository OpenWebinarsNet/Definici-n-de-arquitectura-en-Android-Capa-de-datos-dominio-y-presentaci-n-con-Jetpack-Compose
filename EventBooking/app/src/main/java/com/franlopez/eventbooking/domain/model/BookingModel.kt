package com.franlopez.eventbooking.domain.model

data class BookingModel(
    val id: String,
    val ownerName: String,
    val assistants: Int,
    val eventId: String
)