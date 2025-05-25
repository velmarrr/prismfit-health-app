package com.example.prismfit.diet.data.remote

import com.example.prismfit.diet.data.remote.dto.MealDto
import com.example.prismfit.diet.data.remote.dto.MealRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DietApi {

    @GET("/meals/all")
    suspend fun getAllMeals(): List<MealDto>

    @POST("/meals")
    suspend fun saveMeal(@Body request: MealRequestDto): MealDto

    @DELETE("/meals/{id}")
    suspend fun deleteMeal(@Path("id") id: String)
}