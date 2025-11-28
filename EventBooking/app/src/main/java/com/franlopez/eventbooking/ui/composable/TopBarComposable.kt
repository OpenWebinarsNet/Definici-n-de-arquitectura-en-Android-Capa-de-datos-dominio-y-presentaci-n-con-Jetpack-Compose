package com.franlopez.eventbooking.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.franlopez.eventbooking.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = stringResource(R.string.app_name),
    needBackIcon: Boolean = false,
    onBackClick: () -> Unit = {},
    actionIcon: Painter? = null,
    onActionIconClick: () -> Unit = {}
) {
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehaviour,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            if (needBackIcon) {
                Image(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp)
                        .clickable {
                            onBackClick()
                        },
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = {
            Text(
                title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        },
        actions = {
            actionIcon?.let {
                Image(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp)
                        .clickable {
                            onActionIconClick()
                        },
                    painter = actionIcon,
                    contentDescription = null
                )
            }
        }
    )
}