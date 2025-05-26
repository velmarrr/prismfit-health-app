package com.example.prismfit.activity.data.network.api

import com.example.prismfit.activity.data.network.model.ActivityNetworkModel
import com.example.prismfit.activity.data.network.model.ActivityRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ActivityApiService {

    @GET("/activities")
    suspend fun getActivities(): List<ActivityNetworkModel>

    @POST("/activities")
    suspend fun saveActivity(@Body request: ActivityRequestDto): ActivityNetworkModel
}