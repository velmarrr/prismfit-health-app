package com.example.prismfit.diet.data.remote.dto

data class MealDto(
    val id: String,
    val type: String,
    val date: String,
    val dishes: List<DishDto>
)