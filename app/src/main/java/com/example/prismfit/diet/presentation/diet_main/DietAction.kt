package com.example.prismfit.diet.presentation.diet_main

sealed interface DietAction {
    data class DeleteRequest(val mealId: String) : DietAction
    data object DeleteConfirm : DietAction
    data object DeleteCancel : DietAction
    data class MealClick(val mealId: String) : DietAction
}
