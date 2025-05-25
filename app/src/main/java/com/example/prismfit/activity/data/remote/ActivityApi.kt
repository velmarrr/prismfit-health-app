package com.example.prismfit.activity.data.remote

import com.example.prismfit.activity.data.remote.dto.ActivityDto
import com.example.prismfit.activity.data.remote.dto.ActivityRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ActivityApi {

    @GET("/activities")
    suspend fun getActivities(): List<ActivityDto>

    @POST("/activities")
    suspend fun saveActivity(@Body request: ActivityRequestDto): ActivityDto
}