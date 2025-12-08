package org.tiffinservice.app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.util.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tiffinservice.app.UserProfile
import org.tiffinservice.app.database.FoodEntity
import org.tiffinservice.app.database.OrderItemEntity
import org.tiffinservice.app.database.OrderWithDetails
import org.tiffinservice.app.database.OrderWithItems
import org.tiffinservice.app.database.RestaurantEntity
import org.tiffinservice.app.repository.CartItemEntity
import org.tiffinservice.app.repository.TiffinRepository
import org.tiffinservice.app.sample.SampleDataProvider

//class TiffinViewModel(
//    private val repo: TiffinRepository
//) : ViewModel() {
//
//    private val scope = CoroutineScope(Dispatchers.Main)
//
//    val restaurants = repo.restaurants
//    val foods = mutableStateOf<List<FoodEntity>>(emptyList())
//    val cart = repo.cart
//    val isLoading = mutableStateOf(true)
//
//    fun loadSampleData() = scope.launch{
//        if(isLoading.value){
//            SampleDataProvider.insertAllSampleData(repo)
//            isLoading.value = false
//        }
//    }
//
//    fun getFoodById(id: Long): FoodEntity? {
//        return repo.getFoodById(id)
//    }
//
//    fun loadFoodsForRestaurant(id: Long) {
//        foods.value = repo.getFoodsByRestaurant(id)
//    }
//
//    fun addItem(cartItemEntity: CartItemEntity) = scope.launch {
//        repo.addToCart(cartItemEntity)
//    }
//
//    fun removeItem(cartItemEntity: CartItemEntity)=scope.launch {
//        repo.removeFromCart(cartItemEntity)
//    }
//
//    fun addItem(food: FoodEntity)=scope.launch {
//        repo.addToCart(CartItemEntity(food, 1))
//    }
//
//    fun removeItem(food: FoodEntity)=scope.launch {
//        repo.removeFromCart(CartItemEntity(food, 1))
//    }
//
//
//}


class TiffinViewModel(
    private val repo: TiffinRepository
) : ViewModel() {

    val restaurants = mutableStateOf<List<RestaurantEntity>>(emptyList())
    val foods = MutableStateFlow<List<FoodEntity>>(emptyList())
    val cart = repo.cart
    val isLoading = mutableStateOf(true)
    val selectedFood = mutableStateOf<FoodEntity?>(null)

    init {
        loadInitialData()
    }

    private val _orders = MutableStateFlow<List<OrderWithItems>?>(null)
    val orders: StateFlow<List<OrderWithItems>?> = _orders.asStateFlow()

    fun loadOrders() = viewModelScope.launch {
        _orders.value = repo.getOrders()
    }

    suspend fun placeOrder(currentDiscount : Double, userProfile: UserProfile): Long {
        return repo.placeOrder(currentDiscount, userProfile);
    }

    fun observeFoods(restaurantId: Long) {
        viewModelScope.launch {
            repo.watchFoodsByRestaurant(restaurantId).collect { list ->
                foods.value = list
            }
        }

        println("Restaurant id : $restaurantId")
    }

    suspend fun getOrderDetails(orderId: Long): OrderWithDetails? = repo.getOrderDetails(orderId)

    private fun loadInitialData() = viewModelScope.launch {
        // Seed database if empty
        if (repo.isDatabaseEmpty()) {
            SampleDataProvider.insertAllSampleData(repo)
        }

        restaurants.value = repo.getAllRestaurants()
        foods.value = repo.getAllFood()

        isLoading.value = false
    }

    suspend fun loadFood(id: Long)  {
        val item = repo.getFoodById(id)
        selectedFood.value = item
    }

    suspend fun getFoodById(id: Long): FoodEntity? {
        return repo.getFoodById(id)
    }

    suspend fun fetchFoodsForRestaurant(id: Long){
        println("Restaurant id : $id")
        foods.value = emptyList()
        foods.value = repo.getFoodsByRestaurant(id)
        foods.value.forEach {
            println(it)
        }
    }

    suspend fun addItem(food: FoodEntity) {
        repo.addToCart(CartItemEntity(food, 1))
    }

    suspend fun addItem(cartItemEntity: CartItemEntity){
        repo.addToCart(cartItemEntity)
    }

    suspend fun removeItem(food: FoodEntity) {
        repo.removeFromCart(CartItemEntity(food, 1))
    }

    suspend fun removeItem(cartItemEntity: CartItemEntity){
        repo.removeFromCart(cartItemEntity)
    }
}
