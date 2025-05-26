package com.example.prismfit.diet.data.network.source

import com.example.prismfit.diet.data.network.api.DietApiService
import com.example.prismfit.diet.data.network.model.MealNetworkModel
import com.example.prismfit.diet.data.network.model.MealRequestDto
import javax.inject.Inject

class DietDataSource @Inject constructor(
    private val apiService: DietApiService
) {

    suspend fun getAllMeals(): List<MealNetworkModel> {
        return apiService.getAllMeals()
    }

    suspend fun saveMeal(request: MealRequestDto): MealNetworkModel {
        return apiService.saveMeal(request)
    }

    suspend fun deleteMeal(id: String) {
        apiService.deleteMeal(id)
    }
}