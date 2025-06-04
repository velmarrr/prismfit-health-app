package com.example.prismfit.diet.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class MealNetworkModel(
    val id: String,
    val type: String,
    val date: String,
    val dishes: List<DishNetworkModel>
)