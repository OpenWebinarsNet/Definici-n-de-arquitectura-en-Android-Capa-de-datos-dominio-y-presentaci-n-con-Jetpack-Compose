package com.franlopez.eventbooking.domain.usecase

import com.franlopez.eventbooking.domain.model.BookingModel
import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.common.eitherFailure
import com.franlopez.eventbooking.domain.model.common.eitherSuccess
import com.franlopez.eventbooking.domain.model.error.GenericError
import com.franlopez.eventbooking.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetBookingsUseCase(
    private val bookingRepository: BookingRepository
) {
    fun invoke(eventId: String): Flow<Either<Output, GenericError>> {
        return flow {
            val bookingsResponse = bookingRepository.getBookings(eventId = eventId)

            when (bookingsResponse) {
                is Either.Failure -> emit(eitherFailure(GenericError.Unknown))
                is Either.Success -> bookingsResponse.data.let { (remoteBookings, pendingSyncBookings) ->
                    emit(
                        eitherSuccess(
                            Output(
                                bookings = remoteBookings,
                                pendingSyncBookings = pendingSyncBookings
                            )
                        )
                    )
                }
            }
        }
    }

    data class Output(
        val bookings: List<BookingModel>,
        val pendingSyncBookings: List<BookingModel>
    )
}