package com.example.prismfit.activity.data.model

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class SerializableLatLng(
    val latitude: Double,
    val longitude: Double
)

fun LatLng.toSerializable() = SerializableLatLng(latitude, longitude)
fun SerializableLatLng.toLatLng() = LatLng(latitude, longitude)
