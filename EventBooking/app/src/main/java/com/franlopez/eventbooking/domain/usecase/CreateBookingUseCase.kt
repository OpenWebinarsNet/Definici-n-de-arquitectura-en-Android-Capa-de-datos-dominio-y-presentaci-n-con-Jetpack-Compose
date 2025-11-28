package com.franlopez.eventbooking.domain.usecase

import com.franlopez.eventbooking.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CreateBookingUseCase(
    private val bookingRepository: BookingRepository
) {
    fun invoke(eventId: String, ownerName: String, assistants: Int): Flow<Boolean> {
        return flow {
            val createBookingResponse = bookingRepository.createBooking(
                eventId = eventId,
                ownerName = ownerName,
                assistants = assistants
            )
            emit(createBookingResponse)
        }
    }
}