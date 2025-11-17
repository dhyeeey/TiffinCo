package org.tiffinservice.app.ui
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import org.tiffinservice.app.LocalNavController

@Composable
fun BottomNavBar() {

    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
                      },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (currentRoute == "home") Color(0xFFF48C25) else Color.Gray
                )
            },
            label = {
                Text(
                    "Home",
                    color = if (currentRoute == "home") Color(0xFFF48C25) else Color.Gray
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == "orders",
            onClick = { /* future */ },
            icon = {
                Icon(
                    Icons.Default.ListAlt,
                    contentDescription = "Orders",
                    tint = if (currentRoute == "orders") Color(0xFFF48C25) else Color.Gray
                )
            },
            label = {
                Text(
                    "Orders",
                    color = if (currentRoute == "orders") Color(0xFFF48C25) else Color.Gray
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == "cart",
            onClick = { navController.navigate("cart"){
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            } },
            icon = {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = if (currentRoute == "cart") Color(0xFFF48C25) else Color.Gray
                )
            },
            label = {
                Text(
                    "Cart",
                    color = if (currentRoute == "cart") Color(0xFFF48C25) else Color.Gray
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile"){
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            } },
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = if (currentRoute == "profile") Color(0xFFF48C25) else Color.Gray
                )
            },
            label = {
                Text(
                    "Profile",
                    color = if (currentRoute == "profile") Color(0xFFF48C25) else Color.Gray
                )
            }
        )
    }
}
