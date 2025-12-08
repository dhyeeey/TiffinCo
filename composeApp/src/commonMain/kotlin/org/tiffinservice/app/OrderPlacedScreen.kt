package org.tiffinservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import org.tiffinservice.app.viewmodel.OrderPlacedViewModel

@Composable
fun OrderItemRow(name: String, quantity: Int, price: Double) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$quantity x $name")
        Text(
            "₹${price.formatFixed(2)}",
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun OrderPlacedScreen(
    orderId: Long,
    onTrackOrder: (Long) -> Unit = {},
    onReturnHome: () -> Unit = {}
) {
    val vm = koinViewModel<OrderPlacedViewModel>()
    val orderState by vm.order.collectAsState()

    // Load data once
    LaunchedEffect(orderId) {
        vm.load(orderId)
    }

    val Primary = Color(0xFFF48C25)
    val BgLight = Color(0xFFF8F7F5)
    val TextDark = Color(0xFF1C140D)
    val SubtleText = Color(0xFF666666)

    Column(
        Modifier
            .fillMaxSize()
            .background(BgLight)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(Modifier.height(24.dp))

        // Top Row (Back + Title)
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onReturnHome) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                "Confirmation",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = TextDark
            )

            Spacer(Modifier.width(48.dp))
        }

        Spacer(Modifier.height(30.dp))

        // Check Icon
        Box(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = Primary,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Title
        Text(
            "Order Placed!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = TextDark
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "Thank you for your purchase.",
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = SubtleText
        )

        Spacer(Modifier.height(16.dp))

        Text(
            "Order ID: #$orderId",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = TextDark
        )

        Spacer(Modifier.height(30.dp))


        // ===========================
        // LOADING STATE
        // ===========================

        if (orderState == null) {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }

            Spacer(Modifier.weight(1f))
        }

        // ===========================
        // DATA LOADED
        // ===========================

        orderState?.let { data ->

            // Order Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        "Your Order",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    data.items.forEach { item ->
                        val food = data.foods.firstOrNull { it.food_id == item.foodId }
                        if (food != null) {
                            OrderItemRow(
                                name = food.name,
                                quantity = item.quantity,
                                price = food.price * item.quantity
                            )
                        }
                    }

                    Divider(color = Color.LightGray, thickness = 1.dp)

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "₹${data.order.totalAmount.formatFixed(2)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                }
            }


            Spacer(Modifier.weight(1f))


            // Buttons

//            Button(
//                onClick = { onTrackOrder(orderId) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(16.dp),
//                colors = ButtonDefaults.buttonColors(Primary)
//            ) {
//                Text("Track Order", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//            }

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = onReturnHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Return to Home",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
