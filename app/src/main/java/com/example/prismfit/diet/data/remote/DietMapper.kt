package com.example.prismfit.diet.data.remote

import com.example.prismfit.diet.data.remote.dto.DishDto
import com.example.prismfit.diet.data.remote.dto.DishRequestDto
import com.example.prismfit.diet.data.remote.dto.MealDto
import com.example.prismfit.diet.data.remote.dto.MealRequestDto
import com.example.prismfit.diet.domain.model.Dish
import com.example.prismfit.diet.domain.model.Meal
import com.example.prismfit.diet.presentation.add_diet.model.MealInput

fun DishDto.toDomain(): Dish {
    return Dish(
        name = name,
        weight = weight,
        caloriesPer100 = caloriesPer100,
        proteinPer100 = proteinPer100,
        fatPer100 = fatPer100,
        carbsPer100 = carbsPer100
    )
}

fun Dish.toRequestDto(): DishRequestDto {
    return DishRequestDto(
        name = name,
        weight = weight,
        caloriesPer100 = caloriesPer100,
        proteinPer100 = proteinPer100,
        fatPer100 = fatPer100,
        carbsPer100 = carbsPer100
    )
}

fun MealDto.toDomain(): Meal {
    return Meal(
        id = id,
        type = type,
        date = date,
        dishes = dishes.map { it.toDomain() }
    )
}

fun MealInput.toRequestDto(): MealRequestDto {
    return MealRequestDto(
        id = id,
        type = type,
        date = date,
        dishes = dishes.map { it.toRequestDto() }
    )
}
