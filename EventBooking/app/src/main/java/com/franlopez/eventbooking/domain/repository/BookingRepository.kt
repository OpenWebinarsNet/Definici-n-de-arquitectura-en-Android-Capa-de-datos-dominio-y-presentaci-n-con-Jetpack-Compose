package com.franlopez.eventbooking.domain.repository

import com.franlopez.eventbooking.domain.model.BookingModel
import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.error.GenericError

interface BookingRepository {
    suspend fun getBookings(eventId: String): Either<Pair<List<BookingModel>, List<BookingModel>>, GenericError>
    suspend fun thereIsNoSyncBookings(): Boolean
    suspend fun syncPendingBookings(): Either<Boolean, GenericError>
    suspend fun createBooking(eventId: String, ownerName: String, assistants: Int): Boolean
    suspend fun cancelBooking(eventId: String, bookingId: String, assistants: Int): Either<Boolean, GenericError>
    suspend fun syncBooking(bookingId: String): Either<Boolean, GenericError>
}