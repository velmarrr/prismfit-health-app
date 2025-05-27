package com.example.prismfit.activity.data.network.model

import com.example.prismfit.activity.domain.model.Location
import java.time.Instant

data class ActivityRequestNetworkModel(
    val type: String,
    val startTime: Instant,
    val endTime: Instant,
    val durationSeconds: Long,
    val distanceMeters: Int,
    val route: List<Location>
)