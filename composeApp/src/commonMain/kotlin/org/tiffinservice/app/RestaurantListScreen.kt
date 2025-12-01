package org.tiffinservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.viewmodel.koinViewModel
import org.tiffinservice.app.database.RestaurantEntity
import org.tiffinservice.app.viewmodel.TiffinViewModel

private val Primary = Color(0xFFFF7A00)
private val BackgroundLight = Color(0xFFFFF8F0)
private val TextLight = Color(0xFF333333)
private val TextSecondary = Color(0xFF666666)
private val Accent = Color(0xFFFFC700)
private val Surface = Color.White

@Composable
fun RestaurantCard(restaurant: RestaurantEntity) {
    val nav = LocalNavController.current
    Card(
        modifier = Modifier
            .clickable{
                nav.navigate(RestaurantFoodRoute(restaurant.restaurant_id))
            }
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Surface)
    ) {
        Column {

            // IMAGE + BADGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            ) {

                KamelImage(
                    resource = asyncPainterResource(restaurant.thumbnailImageUrl
                        ?: "https://picsum.photos/600/400",),
                    contentDescription = restaurant.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // promo badge
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(
                            color = Accent,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "20% OFF",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLight
                    )
                }
            }

            // CONTENT
            Column(modifier = Modifier.padding(16.dp)) {

                // NAME
                Text(
                    text = restaurant.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight
                )

                // CUISINE
                Text(
                    text = restaurant.description ?: "Indian, Tiffin",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(Modifier.height(8.dp))

                // RATING + TIME
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // ⭐ Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = restaurant.rating.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextLight,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    // Dot
                    Text(
                        "•",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        color = TextSecondary
                    )

                    // 🕒 Time (placeholder)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "25–30 min",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RestaurantListScreen(
    modifier: Modifier = Modifier
) {
    val nav = LocalNavController.current
    val viewModel = koinViewModel<TiffinViewModel>()

    LaunchedEffect(Unit) {
        viewModel.loadSampleData()
    }

    val cart by viewModel.cart.collectAsState()

    val isLoading = viewModel.isLoading.value
    val restaurants = viewModel.restaurants

    if (isLoading) {
        // Centered loading animation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7EFEB)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFFFF7A00),
                strokeWidth = 4.dp
            )
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EFEB))
    ) {
        val itemCount = cart.sumOf { it.quantity }
        val total = cart.sumOf { it.food.price * it.quantity }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF7EFEB)).padding(horizontal = 10.dp),
//            contentPadding = PaddingValues(16.dp),
            contentPadding = PaddingValues(bottom = if (itemCount > 0) 120.dp else 15.dp, top = 15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(restaurants) { restaurant ->
                RestaurantCard(restaurant = restaurant)
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
