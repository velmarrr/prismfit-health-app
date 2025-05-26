package com.example.prismfit.diet.data.network.model

data class DishNetworkModel(
    val name: String,
    val weight: Double,
    val caloriesPer100: Double,
    val proteinPer100: Double,
    val fatPer100: Double,
    val carbsPer100: Double
)