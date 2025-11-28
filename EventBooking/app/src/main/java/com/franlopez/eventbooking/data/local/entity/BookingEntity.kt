package com.franlopez.eventbooking.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "booking")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "owner_name") val ownerName: String,
    val assistants: Int,
    @ColumnInfo(name = "event_id") val eventId: String
)