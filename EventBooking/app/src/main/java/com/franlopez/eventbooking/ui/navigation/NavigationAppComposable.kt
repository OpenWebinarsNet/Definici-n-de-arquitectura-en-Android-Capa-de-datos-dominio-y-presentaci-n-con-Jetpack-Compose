package com.franlopez.eventbooking.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.franlopez.eventbooking.ui.bookingList.BookingListScreen
import com.franlopez.eventbooking.ui.createBooking.CreateBookingScreen
import com.franlopez.eventbooking.ui.eventList.EventListScreen
import com.franlopez.eventbooking.ui.navigation.Destination.CreateBooking
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.EventList) {
        createEventListScreen(navController = navController)

        createCreateBookingScreen(navController = navController)

        createBookingListScreen(navController = navController)
    }
}

private fun NavGraphBuilder.createEventListScreen(
    navController: NavController
) = composable<Destination.EventList> { backStackEntry ->
    val needUpdate = backStackEntry.savedStateHandle.get<Boolean>(NeedUpdateArgument) != false
    backStackEntry.savedStateHandle[NeedUpdateArgument] = false
    EventListScreen(
        viewModel = koinViewModel(),
        needUpdate = needUpdate,
        navigateToBookingList = { event ->
            navController.navigate(Destination.BookingList(eventId = event.id))
        },
        navigateToCreateBooking = { event ->
            navController.navigate(
                Destination.CreateBooking(
                    eventId = event.id,
                    title = event.title,
                    imgUrl = event.imgUrl,
                    totalCapacity = event.totalCapacity,
                    availableCapacity = event.availableCapacity
                )
            )
        }
    )
}

private fun NavGraphBuilder.createCreateBookingScreen(navController: NavHostController) =
    composable<CreateBooking> { backStackEntry ->
        val createBookingRoute = backStackEntry.toRoute<CreateBooking>()
        CreateBookingScreen(
            viewModel = koinViewModel(),
            eventId = createBookingRoute.eventId,
            title = createBookingRoute.title,
            imgUrl = createBookingRoute.imgUrl,
            totalCapacity = createBookingRoute.totalCapacity,
            availableCapacity = createBookingRoute.availableCapacity,
            bookingCreateAction = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    NeedUpdateArgument,
                    true
                )
                navController.navigateUp()
            },
            onBackClick = {
                navController.navigateUp()
            }
        )
    }

private fun NavGraphBuilder.createBookingListScreen(navController: NavHostController) =
    composable<Destination.BookingList> { backStackEntry ->
        val bookingListRoute = backStackEntry.toRoute<Destination.BookingList>()
        BookingListScreen(
            viewModel = koinViewModel(),
            eventId = bookingListRoute.eventId,
            navigateBack = { needUpdate ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    NeedUpdateArgument,
                    true
                )
                navController.navigateUp()
            }
        )
    }

const val NeedUpdateArgument = "needUpdate"