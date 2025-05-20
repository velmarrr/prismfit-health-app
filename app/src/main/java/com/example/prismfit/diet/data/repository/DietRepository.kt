package com.example.prismfit.diet.data.repository

import com.example.prismfit.diet.data.model.Meal
import com.example.prismfit.diet.data.model.MealRequest
import com.example.prismfit.diet.data.remote.DietApi
import javax.inject.Inject

class DietRepository @Inject constructor(
    private val api: DietApi
) {
    suspend fun getAllMeals(): List<Meal> {
        return api.getAllMeals()
    }

    suspend fun saveMeal(request: MealRequest): Meal {
        return api.saveMeal(request)
    }

    suspend fun deleteMeal(id: String) {
        return api.deleteMeal(id)
    }
}