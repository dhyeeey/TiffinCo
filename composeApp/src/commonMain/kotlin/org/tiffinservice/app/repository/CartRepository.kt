package org.tiffinservice.app.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tiffinservice.app.DTO.CartItem

class CartRepository {

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart = _cart.asStateFlow()

    fun add(item: CartItem) {
        val current = _cart.value.toMutableList()
        val existing = current.find { it.item.id == item.item.id }

        if (existing == null) {
            current.add(item)
        } else {
            current.remove(existing)
            current.add(existing.copy(quantity = existing.quantity + 1))
        }
        _cart.value = current
    }

    fun remove(item: CartItem) {
        val current = _cart.value.toMutableList()
        val existing = current.find { it.item.id == item.item.id }

        if (existing != null) {
            current.remove(existing)
            if (existing.quantity > 1) {
                current.add(existing.copy(quantity = existing.quantity - 1))
            }
        }
        _cart.value = current
    }
}
