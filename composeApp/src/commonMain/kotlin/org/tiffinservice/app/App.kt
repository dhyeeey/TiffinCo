package org.tiffinservice.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.tiffinservice.app.ui.BottomNavBar
import org.tiffinservice.app.ui.LightBackground
import org.tiffinservice.app.ui.OrangeMain
import org.tiffinservice.app.ui.TopBar


// ========= ROOT GRAPH ROUTES (used by bottom nav) =========

const val ROUTE_RESTAURANTS_ROOT = "restaurants_root"
const val ROUTE_CART_ROOT = "cart_root"
const val ROUTE_PROFILE_ROOT = "profile_root"

// ========= Local NavController so screens can call LocalNavController.current =========

val LocalNavController = compositionLocalOf<NavController> {
    error("NavController not provided")
}

// ========= Type-safe routes for detail screens =========

@Serializable
data class FoodDetailRoute(val id: Long)

@Serializable
data class RestaurantFoodRoute(val restaurantId: Long)

@Serializable
data class OrderPlacedRoute(val orderId: Long)

// ========= Theme =========

@Composable
fun TiffinCoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = OrangeMain,
            background = LightBackground,
            surface = LightBackground
        ),
        content = content
    )
}

// ========= Profile Top Bar (special bar for profile tab) =========

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(onBack: (() -> Unit)? = null) {
    TopAppBar(
        title = {
            Text("Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        navigationIcon = {
            // Optional back button (currently just icon, no behavior)
            IconButton(onClick = { onBack?.invoke() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFDF6F0).copy(alpha = 0.9f),
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black
        )
    )
}

// ========= Main App =========

@Composable
fun App() {
    TiffinCoTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val stateHolder = rememberSaveableStateHolder()
        val userPrefs = koinInject<UserPreferencesRepository>()

        var isChecking by remember { mutableStateOf(true) }
        var isLoggedIn by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        // Check DataStore on app start
        LaunchedEffect(Unit) {
            isLoggedIn = userPrefs.isUserLoggedIn()
            isChecking = false
        }

        when {
            isChecking -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading...")
                }
            }

            !isLoggedIn -> {
                // LOGIN FLOW
                LoginScreen(
                    onContinue = { phone, email, username ->
                        coroutineScope.launch {
                            userPrefs.saveUserProfile(
                                username = username,
                                email = email,
                                phoneNumber = phone
                            )
                            isLoggedIn = true
                        }
                    }
                )
            }

            else -> {
                // MAIN NAV APP
                CompositionLocalProvider(LocalNavController provides navController) {

                    // Figure out if we are on profile graph to switch top bar
                    val isOnProfile =
                        currentDestination
                            ?.hierarchy
                            ?.any { it.route == ROUTE_PROFILE_ROOT } == true

                    Scaffold(
                        topBar = {
                            val isOnOrderPlaced =
                                currentDestination?.hierarchy?.any {
                                    it.route?.startsWith("OrderPlacedRoute") == true
                                } == true

                            if (!isOnOrderPlaced) {
                                if (isOnProfile) {
                                    ProfileTopBar()
                                } else {
                                    TopBar()
                                }
                            }
                        },
                        bottomBar = {
                            BottomNavBar()
                        },
                        containerColor = LightBackground
                    ) { padding ->

                        NavHost(
                            navController = navController,
                            startDestination = ROUTE_RESTAURANTS_ROOT,
                            modifier = Modifier.padding(padding)
                        ) {

                            // ========== RESTAURANTS GRAPH ==========
                            navigation(
                                route = ROUTE_RESTAURANTS_ROOT,
                                startDestination = "restaurants"
                            ) {

                                // Restaurant list (home tab)
                                composable("restaurants") { backStackEntry ->
                                    stateHolder.SaveableStateProvider(backStackEntry.id) {
                                        RestaurantListScreen()
                                    }
                                }

                                // Foods for a restaurant
                                composable<RestaurantFoodRoute> { backStackEntry ->
                                    val r = backStackEntry.toRoute<RestaurantFoodRoute>()
                                    RestaurantFoodScreen(restaurantId = r.restaurantId)
                                }

                                // Food detail
                                composable<FoodDetailRoute> { backStackEntry ->
                                    val r = backStackEntry.toRoute<FoodDetailRoute>()
                                    FoodDetailScreen(itemId = r.id)
                                }
                            }

                            // ========== CART GRAPH ==========
                            navigation(
                                route = ROUTE_CART_ROOT,
                                startDestination = "cart"
                            ) {
                                composable("cart") { backStackEntry ->
                                    stateHolder.SaveableStateProvider(backStackEntry.id) {
                                        CartScreen()
                                    }
                                }
                            }

                            // ========== PROFILE GRAPH ==========
                            navigation(
                                route = ROUTE_PROFILE_ROOT,
                                startDestination = "profile"
                            ) {
                                composable("profile") { backStackEntry ->
                                    stateHolder.SaveableStateProvider(backStackEntry.id) {
                                        ProfileScreen()
                                    }
                                }
                            }

                            // ORDER PLACED SCREEN
                            composable<OrderPlacedRoute> { backStackEntry ->
                                val route = backStackEntry.toRoute<OrderPlacedRoute>()

                                OrderPlacedScreen(
                                    orderId = route.orderId,
                                    onTrackOrder = {
                                        // later implement
//                                         navController.navigate(OrderTrackingRoute(route.orderId))
                                    },
                                    onReturnHome = {
                                        navController.navigate(ROUTE_RESTAURANTS_ROOT) {
                                            popUpTo(ROUTE_RESTAURANTS_ROOT) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
