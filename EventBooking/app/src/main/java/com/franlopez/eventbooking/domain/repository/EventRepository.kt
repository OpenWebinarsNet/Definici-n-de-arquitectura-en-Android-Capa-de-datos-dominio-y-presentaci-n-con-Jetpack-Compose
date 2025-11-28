package com.franlopez.eventbooking.domain.repository

import com.franlopez.eventbooking.domain.model.EventModel
import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.error.GenericError

interface EventRepository {
    suspend fun getEvents(): Either<List<EventModel>, GenericError>
}