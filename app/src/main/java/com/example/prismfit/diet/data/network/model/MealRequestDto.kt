package com.example.prismfit.diet.data.network.model

data class MealRequestDto(
    val id: String? = null,
    val type: String,
    val date: String? = null,
    val dishes: List<DishRequestDto>
)