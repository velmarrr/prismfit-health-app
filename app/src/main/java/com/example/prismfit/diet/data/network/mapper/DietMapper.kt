package com.example.prismfit.diet.data.network.mapper

import com.example.prismfit.diet.data.network.model.DishNetworkModel
import com.example.prismfit.diet.data.network.model.DishRequestNetworkModel
import com.example.prismfit.diet.data.network.model.MealNetworkModel
import com.example.prismfit.diet.data.network.model.MealRequestNetworkModel
import com.example.prismfit.diet.domain.model.Dish
import com.example.prismfit.diet.domain.model.Meal
import com.example.prismfit.diet.presentation.add_diet.model.MealInput

fun DishNetworkModel.toDomain(): Dish {
    return Dish(
        name = name,
        weight = weight,
        caloriesPer100 = caloriesPer100,
        proteinPer100 = proteinPer100,
        fatPer100 = fatPer100,
        carbsPer100 = carbsPer100
    )
}

fun Dish.toRequestDto(): DishRequestNetworkModel {
    return DishRequestNetworkModel(
        name = name,
        weight = weight,
        caloriesPer100 = caloriesPer100,
        proteinPer100 = proteinPer100,
        fatPer100 = fatPer100,
        carbsPer100 = carbsPer100
    )
}

fun MealNetworkModel.toDomain(): Meal {
    return Meal(
        id = id,
        type = type,
        date = date,
        dishes = dishes.map { it.toDomain() }
    )
}

fun MealInput.toRequestDto(): MealRequestNetworkModel {
    return MealRequestNetworkModel(
        id = id,
        type = type,
        date = date,
        dishes = dishes.map { it.toRequestDto() }
    )
}
