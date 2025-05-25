package com.example.prismfit.diet.domain.repository

import com.example.prismfit.diet.domain.model.Meal
import com.example.prismfit.diet.data.remote.DietApi
import com.example.prismfit.diet.data.remote.toDomain
import com.example.prismfit.diet.data.remote.toRequestDto
import com.example.prismfit.diet.presentation.add_diet.model.MealInput
import javax.inject.Inject

class DietRepository @Inject constructor(
    private val api: DietApi
) {
    suspend fun getAllMeals(): List<Meal> {
        return api.getAllMeals()
            .map { it.toDomain() }
    }

    suspend fun saveMeal(input: MealInput): Meal {
        return api.saveMeal(input.toRequestDto()).toDomain()
    }

    suspend fun deleteMeal(id: String) {
        return api.deleteMeal(id)
    }
}