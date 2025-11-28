package com.franlopez.eventbooking.ui.eventList

import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.franlopez.eventbooking.R
import com.franlopez.eventbooking.domain.model.EventModel
import com.franlopez.eventbooking.ui.composable.LoadingFullScreen
import com.franlopez.eventbooking.ui.composable.TopBar
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EventListScreen(
    viewModel: EventListViewModel,
    needUpdate: Boolean,
    navigateToCreateBooking: (EventModel) -> Unit,
    navigateToBookingList: (EventModel) -> Unit
) {
    if (needUpdate) {
        viewModel.loadEvents()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val loadEventsErrorText = stringResource(R.string.event_list_load_events_error)
    val syncDataErrorText = stringResource(R.string.event_list_sync_data_error)
    val event by viewModel.events.collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(event) {
        when (event) {
            EventListEvent.LoadEventsFailure -> snackbarHostState.showSnackbar(loadEventsErrorText)
            EventListEvent.SyncDataFailure -> snackbarHostState.showSnackbar(syncDataErrorText)
            null -> Unit
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            if (state.needSync) {
                TopBar(
                    actionIcon = painterResource(R.drawable.ic_sync),
                    onActionIconClick = {
                        viewModel.syncData()
                    }
                )
            } else {
                TopBar()
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingFullScreen(modifier = Modifier.padding(innerPadding))
        } else {
            Data(
                modifier = Modifier.padding(innerPadding),
                events = state.events,
                onBookingListClick = {
                    navigateToBookingList(it)
                },
                onCreateBookingClick = {
                    navigateToCreateBooking(it)
                }
            )
        }
    }
}

@Composable
private fun Data(
    modifier: Modifier,
    events: ImmutableList<EventModel>,
    onCreateBookingClick: (EventModel) -> Unit,
    onBookingListClick: (EventModel) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            events, key = { event -> event.id }
        ) { event ->
            EventItem(
                title = event.title,
                imgUrl = event.imgUrl,
                totalCapacity = event.totalCapacity,
                availableCapacity = event.availableCapacity,
                onCreateBookingClick = {
                    onCreateBookingClick(event)
                },
                onBookingListClick = {
                    onBookingListClick(event)
                }
            )
            Spacer(Modifier
                .fillMaxWidth()
                .height(8.dp))
        }
    }
}

@Composable
private fun EventItem(
    title: String,
    imgUrl: String,
    totalCapacity: Int,
    availableCapacity: Int,
    onCreateBookingClick: () -> Unit,
    onBookingListClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.clip(shape = RoundedCornerShape(16.dp))
    ) {
        Box {
            AsyncImage(
                model = imgUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Text(
                modifier = modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                text = title.uppercase(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    shadow = Shadow(
                        color = Color.White,
                        offset = Offset(1f, 1f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(R.string.event_list_capacity, availableCapacity, totalCapacity),
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
                    onClick = { onCreateBookingClick() },
                    enabled = availableCapacity < totalCapacity
                ) {
                    Text(
                        text = stringResource(R.string.event_list_booking),
                        fontSize = 16.sp
                    )
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onBookingListClick() }
                ) {
                    Text(
                        text = stringResource(R.string.event_list_bookings),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}