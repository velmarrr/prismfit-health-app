package com.example.prismfit.diet.data.repository

import com.example.prismfit.diet.domain.model.Meal
import com.example.prismfit.diet.data.network.mapper.toDomain
import com.example.prismfit.diet.data.network.mapper.toRequestDto
import com.example.prismfit.diet.data.network.source.DietDataSource
import com.example.prismfit.diet.presentation.add_diet.model.MealInput
import javax.inject.Inject

class DietRepository @Inject constructor(
    private val dataSource: DietDataSource
) {

    suspend fun getAllMeals(): List<Meal> {
        return dataSource.getAllMeals().map { it.toDomain() }
    }

    suspend fun saveMeal(input: MealInput): Meal {
        return dataSource.saveMeal(input.toRequestDto()).toDomain()
    }

    suspend fun deleteMeal(id: String) {
        dataSource.deleteMeal(id)
    }
}