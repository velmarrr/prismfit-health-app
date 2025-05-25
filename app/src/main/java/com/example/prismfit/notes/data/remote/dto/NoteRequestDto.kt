package com.example.prismfit.notes.data.remote.dto

data class NoteRequestDto(
    val id: String? = null,
    val title: String,
    val content: String
)