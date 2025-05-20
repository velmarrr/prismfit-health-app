package com.example.prismfit.activity.data.model

import java.time.Instant

data class Activity(
    val id: String,
    val type: String,
    val startTime: Instant,
    val endTime: Instant,
    val durationSeconds: Long,
    val distanceMeters: Int,
    val averageMetersPerHour: Int,
    val route: List<SerializableLatLng>
)