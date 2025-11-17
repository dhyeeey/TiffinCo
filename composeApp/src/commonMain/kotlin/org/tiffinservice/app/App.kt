package org.tiffinservice.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.tiffinservice.app.ui.BottomNavBar
import org.tiffinservice.app.ui.LightBackground
import org.tiffinservice.app.ui.OrangeMain
import org.tiffinservice.app.ui.TopBar

val LocalNavController = compositionLocalOf<NavController> {
    error("NavController not provided")
}

@Serializable
data class FoodDetailRoute(val id: Int)

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

@Composable
fun App() {
    TiffinCoTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val stateHolder = rememberSaveableStateHolder()

        CompositionLocalProvider(LocalNavController provides navController) {
            Scaffold(
                topBar = {
                    when (currentRoute?.substringBefore("/")) {
                        "profile" -> ProfileTopBar()
                        else -> TopBar()
                    }
                },
                bottomBar = {
                    BottomNavBar()
                },
                containerColor = LightBackground
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(padding)
                ) {
                    composable("home") { backStackEntry ->
                        stateHolder.SaveableStateProvider(backStackEntry.id) { HomeScreen() }
                    }
                    composable("cart") { backStackEntry ->
                        stateHolder.SaveableStateProvider(backStackEntry.id) { CartScreen() }
                    }
                    composable("profile") { backStackEntry ->
                        stateHolder.SaveableStateProvider(backStackEntry.id) { ProfileScreen() }
                    }
                    composable<FoodDetailRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<FoodDetailRoute>()
                        FoodDetailScreen(itemId = route.id)
                    }
                }
            }
        }
    }
}
