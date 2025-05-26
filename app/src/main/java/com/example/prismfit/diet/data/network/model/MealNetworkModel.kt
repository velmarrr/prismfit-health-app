package com.example.prismfit.diet.data.network.model

data class MealNetworkModel(
    val id: String,
    val type: String,
    val date: String,
    val dishes: List<DishNetworkModel>
)