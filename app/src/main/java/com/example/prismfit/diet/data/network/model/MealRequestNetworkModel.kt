package com.example.prismfit.diet.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class MealRequestNetworkModel(
    val id: String? = null,
    val type: String,
    val date: String? = null,
    val dishes: List<DishRequestNetworkModel>
)