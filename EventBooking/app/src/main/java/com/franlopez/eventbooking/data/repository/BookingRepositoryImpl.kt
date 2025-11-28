package com.franlopez.eventbooking.data.repository

import com.franlopez.eventbooking.data.local.BookingLocalDataSource
import com.franlopez.eventbooking.data.remote.BookingRemoteDataSource
import com.franlopez.eventbooking.domain.model.BookingModel
import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.common.eitherFailure
import com.franlopez.eventbooking.domain.model.common.eitherSuccess
import com.franlopez.eventbooking.domain.model.error.GenericError
import com.franlopez.eventbooking.domain.repository.BookingRepository

class BookingRepositoryImpl(
    private val remote: BookingRemoteDataSource,
    private val local: BookingLocalDataSource
) : BookingRepository {
    override suspend fun getBookings(eventId: String): Either<Pair<List<BookingModel>, List<BookingModel>>, GenericError> {
        val bookingsResponse = remote.getBookings(eventId = eventId)
        val pendingBooking = local.getPendingBookings(eventId = eventId)
        return when (bookingsResponse) {
            is Either.Failure -> eitherFailure(GenericError.Unknown)
            is Either.Success -> eitherSuccess(
                bookingsResponse.data to pendingBooking
            )
        }
    }

    override suspend fun thereIsNoSyncBookings() =
        local.getFirstBooking().isNotEmpty()

    override suspend fun syncPendingBookings(): Either<Boolean, GenericError> {
        local.getAllPendingBookings().forEach { booking ->
            val uploadBookingResponse = remote.createBooking(
                eventId = booking.eventId,
                ownerName = booking.ownerName,
                assistants = booking.assistants
            )
            when (uploadBookingResponse) {
                is Either.Failure -> Unit
                is Either.Success -> {
                    local.deleteBooking(bookingId = booking.id)
                }
            }
        }
        return eitherSuccess(thereIsNoSyncBookings())
    }

    override suspend fun createBooking(
        eventId: String,
        ownerName: String,
        assistants: Int
    ): Boolean {
        val createBookingResponse = remote.createBooking(
            eventId = eventId,
            ownerName = ownerName,
            assistants = assistants
        )
        return when (createBookingResponse) {
            is Either.Failure -> {
                local.createBooking(
                    eventId = eventId,
                    ownerName = ownerName,
                    assistants = assistants
                )
            }

            is Either.Success -> createBookingResponse.data
        }
    }

    override suspend fun cancelBooking(
        eventId: String,
        bookingId: String,
        assistants: Int
    ): Either<Boolean, GenericError> {
        val isNoSyncBooking = local.getPendingBooking(bookingId = bookingId)
        return if (isNoSyncBooking != null) {
            eitherSuccess(local.deleteBooking(bookingId = bookingId))
        } else {
            remote.deleteBooking(
                eventId = eventId,
                bookingId = bookingId,
                assistants = assistants
            )
        }
    }

    override suspend fun syncBooking(bookingId: String): Either<Boolean, GenericError> {
        val pendingBooking = local.getPendingBooking(bookingId = bookingId)
        return pendingBooking?.let {
            val uploadBookingResponse = remote.createBooking(
                eventId = pendingBooking.eventId,
                ownerName = pendingBooking.ownerName,
                assistants = pendingBooking.assistants
            )
            when (uploadBookingResponse) {
                is Either.Failure -> Unit
                is Either.Success -> {
                    local.deleteBooking(bookingId = pendingBooking.id)
                }
            }
            uploadBookingResponse
        } ?: run {
            eitherFailure(GenericError.Unknown)
        }
    }

}