package org.tiffinservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun CartScreen() {

    val vm = koinViewModel<TiffinViewModel>()
    val cart by vm.cart.collectAsState()

    val subtotal = cart.sumOf { it.food.price * it.quantity }
    val delivery = if (subtotal < 100) 30.0 else 0.0

    var address by remember { mutableStateOf("") }

    // COUPON STATES
    var couponCode by remember { mutableStateOf("") }
    var appliedCoupon by remember { mutableStateOf<String?>(null) }
    var discount by remember { mutableStateOf(0.0) }
    var couponError by remember { mutableStateOf<String?>(null) }

    val total = subtotal + delivery - discount

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

        // ITEMS LIST
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
                        onAdd = { vm.addItem(item.food) },
                        onRemove = { vm.removeItem(item.food) }
                    )
                    if (index < cart.lastIndex)
                        Divider(Modifier.padding(vertical = 12.dp), color = MutedLight)
                }
            }
        }

        // ⭐ COUPON SECTION
        Text("Apply Coupon", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

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
                                appliedCoupon = "SAVE10"
                                discount = 10.0
                                couponError = null
                            }
                            "SAVE20" -> {
                                appliedCoupon = "SAVE20"
                                discount = 20.0
                                couponError = null
                            }
                            "WELCOME50" -> {
                                appliedCoupon = "WELCOME50"
                                discount = 50.0
                                couponError = null
                            }
                            else -> {
                                appliedCoupon = null
                                discount = 0.0
                                couponError = "Invalid coupon"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Apply", fontWeight = FontWeight.Bold, color = Color.White)
                }

                if (couponError != null) {
                    Text(
                        couponError!!,
                        color = Color.Red,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (appliedCoupon != null) {
                    Text(
                        "Applied: $appliedCoupon (-₹${discount.formatFixed(2)})",
                        color = Color(0xFF4A7A37),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // BILL DETAILS
        Text("Bill Details", fontSize = 18.sp, fontWeight = FontWeight.Medium)

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

        // ADDRESS
        Text("Deliver To", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            placeholder = { Text("Enter your full address") },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // PLACE ORDER BUTTON
        Button(
            onClick = { /* handle order */ },
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
