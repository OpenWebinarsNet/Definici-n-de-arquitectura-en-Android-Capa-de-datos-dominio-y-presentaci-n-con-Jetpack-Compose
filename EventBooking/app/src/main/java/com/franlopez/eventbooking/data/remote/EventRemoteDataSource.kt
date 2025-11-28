package com.franlopez.eventbooking.data.remote

import com.franlopez.eventbooking.data.common.EventsCollection
import com.franlopez.eventbooking.data.remote.mapper.toModel
import com.franlopez.eventbooking.data.remote.model.EventDto
import com.franlopez.eventbooking.domain.model.EventModel
import com.franlopez.eventbooking.domain.model.common.Either
import com.franlopez.eventbooking.domain.model.common.eitherFailure
import com.franlopez.eventbooking.domain.model.common.eitherSuccess
import com.franlopez.eventbooking.domain.model.error.GenericError
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun getEvents(): Either<List<EventModel>, GenericError> {
        return try {
            val eventsSnapshot = firestore.collection(EventsCollection)
                .get()
                .await()
            val events = eventsSnapshot.toObjects(EventDto::class.java)
            eitherSuccess(events.map { it.toModel() })
        } catch (_: Exception) {
            eitherFailure(GenericError.Unknown)
        }
    }
}