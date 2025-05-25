package com.example.prismfit.activity.data.remote.dto

import com.example.prismfit.activity.domain.model.SerializableLatLng
import java.time.Instant

data class ActivityRequestDto(
    val type: String,
    val startTime: Instant,
    val endTime: Instant,
    val durationSeconds: Long,
    val distanceMeters: Int,
    val route: List<SerializableLatLng>
)