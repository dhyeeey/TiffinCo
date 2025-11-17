package org.tiffinservice.app.DTO

data class MenuItem(
    val id: Int,
    val category: String,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)
