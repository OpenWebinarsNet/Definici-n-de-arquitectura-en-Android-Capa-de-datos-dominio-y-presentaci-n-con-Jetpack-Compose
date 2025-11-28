package com.franlopez.eventbooking.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.franlopez.eventbooking.data.local.entity.BookingEntity

@Dao
interface BookingDao {

    @Query("SELECT * FROM booking")
    suspend fun getAll(): List<BookingEntity>

    @Query("SELECT * FROM booking WHERE event_id == :eventId")
    suspend fun findByEventId(eventId: String): List<BookingEntity>

    @Query("SELECT * FROM booking WHERE id == :bookingId")
    suspend fun findById(bookingId: String): BookingEntity?

    @Query("SELECT * FROM booking LIMIT 1")
    suspend fun findFirstBooking(): List<BookingEntity>

    @Insert
    suspend fun insertAll(vararg bookings: BookingEntity)

    @Delete
    suspend fun delete(booking: BookingEntity)
}