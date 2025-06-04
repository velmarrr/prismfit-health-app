package com.example.prismfit.notes.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequestNetworkModel(
    val id: String? = null,
    val title: String,
    val content: String
)