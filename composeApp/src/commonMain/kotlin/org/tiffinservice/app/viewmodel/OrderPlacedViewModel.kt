package org.tiffinservice.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tiffinservice.app.database.OrderWithDetails
import org.tiffinservice.app.repository.TiffinRepository

class OrderPlacedViewModel(
    private val repo: TiffinRepository
) : ViewModel() {

    private val _order = MutableStateFlow<OrderWithDetails?>(null)
    val order = _order.asStateFlow()

    suspend fun load(orderId: Long) {
        val result = repo.getOrderDetails(orderId)
        _order.value = result
    }
}
