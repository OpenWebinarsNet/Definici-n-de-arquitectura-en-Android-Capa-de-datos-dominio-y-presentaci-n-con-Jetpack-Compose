package com.franlopez.eventbooking.ui.eventList

sealed interface EventListEvent {
    data object LoadEventsFailure : EventListEvent
    data object SyncDataFailure : EventListEvent
}