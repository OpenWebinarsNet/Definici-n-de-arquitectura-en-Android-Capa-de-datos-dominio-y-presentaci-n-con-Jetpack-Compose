package com.franlopez.eventbooking.ui.bookingList

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.franlopez.eventbooking.R
import com.franlopez.eventbooking.domain.model.BookingModel
import com.franlopez.eventbooking.ui.composable.LoadingFullScreen
import com.franlopez.eventbooking.ui.composable.TopBar
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BookingListScreen(
    viewModel: BookingListViewModel,
    eventId: String,
    navigateBack: (Boolean) -> Unit
) {
    viewModel.loadBookings(eventId = eventId)

    BackHandler {
        navigateBack(viewModel.needUpdate)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val cancelBookingFailureErrorText = stringResource(R.string.booking_list_cancel_booking_error)
    val loadBookingsFailureErrorText = stringResource(R.string.booking_list_load_bookings_error)
    val syncBookingFailureErrorText = stringResource(R.string.booking_list_sync_booking_error)
    val event by viewModel.events.collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(event) {
        when (event) {
            BookingListEvent.CancelBookingFailure -> snackbarHostState.showSnackbar(cancelBookingFailureErrorText)
            BookingListEvent.LoadBookingsFailure -> snackbarHostState.showSnackbar(loadBookingsFailureErrorText)
            BookingListEvent.SyncBookingFailure -> snackbarHostState.showSnackbar(syncBookingFailureErrorText)
            null -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopBar(
                title = stringResource(R.string.booking_list_title),
                needBackIcon = true,
                onBackClick = { navigateBack(viewModel.needUpdate) }
            )
        }
    ) { innerPadding ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        if (state.isLoading) {
            LoadingFullScreen(modifier = Modifier.padding(innerPadding))
        } else {
            Data(
                modifier = Modifier.padding(innerPadding),
                bookings = state.bookings,
                pendingBookings = state.pendingSyncBookings,
                onCancelClick = { booking ->
                    viewModel.cancelBooking(
                        eventId = eventId,
                        booking = booking
                    )
                },
                onSyncClick = { booking ->
                    viewModel.syncBooking(
                        eventId = eventId,
                        booking = booking
                    )
                }
            )
        }
    }
}

@Composable
private fun Data(
    modifier: Modifier,
    bookings: ImmutableList<BookingModel>,
    pendingBookings: ImmutableList<BookingModel>,
    onCancelClick: (BookingModel) -> Unit,
    onSyncClick: (BookingModel) -> Unit,
) {
    if (bookings.isEmpty() && pendingBookings.isEmpty()) {
        Empty()
    } else {
        LazyColumn(
            modifier = modifier, contentPadding = PaddingValues(16.dp)
        ) {
            items(bookings, key = { booking -> booking.id }) { booking ->
                BookingItem(
                    modifier = Modifier.padding(bottom = 8.dp),
                    ownerName = booking.ownerName,
                    assistants = booking.assistants,
                    needSync = false,
                    onCancelClick = { onCancelClick(booking) },
                    onSyncClick = { onSyncClick(booking) }
                )
            }
            items(pendingBookings, key = { event -> event.id }) { booking ->
                BookingItem(
                    modifier = Modifier.padding(bottom = 8.dp),
                    ownerName = booking.ownerName,
                    assistants = booking.assistants,
                    needSync = true,
                    onCancelClick = { onCancelClick(booking) },
                    onSyncClick = { onSyncClick(booking) }
                )
            }
        }
    }
}

@Composable
private fun BookingItem(
    ownerName: String,
    assistants: Int,
    needSync: Boolean,
    onCancelClick: () -> Unit,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clip(shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.booking_list_owner_name, ownerName),
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.booking_list_assistants, assistants),
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )
            Row(
               horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onCancelClick()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.booking_list_cancel),
                        fontSize = 16.sp
                    )
                }
                if (needSync) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onSyncClick()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.booking_list_sync),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Empty() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            text = stringResource(R.string.booking_list_empty_bookings),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookingItemPreview() {
    Column {
        BookingItem(
            ownerName = "Owner name",
            assistants = 2,
            needSync = true,
            onSyncClick = {},
            onCancelClick = {}
        )
        Spacer(modifier = Modifier.height(8.dp))
        BookingItem(
            ownerName = "Owner name",
            assistants = 2,
            needSync = false,
            onSyncClick = {},
            onCancelClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    Empty()
}
