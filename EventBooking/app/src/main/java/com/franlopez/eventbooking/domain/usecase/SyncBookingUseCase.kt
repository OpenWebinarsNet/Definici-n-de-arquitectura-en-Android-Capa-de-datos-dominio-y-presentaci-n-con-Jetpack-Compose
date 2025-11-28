package com.franlopez.eventbooking.domain.usecase

import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.error.GenericError
import com.franlopez.eventbooking.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncBookingUseCase(
    private val bookingRepository: BookingRepository
) {
    fun invoke(bookingId: String): Flow<Either<Boolean, GenericError>> {
        return flow {
            val syncBookingResponse = bookingRepository.syncBooking(bookingId = bookingId)
            emit(syncBookingResponse)
        }
    }
}