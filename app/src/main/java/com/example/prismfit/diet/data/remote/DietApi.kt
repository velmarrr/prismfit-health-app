package com.example.prismfit.diet.data.remote

import com.example.prismfit.diet.data.model.Meal
import com.example.prismfit.diet.data.model.MealRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DietApi {

    @GET("/meals/all")
    suspend fun getAllMeals(): List<Meal>

    @POST("/meals")
    suspend fun saveMeal(@Body request: MealRequest): Meal

    @DELETE("/meals/{id}")
    suspend fun deleteMeal(@Path("id") id: String)
}