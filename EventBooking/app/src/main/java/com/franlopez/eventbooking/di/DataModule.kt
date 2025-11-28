package com.franlopez.eventbooking.di

import androidx.room.Room
import com.franlopez.eventbooking.data.local.BookingLocalDataSource
import com.franlopez.eventbooking.data.local.dao.BookingDao
import com.franlopez.eventbooking.data.local.database.AppDatabase
import com.franlopez.eventbooking.data.local.database.DatabaseName
import com.franlopez.eventbooking.data.remote.BookingRemoteDataSource
import com.franlopez.eventbooking.data.remote.EventRemoteDataSource
import com.franlopez.eventbooking.data.repository.BookingRepositoryImpl
import com.franlopez.eventbooking.data.repository.EventRepositoryImpl
import com.franlopez.eventbooking.domain.repository.BookingRepository
import com.franlopez.eventbooking.domain.repository.EventRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(
            context = androidApplication(),
            AppDatabase::class.java,
            DatabaseName
        ).build()
    }
    single<BookingDao> {
        val database = get<AppDatabase>()
        database.bookingDao()
    }

    single { BookingLocalDataSource(get()) }

    single<FirebaseFirestore> { Firebase.firestore }
    single { EventRemoteDataSource(get()) }
    single { BookingRemoteDataSource(get()) }

    single<EventRepository> { EventRepositoryImpl(get()) }
    single<BookingRepository> { BookingRepositoryImpl(get(), get()) }
}