package org.tiffinservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.tiffinservice.app.DTO.CartItem
import org.tiffinservice.app.repository.CartItemEntity
import org.tiffinservice.app.viewmodel.CartViewModel
import org.tiffinservice.app.viewmodel.TiffinViewModel

private val Primary = Color(0xFFF48C25)
private val BgLight = Color(0xFFF8F7F5)
private val SurfaceLight = Color(0xFFFCFAF8)
private val MutedLight = Color(0xFFF4EDE7)
private val SubtleLight = Color(0xFF9C7349)
private val TextDark = Color(0xFF1C140D)


@Composable
fun ChipItem(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MutedLight) // light background
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontSize = 13.sp,
            color = TextDark,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BillBreakdown(subtotal: Double, delivery: Double, discount: Double, total: Double) {

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", color = SubtleLight)
                Text("₹${subtotal.formatFixed(2)}", color = SubtleLight)
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Delivery Fee", color = SubtleLight)
                Text("₹${delivery.formatFixed(2)}", color = SubtleLight)
            }

            if (discount > 0) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Discount", color = Color(0xFF4A7A37))
                    Text("-₹${discount.formatFixed(2)}", color = Color(0xFF4A7A37))
                }
            }

            Divider(Modifier.padding(vertical = 8.dp), color = MutedLight)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Amount", fontWeight = FontWeight.Bold)
                Text("₹${total.formatFixed(2)}", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CartItemRow(cartItem: CartItemEntity, onAdd: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            cartItem.food.imageUrl?.let {
                KamelImage(
                    resource = asyncPainterResource(it),
                    contentDescription = cartItem.food.name,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(cartItem.food.name, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                Text("₹${cartItem.food.price}", color = SubtleLight, fontSize = 13.sp)
            }
        }

        Row(
            modifier = Modifier.width(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallCircleButton("-", onClick = onRemove)
            Text("${cartItem.quantity}", fontWeight = FontWeight.Medium)
            SmallCircleButton("+", onClick = onAdd)
        }
    }
}

@Composable
fun SmallCircleButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(MutedLight)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun CartScreen() {

    val navController = LocalNavController.current

    val vm = koinViewModel<TiffinViewModel>()
    val cart by vm.cart.collectAsState()

    val userPrefs = koinInject<UserPreferencesRepository>()
    val userProfile by userPrefs.userProfileFlow.collectAsState(
        initial = UserProfile("", "", "", DeliveryAddress())
    )

    val coroutineScope = rememberCoroutineScope()

    val subtotal = cart.sumOf { it.food.price * it.quantity }
    val delivery = if (subtotal < 100) 30.0 else 0.0

    // COUPON STATES
    var couponCode by remember { mutableStateOf("") }
    var appliedCoupon by remember { mutableStateOf<String?>(null) }
    var discount by remember { mutableStateOf(0.0) }
    var couponError by remember { mutableStateOf<String?>(null) }

    val total = subtotal + delivery - discount


    // ==================== BOTTOM SHEET STATE ====================

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

//    LaunchedEffect(userProfile.address.street) {
//        // only trigger when data is loaded and still empty
////        if (userProfile.address.street.isBlank()) {
////            showSheet = true
////        }
//    }

    // Form fields
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

    // ==================== SCREEN ROOT ====================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // Title
        Text("Confirm Your Order", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)


        // ================= ITEMS LIST ====================

        Text("Your Items", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                cart.forEachIndexed { index, item ->
                    CartItemRow(
                        cartItem = item,
                        onAdd = { coroutineScope.launch { vm.addItem(item.food) } },
                        onRemove = { coroutineScope.launch { vm.removeItem(item.food) } }
                    )
                    if (index < cart.lastIndex)
                        Divider(Modifier.padding(vertical = 12.dp), color = MutedLight)
                }
            }
        }


        // ================= COUPON CHIPS & FIELD ====================

        Text("Apply Coupon", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                val couponList = listOf("SAVE10", "SAVE20", "WELCOME50")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    couponList.forEach { code ->
                        ChipItem(
                            text = code,
                            onClick = {
                                couponCode = code
                                appliedCoupon = code
                                discount = when (code) {
                                    "SAVE10" -> 10.0
                                    "SAVE20" -> 20.0
                                    "WELCOME50" -> 50.0
                                    else -> 0.0
                                }
                                couponError = null
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = couponCode,
                    onValueChange = {
                        couponCode = it.uppercase()
                        couponError = null
                    },
                    placeholder = { Text("Enter coupon code") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = {
                        when (couponCode.trim()) {
                            "SAVE10" -> {
                                appliedCoupon = "SAVE10"; discount = 10.0; couponError = null
                            }
                            "SAVE20" -> {
                                appliedCoupon = "SAVE20"; discount = 20.0; couponError = null
                            }
                            "WELCOME50" -> {
                                appliedCoupon = "WELCOME50"; discount = 50.0; couponError = null
                            }
                            else -> {
                                appliedCoupon = null; discount = 0.0; couponError = "Invalid coupon"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Apply", fontWeight = FontWeight.Bold, color = Color.White)
                }

                couponError?.let {
                    Text(it, color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                appliedCoupon?.let {
                    Text(
                        "Applied: $it (-₹${discount.formatFixed(2)})",
                        color = Color(0xFF4A7A37),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }


        // ================= BILL DETAILS ====================

        Text("Bill Details", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        BillBreakdown(subtotal, delivery, discount, total)


        // ================= ADDRESS SECTION ====================

        Text("Deliver To", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        if (userProfile.address.street.isBlank()) {

            // EMPTY STATE
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text("No address added yet", color = SubtleLight)

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { showSheet = true },
                        colors = ButtonDefaults.buttonColors(Primary)
                    ) {
                        Text("Add Address", color = Color.White)
                    }
                }
            }

        } else {

            // SHOW EXISTING ADDRESS
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {

                    Text(userProfile.address.street, fontWeight = FontWeight.SemiBold)
                    Text("${userProfile.address.city}, ${userProfile.address.postalCode}")

                    if (userProfile.address.landmark.isNotBlank())
                        Text("Landmark: ${userProfile.address.landmark}", color = SubtleLight)

                    Spacer(Modifier.height(6.dp))

                    TextButton(onClick = { showSheet = true }) {
                        Text("Change Address", color = Primary)
                    }
                }
            }
        }


        // ================= PLACE ORDER ====================

        Button(
            onClick = {
                coroutineScope.launch {
                    val orderId = vm.placeOrder(
                        currentDiscount = discount,
                        userProfile = userProfile
                    )

                    navController.navigate(OrderPlacedRoute(orderId)) {
                        popUpTo("${ROUTE_CART_ROOT}/cart") { inclusive = true }
                        launchSingleTop = true
                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Place Order | ₹${total.toInt()}", fontSize = 18.sp, color = Color.White)
        }

        Spacer(Modifier.height(24.dp))
    }


    // ================== BOTTOM SHEET =====================

    if (showSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showSheet = false },
            containerColor = BgLight
        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text("Add New Delivery Address", fontWeight = FontWeight.Bold, fontSize = 20.sp)

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

