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
import org.tiffinservice.app.DTO.CartItem
import org.tiffinservice.app.viewmodel.CartViewModel

private val Primary = Color(0xFFF48C25)
private val BgLight = Color(0xFFF8F7F5)
private val SurfaceLight = Color(0xFFFCFAF8)
private val MutedLight = Color(0xFFF4EDE7)
private val SubtleLight = Color(0xFF9C7349)
private val TextDark = Color(0xFF1C140D)

@Composable
fun CartScreen() {

    val vm: CartViewModel = koinInject()
    val cart by vm.cart.collectAsState()

    val subtotal = cart.sumOf { it.item.price * it.quantity }
    val delivery = if (subtotal < 100) 30.0 else 0.0
    val total = subtotal + delivery
    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("cod") }

    // Simple scrollable layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // ✅ Title
        Text(
            "Confirm Your Order",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        // ✅ Items
        Text("Your Items", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                cart.forEachIndexed { index, item ->
                    CartItemRow(
                        cartItem = item,
                        onAdd = { vm.add(item) },
                        onRemove = { vm.remove(item) }
                    )
                    if (index < cart.lastIndex)
                        Divider(Modifier.padding(vertical = 12.dp), color = MutedLight)
                }
            }
        }

        // ✅ Bill Details
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
                Divider(Modifier.padding(vertical = 8.dp), color = MutedLight)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Amount", fontWeight = FontWeight.Bold)
                    Text("₹${total.formatFixed(2)}", fontWeight = FontWeight.Bold)
                }
            }
        }

        // ✅ Address
        Text("Deliver To", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            placeholder = { Text("Enter your full address, including flat number") },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // ✅ Payment Methods
        Text("Choose Payment Method", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PaymentOption(
                    title = "Cash on Delivery",
                    subtitle = "Pay with cash at your doorstep.",
                    selected = paymentMethod == "cod",
                    onSelect = { paymentMethod = "cod" }
                )
                Divider(color = MutedLight)
                PaymentOption(
                    title = "UPI (Manual)",
                    subtitle = "UPI ID will be shown on the next screen.",
                    selected = paymentMethod == "upi",
                    onSelect = { paymentMethod = "upi" }
                )
            }
        }

        // ✅ Place Order Button (scrolls naturally)
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

        Spacer(Modifier.height(24.dp)) // bottom padding
    }
}

@Composable
fun CartItemRow(cartItem: CartItem, onAdd: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 🖼️ Left Section — Image + Text
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image
            KamelImage(
                resource = asyncPainterResource(cartItem.item.imageUrl),
                contentDescription = cartItem.item.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Title + Price
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    cartItem.item.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    "₹${cartItem.item.price}",
                    color = Color(0xFF9C7349),
                    fontSize = 13.sp
                )
            }
        }

        // ➕➖ Right Section — Quantity Controls
        Row(
            modifier = Modifier.width(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallCircleButton("-", onClick = onRemove)
            Text(
                "${cartItem.quantity}",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.widthIn(min = 20.dp)
            )
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

@Composable
fun PaymentOption(title: String, subtitle: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        RadioButton(selected = selected, onClick = onSelect, colors = RadioButtonDefaults.colors(selectedColor = Primary))
        Column {
            Text(title, fontWeight = FontWeight.Medium)
            Text(subtitle, color = SubtleLight, fontSize = 12.sp)
        }
    }
}
