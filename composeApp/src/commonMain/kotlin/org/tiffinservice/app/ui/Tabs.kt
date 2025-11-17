package org.tiffinservice.app.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.tiffinservice.app.CartScreen
import org.tiffinservice.app.HomeScreen

object HomeTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 0u, title = "Home")

    @Composable
    override fun Content() {
//        HomeScreen().Content()
    }
}

object OrdersTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 1u, title = "Orders")

    @Composable
    override fun Content() {
        Text("Orders Coming Soon...")
    }
}

object CartTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 2u, title = "Cart")

    @Composable
    override fun Content() {
//        CartScreen().Content()
    }
}

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 3u, title = "Profile")

    @Composable
    override fun Content() {
        Text("Profile Coming Soon...")
    }
}
