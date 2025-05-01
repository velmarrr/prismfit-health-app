package com.example.prismfit.diet.presentation.add_diet

import com.example.prismfit.diet.data.model.Dish

data class AddDietScreenUiState(
    val mealType: String = "",
    val dishes: List<Dish> = emptyList(),
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val dishName: String = "",
    val dishWeight: String = "",
    val dishCalories: String = "",
    val dishProtein: String = "",
    val dishFat: String = "",
    val dishCarbs: String = "",
    val date: String? = null
)