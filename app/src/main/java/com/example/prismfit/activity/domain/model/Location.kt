package com.example.prismfit.activity.domain.model

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)

fun LatLng.toSerializable() = Location(latitude, longitude)
fun Location.toLatLng() = LatLng(latitude, longitude)
