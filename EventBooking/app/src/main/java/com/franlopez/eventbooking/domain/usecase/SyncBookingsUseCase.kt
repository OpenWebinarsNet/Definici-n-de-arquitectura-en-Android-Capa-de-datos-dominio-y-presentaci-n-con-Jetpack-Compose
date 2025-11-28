package com.franlopez.eventbooking.domain.usecase

import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.common.eitherFailure
import com.franlopez.eventbooking.domain.model.common.eitherSuccess
import com.franlopez.eventbooking.domain.model.error.GenericError
import com.franlopez.eventbooking.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncBookingsUseCase(
    private val bookingRepository: BookingRepository
) {
    fun invoke(): Flow<Either<Boolean, GenericError>> {
        return flow {
            val syncPendingBookingsResponse = bookingRepository.syncPendingBookings()

            when (syncPendingBookingsResponse) {
                is Either.Failure -> emit(eitherFailure(GenericError.Unknown))
                is Either.Success -> {
                    val needSync = bookingRepository.thereIsNoSyncBookings()
                    emit(eitherSuccess(needSync))
                }
            }
        }
    }
}