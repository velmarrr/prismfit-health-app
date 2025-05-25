package com.example.prismfit.diet.presentation.add_diet.model

import com.example.prismfit.diet.domain.model.Dish

data class MealInput(
    val id: String? = null,
    val type: String,
    val date: String? = null,
    val dishes: List<Dish>
)