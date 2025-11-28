package com.franlopez.eventbooking.data.remote.model

data class EventDto(
    val id: String = "",
    val imgUrl: String = "",
    val title: String = "",
    val totalCapacity: Int = 0,
    val availableCapacity: Int = 0
)
