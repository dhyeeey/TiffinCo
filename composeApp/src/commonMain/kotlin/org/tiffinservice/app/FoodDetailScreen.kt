package org.tiffinservice.app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.viewmodel.koinViewModel
import org.tiffinservice.app.ui.Background
import org.tiffinservice.app.ui.TextGray
import org.tiffinservice.app.viewmodel.TiffinViewModel

private val Primary = Color(0xFFF48C25)

@Composable
fun FoodDetailScreen(itemId: Long) {

    val nav = LocalNavController.current
    val vm = koinViewModel<TiffinViewModel>()

    // get food entity
    val food = vm.getFoodById(itemId)
    val cart by vm.cart.collectAsState()

    if (food == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading food details...", color = Color.Gray)
        }
        return
    }

    val quantity = cart.find { it.food.food_id == food.food_id }?.quantity ?: 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔙 Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Text(
                    food.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.size(24.dp))
            }

            // 🖼 Image
            KamelImage(
                resource = asyncPainterResource(food.imageUrl ?: "https://picsum.photos/600"),
                contentDescription = food.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            // 🏷 Title
            Text(
                food.name,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))

            // 💰 Price + Counter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Primary.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("₹", color = Primary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = food.price.toInt().toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // ➕➖ Counter UI (same as before)
                Row(
                    modifier = Modifier
                        .background(Primary.copy(alpha = 0.2f), RoundedCornerShape(50))
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    IconButton(
                        onClick = {
                            vm.removeItem(food)
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Text("-", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        quantity.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.widthIn(min = 24.dp),
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        onClick = {
                            vm.addItem(food)
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .background(Primary, CircleShape)
                    ) {
                        Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // 🧾 Tabs (mocked)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Text(
                    "Description",
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .border(
                            BorderStroke(2.dp, Primary),
                            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                        .padding(bottom = 8.dp)
                )

                Text("Ingredients", color = TextGray.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
            }

            Text(
                text = food.description ?: "No description available",
                color = TextGray,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // 🧡 Order Now Button
        Button(
            onClick = { /* place order */ },
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Order Now", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
        }
    }
}