package org.tiffinservice.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(fontFamily: FontFamily? = null) {
    TopAppBar(
        title = {
            Text(
                "TiffinCo",
                style = TextStyle(
                    fontFamily = fontFamily ?: FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        },
        navigationIcon = {
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Logo",
                tint = Color(0xFFF48C25),
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        },
        actions = {
            IconButton(onClick = { /* handle profile click */ }) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Profile"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFF8F0)
        )
    )
}
