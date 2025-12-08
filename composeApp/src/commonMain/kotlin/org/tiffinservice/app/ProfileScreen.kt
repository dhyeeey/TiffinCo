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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

// ==== THEME COLORS ====

private val Primary = Color(0xFFE87A5D)
private val BackgroundLight = Color(0xFFFDF6F0)
private val CardLight = Color.White
private val TextSecondary = Color(0xFF9C7349)
private val IconBgLight = Color(0xFFFBEAE4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {

    val userPrefs = koinInject<UserPreferencesRepository>()

    val userProfile by userPrefs.userProfileFlow.collectAsState(
        initial = UserProfile("", "", "", DeliveryAddress())
    )

    val coroutineScope = rememberCoroutineScope()

    // SHOW bottom sheet if no address saved
    var showSheet by remember { mutableStateOf(false) }

    // bottom sheet state
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val scrollState = rememberScrollState()

    // form states
    var street by remember { mutableStateOf(userProfile.address.street) }
    var city by remember { mutableStateOf(userProfile.address.city) }
    var postal by remember { mutableStateOf(userProfile.address.postalCode) }
    var landmark by remember { mutableStateOf(userProfile.address.landmark) }

    LaunchedEffect(showSheet) {
        if (showSheet) {
            street = userProfile.address.street
            city = userProfile.address.city
            postal = userProfile.address.postalCode
            landmark = userProfile.address.landmark
        }
    }

    // ================= MAIN CONTENT =======================

    Scaffold(
        containerColor = BackgroundLight
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // PROFILE HEADER
            ProfileHeader(
                username = userProfile.username,
                email = userProfile.email,
                phone = userProfile.phoneNumber
            )

            Spacer(Modifier.height(24.dp))


            // PERSONAL INFO
            ExpandableSection(
                title = "Personal Information",
                icon = Icons.Default.Person
            ) {
                InfoRow("Username", userProfile.username)
                InfoRow("Email", userProfile.email)
                InfoRow("Phone", userProfile.phoneNumber)
            }

            Spacer(Modifier.height(16.dp))


            // ADDRESS
            ExpandableSection(
                title = "My Addresses",
                icon = Icons.Default.Home
            ) {

                if (userProfile.address.street.isBlank()) {

                    // EMPTY STATE
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(
                                "No address added yet",
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(Modifier.height(10.dp))

                            Button(
                                onClick = { showSheet = true },
                                colors = ButtonDefaults.buttonColors(Primary)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Add Address")
                            }
                        }
                    }

                } else {

                    AddressRow("Street & House No", userProfile.address.street)
                    AddressRow("City", userProfile.address.city)
                    AddressRow("Postal Code", userProfile.address.postalCode)

                    if (userProfile.address.landmark.isNotBlank()) {
                        AddressRow("Landmark", userProfile.address.landmark)
                    }

                    Spacer(Modifier.height(8.dp))

                    TextButton(
                        onClick = { showSheet = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = Primary)
                    ) {
                        Text("Edit Address")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))


            // ORDER HISTORY
            StaticRow(Icons.Default.ReceiptLong, "Order History") {}

            Spacer(Modifier.height(16.dp))

            // HELP
            StaticRow(Icons.Default.HelpOutline, "Help & Support") {}

            Spacer(Modifier.height(24.dp))


            // LOGOUT
            TextButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = Primary)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(80.dp))
        }


        // ================== BOTTOM SHEET =====================

        if (showSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showSheet = false },
                containerColor = BackgroundLight
            ) {

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text(
                        "Add New Delivery Address",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    OutlinedTextField(
                        value = street,
                        onValueChange = { street = it },
                        label = { Text("Street & House No.") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text("City") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = postal,
                            onValueChange = { postal = it },
                            label = { Text("Postal Code") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    OutlinedTextField(
                        value = landmark,
                        onValueChange = { landmark = it },
                        label = { Text("Landmark (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )


                    Button(
                        onClick = {
                            // SAVE
                            coroutineScope.launch {
                                userPrefs.saveAddress(
                                    DeliveryAddress(
                                        street = street,
                                        city = city,
                                        postalCode = postal,
                                        landmark = landmark
                                    )
                                )
                            }
                            showSheet = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(Primary)
                    ) {
                        Text("Save Address", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

// ===============================================================
// HEADER WITH AVATAR
// ===============================================================

@Composable
fun ProfileHeader(username: String, email: String, phone: String) {

    Box(contentAlignment = Alignment.BottomEnd) {

        // Avatar
        KamelImage(
            resource = asyncPainterResource("https://picsum.photos/200"), // replace with real
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )

        // Edit icon
        Box(
            modifier = Modifier
                .offset(x = (-8).dp, y = (-8).dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Primary)
                .clickable { /* edit profile */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
    }

    Spacer(Modifier.height(12.dp))
    Text(
        username.ifBlank { "Guest User" },
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )

    Text(
        email.ifBlank { "No email provided" },
        color = TextSecondary,
        fontSize = 15.sp
    )

    if (phone.isNotBlank()) {
        Text(
            phone,
            color = TextSecondary,
            fontSize = 14.sp
        )
    }
}


// ===============================================================
// EXPANDABLE SECTION
// ===============================================================

@Composable
fun ExpandableSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardLight)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(IconBgLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Primary)
            }

            Spacer(Modifier.width(12.dp))

            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)

            Spacer(Modifier.weight(1f))

            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = TextSecondary.copy(alpha = 0.8f)
            )
        }

        if (expanded) {
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}


// ===============================================================
// ROW COMPONENTS
// ===============================================================

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold)
        Text(value.ifBlank { "-" }, color = TextSecondary)
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun AddressRow(label: String, value: String) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold)
        Text(value, color = TextSecondary)
    }
}



// Standard row (not expandable)
@Composable
fun StaticRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardLight)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(IconBgLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Primary)
        }

        Spacer(Modifier.width(12.dp))

        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)

        Spacer(Modifier.weight(1f))

        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary.copy(alpha = 0.8f))
    }
}
