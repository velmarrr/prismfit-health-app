package com.example.prismfit.activity.data.network.source

import com.example.prismfit.activity.data.network.api.ActivityApiService
import com.example.prismfit.activity.data.network.model.ActivityNetworkModel
import com.example.prismfit.activity.data.network.model.ActivityRequestNetworkModel
import javax.inject.Inject

class ActivityDataSource @Inject constructor(
    private val apiService: ActivityApiService
) {

    suspend fun getActivities(): List<ActivityNetworkModel> {
        return apiService.getActivities()
    }

    suspend fun saveActivity(request: ActivityRequestNetworkModel): ActivityNetworkModel {
        return apiService.saveActivity(request)
    }
}