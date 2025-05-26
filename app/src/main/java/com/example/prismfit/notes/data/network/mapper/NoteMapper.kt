package com.example.prismfit.notes.data.network.mapper

import com.example.prismfit.notes.data.network.model.NoteNetworkModel
import com.example.prismfit.notes.data.network.model.NoteRequestDto
import com.example.prismfit.notes.domain.model.Note
import com.example.prismfit.notes.presentation.add_note.model.NoteInput

fun NoteNetworkModel.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt
    )
}

fun NoteInput.toRequestDto(): NoteRequestDto {
    return NoteRequestDto(
        id = id,
        title = title,
        content = content
    )
}