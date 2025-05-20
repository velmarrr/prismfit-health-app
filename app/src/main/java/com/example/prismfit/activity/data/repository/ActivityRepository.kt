package com.example.prismfit.activity.data.repository

import com.example.prismfit.activity.data.model.Activity
import com.example.prismfit.activity.data.model.ActivityRequest
import com.example.prismfit.activity.data.remote.ActivityApi
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val api: ActivityApi
) {

    suspend fun getActivities(): List<Activity> {
        return api.getActivities()
    }

    suspend fun saveActivity(request: ActivityRequest): Activity {
        return api.saveActivity(request)
    }
}