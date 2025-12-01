package org.tiffinservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import org.tiffinservice.app.DTO.MenuItem
import org.tiffinservice.app.ui.Background
import org.tiffinservice.app.ui.TextGray
import org.tiffinservice.app.viewmodel.CartViewModel
import org.tiffinservice.app.viewmodel.MenuViewModel

private val Primary = Color(0xFFF48C25)

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    val vm: CartViewModel = koinInject()
//        val tabNavigator = cafe.adriel.voyager.navigator.tab.LocalTabNavigator.current
    val cart by vm.cart.collectAsState()
    val menuVM: MenuViewModel = koinInject()
    val menuItems by menuVM.menuItems.collectAsState()

    val cartCount = cart.sumOf { it.quantity }
    val cartTotal = cart.sumOf { it.item.price * it.quantity }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = if (cartCount > 0) 120.dp else 0.dp)
        ) {
            items(menuItems) { item ->
                val qty = cart.find { it.item.id == item.id }?.quantity ?: 0
                MenuItemCard(
                    item = item,
                    quantity = qty,
                    onAdd = { vm.add(CartItem(item, 1)) },
                    onRemove = { vm.remove(CartItem(item, 1)) },
                    onClick = { navController.navigate(FoodDetailRoute(id = item.id.toLong())) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        // Floating Cart Summary
        if (cartCount > 0) {
            FloatingCartSummary(
                itemCount = cartCount,
                total = cartTotal,
                onClick = {
//                        tabNavigator.current = org.tiffinservice.app.ui.CartTab
                    navController.navigate("cart")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
fun MenuItemCard(
    item: MenuItem,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // 📷 Image
            KamelImage(
                resource = asyncPainterResource(item.imageUrl),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            // 📄 Details
            Column(modifier = Modifier.padding(16.dp)) {
                Text(item.category, color = Primary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text(item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    item.description,
                    fontSize = 14.sp,
                    color = TextGray,
                    lineHeight = 18.sp
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("₹${item.price}", fontSize = 17.sp, fontWeight = FontWeight.Bold)

                    if (quantity == 0) {
                        Button(
                            onClick = onAdd,
                            modifier = Modifier.height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Add", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .background(Primary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .height(38.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = onRemove) {
                                Text("-", fontWeight = FontWeight.Bold, color = Primary, fontSize = 18.sp)
                            }
                            Text(
                                "$quantity",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            TextButton(onClick = onAdd) {
                                Text("+", fontWeight = FontWeight.Bold, color = Primary, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingCartSummary(itemCount: Int, total: Double, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$itemCount items | ₹${total.formatFixed(2)}", color = Color.White, fontWeight = FontWeight.Bold)
            Text("View Cart →", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
