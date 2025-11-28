package com.franlopez.eventbooking.data.remote.mapper

import com.franlopez.eventbooking.data.common.BookingDocumentAssistants
import com.franlopez.eventbooking.data.common.BookingDocumentEventId
import com.franlopez.eventbooking.data.common.BookingDocumentId
import com.franlopez.eventbooking.data.common.BookingDocumentOwnerName
import com.franlopez.eventbooking.data.remote.model.BookingDto
import com.franlopez.eventbooking.data.remote.model.EventDto
import com.franlopez.eventbooking.domain.model.BookingModel
import com.franlopez.eventbooking.domain.model.EventModel

fun EventDto.toModel() = EventModel(
    id = id,
    imgUrl = imgUrl,
    title = title,
    totalCapacity = totalCapacity,
    availableCapacity = availableCapacity
)

fun BookingDto.toModel() = BookingModel(
    id = id,
    ownerName = ownerName,
    assistants = assistants,
    eventId = eventId
)

fun BookingModel.toMap() = mapOf<String, Any>(
    BookingDocumentId to id,
    BookingDocumentOwnerName to ownerName,
    BookingDocumentAssistants to assistants,
    BookingDocumentEventId to eventId
)