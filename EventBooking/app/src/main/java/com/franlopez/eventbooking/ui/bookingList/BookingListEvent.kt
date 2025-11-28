package com.franlopez.eventbooking.ui.bookingList

sealed interface BookingListEvent {
    data object LoadBookingsFailure : BookingListEvent
    data object CancelBookingFailure : BookingListEvent
    data object SyncBookingFailure : BookingListEvent
}