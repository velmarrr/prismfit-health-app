package com.example.prismfit.activity.data.remote

import com.example.prismfit.activity.data.remote.dto.ActivityDto
import com.example.prismfit.activity.data.remote.dto.ActivityRequestDto
import com.example.prismfit.activity.domain.model.Activity
import com.example.prismfit.activity.domain.model.ActivityType
import com.example.prismfit.activity.domain.model.NewActivity

fun ActivityDto.toDomain(): Activity {
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

fun NewActivity.toRequestDto(): ActivityRequestDto {
    return ActivityRequestDto(
        type = type.typeName,
        startTime = startTime,
        endTime = endTime,
        durationSeconds = durationSeconds,
        distanceMeters = distanceMeters,
        route = route
    )
}