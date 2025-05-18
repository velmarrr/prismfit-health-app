package com.example.prismfit.activity.data.remote

import com.example.prismfit.activity.data.model.Activity
import com.example.prismfit.activity.data.model.ActivityRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ActivityApi {

    @GET("/activities")
    suspend fun getActivities(): List<Activity>

    @POST("/activities")
    suspend fun saveActivity(@Body request: ActivityRequest): Activity
}