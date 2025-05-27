package com.example.prismfit.diet.data.network.model

data class MealRequestNetworkModel(
    val id: String? = null,
    val type: String,
    val date: String? = null,
    val dishes: List<DishRequestNetworkModel>
)