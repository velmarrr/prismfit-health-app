package com.example.prismfit.diet.data.remote.dto

data class MealRequestDto(
    val id: String? = null,
    val type: String,
    val date: String? = null,
    val dishes: List<DishRequestDto>
)