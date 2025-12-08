package org.tiffinservice.app.ui
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import org.tiffinservice.app.LocalNavController
import org.tiffinservice.app.ROUTE_CART_ROOT
import org.tiffinservice.app.ROUTE_PROFILE_ROOT
import org.tiffinservice.app.ROUTE_RESTAURANTS_ROOT


private data class BottomNavItem(
    val rootRoute: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val navItems = listOf(
    BottomNavItem(ROUTE_RESTAURANTS_ROOT, "Home", Icons.Default.Home),
    BottomNavItem(ROUTE_CART_ROOT, "Cart", Icons.Default.ShoppingCart),
    BottomNavItem(ROUTE_PROFILE_ROOT, "Profile", Icons.Default.Person)
)

@Composable
fun BottomNavBar() {

    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = Color.White) {

        navItems.forEach { item ->

            val selected = currentDestination
                ?.hierarchy
                ?.any { it.route == item.rootRoute } == true

            val active = Color(0xFFF48C25)
            val inactive = Color.Gray

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.rootRoute) {

                        // 🔥 Proper safe backstack handling
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                            saveState = true
                        }

                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (selected) active else inactive
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (selected) active else inactive
                    )
                }
            )
        }
    }
}
