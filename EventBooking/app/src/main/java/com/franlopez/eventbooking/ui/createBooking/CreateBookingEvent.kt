package com.franlopez.eventbooking.ui.createBooking

sealed interface CreateBookingEvent {
    data object NavigateBackWithResult : CreateBookingEvent
    data object EmptyAuthor : CreateBookingEvent
    data object Failure : CreateBookingEvent
}