package org.tiffinservice.app.viewmodel

import kotlinx.coroutines.flow.StateFlow
import org.tiffinservice.app.DTO.MenuItem
import org.tiffinservice.app.repository.MenuRepository

class MenuViewModel(
    private val repo: MenuRepository
) {
    val menuItems: StateFlow<List<MenuItem>> = repo.menuItems
    fun getItemById(id: Int): MenuItem? = repo.getItemById(id)
}