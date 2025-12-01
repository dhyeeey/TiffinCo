package org.tiffinservice.app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tiffinservice.app.database.FoodEntity
import org.tiffinservice.app.repository.CartItemEntity
import org.tiffinservice.app.repository.TiffinRepository
import org.tiffinservice.app.sample.SampleDataProvider

class TiffinViewModel(
    private val repo: TiffinRepository
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)

    val restaurants = repo.restaurants
    val foods = mutableStateOf<List<FoodEntity>>(emptyList())
    val cart = repo.cart
    val isLoading = mutableStateOf(true)

    fun loadSampleData() = scope.launch{
        if(isLoading.value){
            SampleDataProvider.insertAllSampleData(repo)
            isLoading.value = false
        }
    }

    fun loadFoodsForRestaurant(id: Long) {
        foods.value = repo.getFoodsByRestaurant(id)
    }

    fun addItem(cartItemEntity: CartItemEntity) = scope.launch {
        repo.addToCart(cartItemEntity)
    }

    fun removeItem(cartItemEntity: CartItemEntity)=scope.launch {
        repo.removeFromCart(cartItemEntity)
    }

    fun addItem(food: FoodEntity)=scope.launch {
        repo.addToCart(CartItemEntity(food, 1))
    }

    fun removeItem(food: FoodEntity)=scope.launch {
        repo.removeFromCart(CartItemEntity(food, 1))
    }


}
