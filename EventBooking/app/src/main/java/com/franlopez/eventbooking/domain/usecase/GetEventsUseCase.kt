package com.franlopez.eventbooking.domain.usecase

import com.franlopez.eventbooking.domain.model.EventModel
import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.common.eitherFailure
import com.franlopez.eventbooking.domain.model.common.eitherSuccess
import com.franlopez.eventbooking.domain.model.error.GenericError
import com.franlopez.eventbooking.domain.repository.BookingRepository
import com.franlopez.eventbooking.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetEventsUseCase(
    private val eventRepository: EventRepository,
    private val bookingRepository: BookingRepository
) {
    fun invoke(): Flow<Either<Output, GenericError>> {
        return flow {
            val eventsResponse = eventRepository.getEvents()
            val needSync = bookingRepository.thereIsNoSyncBookings()

            emit(
                when (eventsResponse) {
                    is Either.Failure -> eitherFailure(GenericError.Unknown)
                    is Either.Success -> eitherSuccess(
                        Output(
                            items = eventsResponse.data,
                            needSync = needSync
                        )
                    )
                }
            )
        }
    }

    data class Output(
        val items: List<EventModel>,
        val needSync: Boolean
    )
}