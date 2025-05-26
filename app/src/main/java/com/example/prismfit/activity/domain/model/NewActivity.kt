package com.example.prismfit.activity.domain.model

import java.time.Instant

data class NewActivity(
    val type: ActivityType,
    val startTime: Instant,
    val endTime: Instant,
    val durationSeconds: Long,
    val distanceMeters: Int,
    val route: List<Location>
)