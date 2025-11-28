package com.franlopez.eventbooking.data.local

import com.franlopez.eventbooking.data.local.dao.BookingDao
import com.franlopez.eventbooking.data.local.entity.BookingEntity
import com.franlopez.eventbooking.data.local.mapper.toModel
import com.franlopez.eventbooking.domain.model.BookingModel

class BookingLocalDataSource(
    private val bookingDao: BookingDao
) {
    suspend fun getFirstBooking() =
        bookingDao.findFirstBooking().map { it.toModel() }

    suspend fun getAllPendingBookings() =
        bookingDao.getAll().map { it.toModel() }

    suspend fun deleteBooking(bookingId: String): Boolean {
        val booking = bookingDao.findById(bookingId = bookingId)
        booking?.let {
            bookingDao.delete(it)
        }
        return booking != null
    }

    suspend fun createBooking(eventId: String, ownerName: String, assistants: Int): Boolean {
        bookingDao.insertAll(
            BookingEntity(
                eventId = eventId,
                ownerName = ownerName,
                assistants = assistants
            )
        )
        return true
    }

    suspend fun getPendingBooking(bookingId: String): BookingModel? =
        bookingDao.findById(bookingId = bookingId)?.toModel()

    suspend fun getPendingBookings(eventId: String) =
        bookingDao.findByEventId(eventId = eventId).map { it.toModel() }
}