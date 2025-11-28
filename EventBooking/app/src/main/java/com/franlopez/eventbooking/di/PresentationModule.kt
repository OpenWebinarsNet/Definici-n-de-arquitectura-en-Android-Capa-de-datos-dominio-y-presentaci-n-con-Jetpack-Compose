package com.franlopez.eventbooking.di

import com.franlopez.eventbooking.ui.bookingList.BookingListViewModel
import com.franlopez.eventbooking.ui.createBooking.CreateBookingViewModel
import com.franlopez.eventbooking.ui.eventList.EventListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::EventListViewModel)
    viewModelOf(::CreateBookingViewModel)
    viewModelOf(::BookingListViewModel)
}