package com.franlopez.eventbooking.domain.usecase

import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.error.GenericError
import com.franlopez.eventbooking.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CancelBookingUseCase(
    private val bookingRepository: BookingRepository
) {
    fun invoke(eventId: String, bookingId: String, assistants: Int): Flow<Either<Boolean, GenericError>> {
        return flow {
            val cancelBookingResponse = bookingRepository.cancelBooking(
                eventId = eventId,
                bookingId = bookingId,
                assistants = assistants
            )
            emit(cancelBookingResponse)
        }
    }
}