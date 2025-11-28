package com.franlopez.eventbooking.ui.eventList

import androidx.compose.runtime.Immutable
import com.franlopez.eventbooking.domain.model.EventModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class EventListState(
    val isLoading: Boolean = true,
    val events: ImmutableList<EventModel> = persistentListOf(),
    val needSync: Boolean = false
)
