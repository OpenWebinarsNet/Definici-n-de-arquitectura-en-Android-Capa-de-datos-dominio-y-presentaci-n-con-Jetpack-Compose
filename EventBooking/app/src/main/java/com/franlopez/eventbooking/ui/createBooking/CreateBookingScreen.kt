package com.franlopez.eventbooking.ui.createBooking

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.franlopez.eventbooking.R
import com.franlopez.eventbooking.ui.composable.TopBar

@Composable
fun CreateBookingScreen(
    viewModel: CreateBookingViewModel,
    eventId: String,
    title: String,
    imgUrl: String,
    totalCapacity: Int,
    availableCapacity: Int,
    bookingCreateAction: () -> Unit,
    onBackClick: () -> Unit
) {
    viewModel.updateState(totalCapacity = totalCapacity, availableCapacity = availableCapacity)

    val snackbarHostState = remember { SnackbarHostState() }
    val emptyAuthorErrorText = stringResource(R.string.create_booking_empty_author)
    val createBookingErrorText = stringResource(R.string.create_booking_create_booking_error)
    val event by viewModel.events.collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(event) {
        when (event) {
            CreateBookingEvent.Failure -> snackbarHostState.showSnackbar(createBookingErrorText)
            CreateBookingEvent.NavigateBackWithResult -> bookingCreateAction()
            CreateBookingEvent.EmptyAuthor -> snackbarHostState.showSnackbar(emptyAuthorErrorText)
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
                title = stringResource(R.string.create_booking_title),
                needBackIcon = true,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        CreateBookingContent(
            modifier = Modifier.padding(innerPadding),
            imgUrl = imgUrl,
            title = title,
            assistants = state.assistants,
            onIncreaseClick = { viewModel.increaseAssistant() },
            onDecreaseClick = { viewModel.decreaseAssistant() },
            onCreateBooking = { ownerName ->
                viewModel.createBooking(eventId = eventId, ownerName = ownerName)
            }
        )
    }
}

@Composable
private fun CreateBookingContent(
    modifier: Modifier,
    imgUrl: String,
    title: String,
    assistants: Int,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    onCreateBooking: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            model = imgUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            var ownerName by remember { mutableStateOf("") }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.create_booking_owner_name_hint),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    )
                },
                value = ownerName,
                onValueChange = { ownerName = it }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(48.dp)
                        .border(width = 2.dp, shape = CircleShape, color = Color.Black)
                        .clip(CircleShape)
                        .clickable {
                            onDecreaseClick()
                        },
                    painter = painterResource(R.drawable.ic_reduce),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "$assistants",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Image(
                    modifier = Modifier.size(48.dp)
                        .border(width = 2.dp, shape = CircleShape, color = Color.Black)
                        .clip(CircleShape)
                        .clickable {
                            onIncreaseClick()
                        },
                    painter = painterResource(R.drawable.ic_increase),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onCreateBooking(ownerName)
                }
            ) {
                Text(
                    text = stringResource(R.string.create_booking_accept),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateBookingContentPreview() {
    CreateBookingContent(
        modifier = Modifier.fillMaxSize(),
        imgUrl = "",
        title = "Title",
        assistants = 2,
        onIncreaseClick = {},
        onDecreaseClick = {},
        onCreateBooking = {}
    )
}