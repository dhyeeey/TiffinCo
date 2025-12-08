package org.tiffinservice.app.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tiffinservice.app.UserProfile
import org.tiffinservice.app.database.FoodDao
import org.tiffinservice.app.database.FoodEntity
import org.tiffinservice.app.database.OrderDao
import org.tiffinservice.app.database.OrderEntity
import org.tiffinservice.app.database.OrderItemEntity
import org.tiffinservice.app.database.OrderWithDetails
import org.tiffinservice.app.database.RestaurantDao
import org.tiffinservice.app.database.RestaurantEntity

data class CartItemEntity(
    val food: FoodEntity,
    var quantity: Int
)

//class TiffinRepository {
//
//    val restaurants = mutableListOf<RestaurantEntity>()
//    val foods = mutableListOf<FoodEntity>()
//    private val _cart = MutableStateFlow<List<CartItemEntity>>(emptyList())
//    val cart = _cart.asStateFlow()
//
//    fun insertRestaurant(restaurant: RestaurantEntity): Long {
//        val newId = (restaurants.maxOfOrNull { it.restaurant_id } ?: 0L) + 1
//        val r = restaurant.copy(restaurant_id = newId)
//        restaurants.add(r)
//        return newId
//    }
//
//    fun insertFood(food: FoodEntity): Long {
//        val newId = (foods.maxOfOrNull { it.food_id } ?: 0L) + 1
//        val f = food.copy(food_id = newId)
//        foods.add(f)
//        return newId
//    }
//
//    fun getFoodById(id: Long): FoodEntity? {
//        return foods.find { it.food_id == id }
//    }
//
//    fun getFoodsByRestaurant(id: Long) =
//        foods.filter { it.restaurant_id == id }
//
//
//    // CART — NEW
//    fun addToCart(item: CartItemEntity) {
//
//        val current = _cart.value.toMutableList()
//        val existing = current.find { it.food.food_id == item.food.food_id }
//
//        if (existing == null) {
//            current.add(item)
//        } else {
//            current.remove(existing)
//            current.add(existing.copy(quantity = existing.quantity + 1))
//        }
//        _cart.value = current
//    }
//
//    fun removeFromCart(item: CartItemEntity) {
//        val current = _cart.value.toMutableList()
//        val existing = current.find { it.food.food_id == item.food.food_id }
//
//        if (existing != null) {
//            current.remove(existing)
//            if (existing.quantity > 1) {
//                current.add(existing.copy(quantity = existing.quantity - 1))
//            }
//        }
//        _cart.value = current
//    }
//
//}


class TiffinRepository(
    private val restaurantDao: RestaurantDao,
    private val foodDao: FoodDao,
    private val orderDao: OrderDao
) {

    // ===== CART (in-memory) =========
    private val _cart = MutableStateFlow<List<CartItemEntity>>(emptyList())
    val cart = _cart.asStateFlow()

    // ===== RESTAURANT DB calls =====

    suspend fun insertRestaurant(restaurant: RestaurantEntity) =
        restaurantDao.insertRestaurant(restaurant)

    suspend fun insertAllRestaurants(restaurants: List<RestaurantEntity>) =
        restaurantDao.insertAllRestaurants(restaurants)

    suspend fun getAllRestaurants(): List<RestaurantEntity> =
        restaurantDao.getAllRestaurant()

    suspend fun getAllFood(): List<FoodEntity> = foodDao.getAllFood()

    suspend fun getRestaurantCount(): Int =
        restaurantDao.countRestaurants()


    // ===== FOOD DB calls =====

    suspend fun insertFood(food: FoodEntity) =
        foodDao.insertFood(food)

    suspend fun insertAllFoods(foods: List<FoodEntity>) =
        foodDao.insertAllFood(foods)

    suspend fun getFoodsByRestaurant(id: Long) =
        foodDao.getFoodByRestaurant(id)

    suspend fun getFoodById(id: Long) =
        foodDao.getFoodById(id)

    fun watchFoodsByRestaurant(restaurantId: Long): Flow<List<FoodEntity>> =
        foodDao.watchFoodsByRestaurant(restaurantId)

    suspend fun getFoodCount(): Int =
        foodDao.countFood()

    fun clearCart(){
        _cart.value = emptyList()
    }

    suspend fun getOrderDetails(orderId: Long): OrderWithDetails? {
        val order = orderDao.getOrder(orderId) ?: return null

        val foodIds = order.items.map { it.foodId }

        val foods = foodDao.getFoodsByIds(foodIds)

        return OrderWithDetails(
            order = order.order,
            items = order.items,
            foods = foods
        )
    }

    suspend fun placeOrder(currentDiscount : Double, userProfile: UserProfile): Long {
        val cartItems = cart.value

        val subtotal = cartItems.sumOf { it.food.price * it.quantity }
        val delivery = if (subtotal < 100) 30.0 else 0.0

        val orderId = orderDao.insertOrder(
            OrderEntity(
                userId = 1,
                totalAmount = subtotal,
                deliveryFee = delivery,
                discount = currentDiscount,
                address = userProfile.address.toString()
            )
        )

        orderDao.insertOrderItems(
            cartItems.map {
                OrderItemEntity(
                    orderId = orderId,
                    foodId = it.food.food_id,
                    quantity = it.quantity,
                    price = it.food.price
                )
            }
        )

        clearCart()

        return orderId
    }


    // ===== CART OPERATIONS (unchanged) =====

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

    // ===== SEEDING DB =====

    suspend fun isDatabaseEmpty(): Boolean =
        getRestaurantCount() == 0 && getFoodCount() == 0
}
