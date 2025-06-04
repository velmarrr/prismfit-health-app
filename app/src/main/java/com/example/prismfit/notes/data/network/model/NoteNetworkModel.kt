package com.example.prismfit.notes.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteNetworkModel(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long
)