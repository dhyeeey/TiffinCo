package org.tiffinservice.app.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tiffinservice.app.DTO.CartItem
import org.tiffinservice.app.repository.CartRepository

class CartViewModel(
    private val repo: CartRepository
) {
    val cart: StateFlow<List<CartItem>> = repo.cart
    val homeListState = LazyListState()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun add(item: CartItem) = scope.launch {
        repo.add(item)
    }
    fun remove(item: CartItem) = scope.launch {
        repo.remove(item)
    }
}
