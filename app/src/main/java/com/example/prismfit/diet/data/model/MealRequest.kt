package com.example.prismfit.diet.data.model

data class MealRequest(
    val id: String?,
    val type: String,
    val dishes: List<Dish>,
    val date: String? = null
)