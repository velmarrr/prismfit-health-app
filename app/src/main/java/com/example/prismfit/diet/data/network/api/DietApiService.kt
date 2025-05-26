package com.example.prismfit.diet.data.network.api

import com.example.prismfit.diet.data.network.model.MealNetworkModel
import com.example.prismfit.diet.data.network.model.MealRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DietApiService {

    @GET("/meals/all")
    suspend fun getAllMeals(): List<MealNetworkModel>

    @POST("/meals")
    suspend fun saveMeal(@Body request: MealRequestDto): MealNetworkModel

    @DELETE("/meals/{id}")
    suspend fun deleteMeal(@Path("id") id: String)
}