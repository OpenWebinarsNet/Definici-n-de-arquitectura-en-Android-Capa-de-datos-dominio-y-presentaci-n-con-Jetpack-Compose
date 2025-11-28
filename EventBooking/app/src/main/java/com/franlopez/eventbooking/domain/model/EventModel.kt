package com.franlopez.eventbooking.domain.model

data class EventModel(
    val id: String,
    val imgUrl: String,
    val title: String,
    val totalCapacity: Int,
    val availableCapacity: Int
)