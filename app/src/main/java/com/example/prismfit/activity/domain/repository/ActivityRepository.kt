package com.example.prismfit.activity.domain.repository

import com.example.prismfit.activity.domain.model.Activity
import com.example.prismfit.activity.data.remote.ActivityApi
import com.example.prismfit.activity.data.remote.toDomain
import com.example.prismfit.activity.data.remote.toRequestDto
import com.example.prismfit.activity.domain.model.NewActivity
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val api: ActivityApi
) {

    suspend fun getActivities(): List<Activity> {
        return api.getActivities().map { it.toDomain() }
    }

    suspend fun saveActivity(newActivity: NewActivity): Activity {
        return api.saveActivity(newActivity.toRequestDto()).toDomain()
    }
}