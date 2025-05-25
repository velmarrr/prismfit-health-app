package com.example.prismfit.diet.domain.model

data class Meal(
    val id: String,
    val type: String,
    val date: String,
    val dishes: List<Dish>
)