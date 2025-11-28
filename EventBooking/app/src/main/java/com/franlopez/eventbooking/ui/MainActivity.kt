package com.franlopez.eventbooking.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.franlopez.eventbooking.ui.navigation.NavigationApp
import com.franlopez.eventbooking.ui.theme.EventBookingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventBookingTheme {
                NavigationApp()
            }
        }
    }
}