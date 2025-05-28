package com.example.prismfit.activity.data.repository

import com.example.prismfit.activity.domain.model.Activity
import com.example.prismfit.activity.data.network.source.ActivityDataSource
import com.example.prismfit.activity.data.network.mapper.toDomain
import com.example.prismfit.activity.data.network.mapper.toRequestDto
import com.example.prismfit.activity.domain.model.NewActivity
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val dataSource: ActivityDataSource
) {

    suspend fun getActivities(): List<Activity> {
        return dataSource.getActivities().map { it.toDomain() }
    }

    suspend fun saveActivity(newActivity: NewActivity): Activity {
        return dataSource.saveActivity(newActivity.toRequestDto()).toDomain()
    }
}