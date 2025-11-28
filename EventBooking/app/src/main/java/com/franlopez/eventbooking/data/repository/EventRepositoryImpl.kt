package com.franlopez.eventbooking.data.repository

import com.franlopez.eventbooking.data.remote.EventRemoteDataSource
import com.franlopez.eventbooking.domain.repository.EventRepository

class EventRepositoryImpl(
    private val remote: EventRemoteDataSource
) : EventRepository {
    override suspend fun getEvents() = remote.getEvents()
}