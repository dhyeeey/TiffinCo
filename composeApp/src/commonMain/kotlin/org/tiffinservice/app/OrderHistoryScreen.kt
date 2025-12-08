package org.tiffinservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import org.tiffinservice.app.database.OrderWithItems
import org.tiffinservice.app.viewmodel.TiffinViewModel
import org.tiffinservice.app.LocalNavController

private val BgLight = Color(0xFFF8F7F5)
private val CardColor = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen() {

    val navController = LocalNavController.current
    val vm = koinViewModel<TiffinViewModel>()
    val orders by vm.orders.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadOrders()
    }

    Scaffold(
        containerColor = BgLight
    ) { padding ->

        if (orders == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (orders!!.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No Orders Found")
            }
        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BgLight)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(orders!!) { order ->
                    OrderHistoryCard(order = order) {
//                        navController.navigate(OrderDetailsRoute(order.order.orderId))
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(order: OrderWithItems, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(CardColor)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Order #${order.order.orderId}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Text(
                    "₹${order.order.totalAmount.formatFixed(2)}",
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                order.order.createdAt.toDateFormatted(),
                fontSize = 13.sp,
                color = Color.Gray
            )

            StatusChip(order.order.status)
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val color = when(status.uppercase()) {
        "DELIVERED" -> Color(0xFFB6E8C5)
        "IN PROGRESS" -> Color(0xFFF2C19B)
        "PENDING" -> Color(0xFFF4D5AA)
        "CANCELLED" -> Color(0xFFD0D6DF)
        else -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(status, fontSize = 12.sp)
    }
}
