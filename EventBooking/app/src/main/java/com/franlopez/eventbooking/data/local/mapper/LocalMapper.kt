package com.franlopez.eventbooking.data.local.mapper

import com.franlopez.eventbooking.data.local.entity.BookingEntity
import com.franlopez.eventbooking.domain.model.BookingModel

fun BookingEntity.toModel() = BookingModel(
    id = id.toString(),
    ownerName = ownerName,
    assistants = assistants,
    eventId = eventId
)

fun BookingModel.toEntity() = BookingEntity(
    id = try { id.toLong() } catch (_: Exception) { 0L },
    ownerName = ownerName,
    assistants = assistants,
    eventId = eventId
)