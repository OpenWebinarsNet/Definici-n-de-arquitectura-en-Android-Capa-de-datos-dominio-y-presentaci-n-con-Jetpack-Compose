package com.franlopez.eventbooking.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.franlopez.eventbooking.data.local.dao.BookingDao
import com.franlopez.eventbooking.data.local.entity.BookingEntity

@Database(entities = [BookingEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
}

const val DatabaseName = "database"