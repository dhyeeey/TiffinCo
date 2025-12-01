package org.tiffinservice.app.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tiffinservice.app.database.FoodEntity
import org.tiffinservice.app.database.RestaurantEntity

data class CartItemEntity(
    val food: FoodEntity,
    var quantity: Int
)

class TiffinRepository {

    val restaurants = mutableListOf<RestaurantEntity>()
    val foods = mutableListOf<FoodEntity>()
    private val _cart = MutableStateFlow<List<CartItemEntity>>(emptyList())
    val cart = _cart.asStateFlow()

    fun insertRestaurant(restaurant: RestaurantEntity): Long {
        val newId = (restaurants.maxOfOrNull { it.restaurant_id } ?: 0L) + 1
        val r = restaurant.copy(restaurant_id = newId)
        restaurants.add(r)
        return newId
    }

    fun insertFood(food: FoodEntity): Long {
        val newId = (foods.maxOfOrNull { it.food_id } ?: 0L) + 1
        val f = food.copy(food_id = newId)
        foods.add(f)
        return newId
    }

    fun getFoodById(id: Long): FoodEntity? {
        return foods.find { it.food_id == id }
    }

    fun getFoodsByRestaurant(id: Long) =
        foods.filter { it.restaurant_id == id }


    // CART — NEW
    fun addToCart(item: CartItemEntity) {

        val current = _cart.value.toMutableList()
        val existing = current.find { it.food.food_id == item.food.food_id }

        if (existing == null) {
            current.add(item)
        } else {
            current.remove(existing)
            current.add(existing.copy(quantity = existing.quantity + 1))
        }
        _cart.value = current
    }

    fun removeFromCart(item: CartItemEntity) {
        val current = _cart.value.toMutableList()
        val existing = current.find { it.food.food_id == item.food.food_id }

        if (existing != null) {
            current.remove(existing)
            if (existing.quantity > 1) {
                current.add(existing.copy(quantity = existing.quantity - 1))
            }
        }
        _cart.value = current
    }

}
