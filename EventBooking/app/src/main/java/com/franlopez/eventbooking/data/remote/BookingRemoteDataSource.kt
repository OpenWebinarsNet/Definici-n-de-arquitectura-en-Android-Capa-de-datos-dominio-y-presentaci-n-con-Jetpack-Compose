package com.franlopez.eventbooking.data.remote

import com.franlopez.eventbooking.data.common.BookingDocumentEventId
import com.franlopez.eventbooking.data.common.BookingsCollection
import com.franlopez.eventbooking.data.common.EventsAvailableCapacity
import com.franlopez.eventbooking.data.common.EventsCollection
import com.franlopez.eventbooking.data.remote.mapper.toMap
import com.franlopez.eventbooking.data.remote.mapper.toModel
import com.franlopez.eventbooking.data.remote.model.BookingDto
import com.franlopez.eventbooking.data.remote.model.EventDto
import com.franlopez.eventbooking.domain.model.BookingModel
import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.common.eitherFailure
import com.franlopez.eventbooking.domain.model.common.eitherSuccess
import com.franlopez.eventbooking.domain.model.error.GenericError
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BookingRemoteDataSource(
    private val firestore: FirebaseFirestore
) {

    suspend fun getBookings(eventId: String): Either<List<BookingModel>, GenericError> {
        return try {
            val bookingsSnapshot = firestore.collection(BookingsCollection)
                .whereEqualTo(BookingDocumentEventId, eventId)
                .get()
                .await()
            val bookings = bookingsSnapshot.toObjects(BookingDto::class.java)
            eitherSuccess(bookings.map { it.toModel() })
        } catch (_: Exception) {
            eitherFailure(GenericError.Unknown)
        }
    }

    suspend fun createBooking(eventId: String, ownerName: String, assistants: Int): Either<Boolean, GenericError> {
        return try {
            val bookingId = firestore
                .collection(BookingsCollection)
                .document().id
            val bookingToUpload = BookingModel(
                id = bookingId,
                ownerName = ownerName,
                assistants = assistants,
                eventId = eventId
            )
            firestore.collection(BookingsCollection)
                .document(bookingId)
                .set(bookingToUpload.toMap())
                .await()

            updateEventAvailableCapacity(eventId = eventId, assistants = assistants)

            eitherSuccess(true)
        } catch (_ : Exception) {
            eitherFailure(GenericError.Unknown)
        }
    }

    suspend fun deleteBooking(eventId: String, bookingId: String, assistants: Int): Either<Boolean, GenericError> {
        return try {
            firestore.collection(BookingsCollection)
                .document(bookingId)
                .delete()
                .await()

            updateEventAvailableCapacity(eventId = eventId, assistants = -assistants)

            eitherSuccess(true)
        } catch (_ : Exception) {
            eitherFailure(GenericError.Unknown)
        }
    }

    private suspend fun updateEventAvailableCapacity(eventId: String, assistants: Int) {
        try {
            val eventSnapshot = firestore.collection(EventsCollection)
                .document(eventId)
                .get()
                .await()
            val event = eventSnapshot.toObject(EventDto::class.java)
            event?.let {
                val updateAvailableCapacity = if (assistants > 0) {
                    (event.availableCapacity + assistants).coerceAtMost(event.totalCapacity)
                } else {
                    (event.availableCapacity + assistants).coerceAtLeast(0)
                }

                firestore.collection(EventsCollection)
                    .document(eventId)
                    .update(EventsAvailableCapacity, updateAvailableCapacity)
                    .await()
            }

        } catch (_ : Exception) {
            // no-op
        }
    }
}