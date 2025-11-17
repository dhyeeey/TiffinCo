package org.tiffinservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

private val Primary = Color(0xFFE87A5D)
private val BackgroundLight = Color(0xFFFDF6F0)
private val CardLight = Color.White
private val TextSecondary = Color(0xFF9C7349)
private val IconBgLight = Color(0xFFFBEAE4)

@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
//            ProfileTopBar()
        },
        bottomBar = {
//            ProfileBottomNavBar()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header
            ProfileHeader()

            Spacer(Modifier.height(24.dp))

            // Account Section
            InfoSection(
                title = "MY ACCOUNT",
                items = listOf(
                    InfoItem(Icons.Default.Person, "Personal Information"),
                    InfoItem(Icons.Default.Home, "My Addresses"),
                    InfoItem(Icons.Default.ReceiptLong, "Order History")
                )
            )

            Spacer(Modifier.height(16.dp))

            // Support Section
            InfoSection(
                title = "SUPPORT",
                items = listOf(
                    InfoItem(Icons.Default.HelpOutline, "Help & Support")
                )
            )

            Spacer(Modifier.height(24.dp))

            // Logout
            TextButton(
                onClick = { /* TODO logout */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = Primary)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(80.dp)) // spacing for bottom nav
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar() {
    TopAppBar(
        title = {
            Text("Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        navigationIcon = {
            IconButton(onClick = { /* Back */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { /* Settings */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundLight.copy(alpha = 0.9f),
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}

@Composable
fun ProfileHeader() {
    Box(contentAlignment = Alignment.BottomEnd) {
        // Profile Picture
        KamelImage(
            resource = asyncPainterResource("https://lh3.googleusercontent.com/aida-public/AB6AXuBUD6qRqFnhyuhWddYry9sI4GsQrXfgReWGxbKTNi8enIbiLy56tvmSPqINQ_hc0M7oFwfD3Tst9k-mnioGFwAyktpCGhkY80grAqZi_lhh_xOwhQWXWx3OafWJDMw2-lqPKWz4j5NIT08KaQw-jnKPMM4jb68Neor-GCcsqxUR18uCzC9osYLhokG-lXpMLqs1ZwX9bTFwrkjq7rMZx74ktkGU4Qs7-942Xcw9cH4dLOMIk5CfN7YwqJYl5JEfP9ACF6WRCMbQPAA"),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(3.dp, BackgroundLight, CircleShape),
            contentScale = ContentScale.Crop
        )

        // Edit Button
        Box(
            modifier = Modifier
                .offset(x = (-8).dp, y = (-8).dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Primary)
                .border(2.dp, BackgroundLight, CircleShape)
                .clickable { /* edit profile */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
    }

    Spacer(Modifier.height(12.dp))
    Text("Amelia Johnson", fontSize = 22.sp, fontWeight = FontWeight.Bold)
    Text("amelia.j@example.com", color = TextSecondary, fontSize = 15.sp)
}

data class InfoItem(val icon: ImageVector, val title: String)

@Composable
fun InfoSection(title: String, items: List<InfoItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardLight)
            .padding(16.dp)
    ) {
        Text(title, color = TextSecondary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO */ }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(IconBgLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(item.icon, contentDescription = null, tint = Primary)
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(item.title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary.copy(alpha = 0.8f))
                }
            }
        }
    }
}

@Composable
fun ProfileBottomNavBar() {
    NavigationBar(
        containerColor = CardLight.copy(alpha = 0.9f)
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { /* home */ },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* menu */ },
            icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = null) },
            label = { Text("Menu") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { /* profile */ },
            icon = { Icon(Icons.Default.Person, contentDescription = null, tint = Primary) },
            label = { Text("Profile", color = Primary, fontWeight = FontWeight.Bold) }
        )
    }
}
