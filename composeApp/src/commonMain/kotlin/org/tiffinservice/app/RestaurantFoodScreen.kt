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
import androidx.compose.runtime.LaunchedEffect
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
import org.koin.compose.viewmodel.koinViewModel
import org.tiffinservice.app.database.FoodEntity
import org.tiffinservice.app.repository.CartItemEntity
import org.tiffinservice.app.ui.Primary
import org.tiffinservice.app.ui.TextGray
import org.tiffinservice.app.viewmodel.TiffinViewModel

@Composable
fun FoodItemCard(
    food: FoodEntity,
    quantity : Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {

            KamelImage(
                resource = asyncPainterResource(food.imageUrl ?: "https://picsum.photos/600"),
                contentDescription = food.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {

                Text(food.category ?: "Food", color = Primary, fontSize = 13.sp)
                Text(food.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    food.description ?: "",
                    fontSize = 14.sp,
                    color = TextGray
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("₹${food.price}", fontSize = 17.sp, fontWeight = FontWeight.Bold)

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
                            Text("$quantity", fontWeight = FontWeight.Bold, color = Color.Black)
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
fun RestaurantFoodScreen(restaurantId: Long) {

    val nav = LocalNavController.current
    val vm = koinViewModel<TiffinViewModel>()

    // Observe state
    val foods by vm.foods
    val cart by vm.cart.collectAsState()

    // load foods on screen load
    LaunchedEffect(restaurantId) {
        vm.loadFoodsForRestaurant(restaurantId)
    }

    // Wrap the content in a Box to allow for alignment
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EFEB))
    ) {

        // Floating Cart Summary
        val itemCount = cart.sumOf { it.quantity }
        val total = cart.sumOf { it.food.price * it.quantity }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp), // LazyColumn fills the Box
//            contentPadding = PaddingValues(16.dp),
            contentPadding = PaddingValues(bottom = if (itemCount > 0) 120.dp else 20.dp, top = 15.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(foods) { food ->
                val quantity = cart.find { it.food.food_id == food.food_id }?.quantity ?: 0

                FoodItemCard(
                    food = food,
                    quantity = quantity,
                    onAdd = { vm.addItem(CartItemEntity(food, 1)) },
                    onRemove = { vm.removeItem(CartItemEntity(food, 1)) },
                    onClick = {
                        // nav.navigate(FoodDetailRoute(id = food.food_id.toInt()))
                    }
                )
            }
        }

        if (itemCount > 0) {
            FloatingCartSummary(
                itemCount = itemCount,
                total = total,
                onClick = {
                     nav.navigate("cart")
                },
                // Now 'align' will work because it's inside a BoxScope
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}