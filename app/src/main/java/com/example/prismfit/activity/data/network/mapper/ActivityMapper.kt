package com.example.prismfit.activity.data.network.mapper

import com.example.prismfit.activity.data.network.model.ActivityNetworkModel
import com.example.prismfit.activity.data.network.model.ActivityRequestNetworkModel
import com.example.prismfit.activity.domain.model.Activity
import com.example.prismfit.activity.domain.model.ActivityType
import com.example.prismfit.activity.domain.model.NewActivity

fun ActivityNetworkModel.toDomain(): Activity {
    return Activity(
        id = id,
        type = ActivityType.fromString(type),
        startTime = startTime,
        endTime = endTime,
        durationSeconds = durationSeconds,
        distanceMeters = distanceMeters,
        averageMetersPerHour = averageMetersPerHour,
        route = route
    )
}

fun NewActivity.toRequestDto(): ActivityRequestNetworkModel {
    return ActivityRequestNetworkModel(
        type = type.typeName,
        startTime = startTime,
        endTime = endTime,
        durationSeconds = durationSeconds,
        distanceMeters = distanceMeters,
        route = route
    )
}