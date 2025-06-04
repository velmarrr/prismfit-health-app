package com.example.prismfit.activity.data.network.model

import com.example.prismfit.activity.domain.model.Location
import com.example.prismfit.common.core.serialization.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ActivityRequestNetworkModel(
    val type: String,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    val durationSeconds: Long,
    val distanceMeters: Int,
    val route: List<Location>
)