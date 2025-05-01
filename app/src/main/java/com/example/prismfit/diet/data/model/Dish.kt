package com.example.prismfit.diet.data.model

data class Dish(
    val name: String,
    val weight: Double,
    val caloriesPer100: Double,
    val proteinPer100: Double,
    val fatPer100: Double,
    val carbsPer100: Double
) {
    val totalCalories: Double get() = (caloriesPer100 * weight) / 100
    val totalProtein: Double get() = (proteinPer100 * weight) / 100
    val totalFat: Double get() = (fatPer100 * weight) / 100
    val totalCarbs: Double get() = (carbsPer100 * weight) / 100
}