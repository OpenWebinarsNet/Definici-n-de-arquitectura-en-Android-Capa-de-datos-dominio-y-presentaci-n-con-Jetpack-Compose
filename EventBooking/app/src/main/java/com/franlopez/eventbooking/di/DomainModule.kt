package com.franlopez.eventbooking.di

import com.franlopez.eventbooking.domain.usecase.CancelBookingUseCase
import com.franlopez.eventbooking.domain.usecase.CreateBookingUseCase
import com.franlopez.eventbooking.domain.usecase.GetBookingsUseCase
import com.franlopez.eventbooking.domain.usecase.GetEventsUseCase
import com.franlopez.eventbooking.domain.usecase.SyncBookingUseCase
import com.franlopez.eventbooking.domain.usecase.SyncBookingsUseCase
import org.koin.dsl.module

val domainModule = module {

    single { CancelBookingUseCase(get()) }
    single { CreateBookingUseCase(get()) }
    single { GetBookingsUseCase(get()) }
    single { GetEventsUseCase(get(), get()) }
    single { SyncBookingsUseCase(get()) }
    single { SyncBookingUseCase(get()) }

}