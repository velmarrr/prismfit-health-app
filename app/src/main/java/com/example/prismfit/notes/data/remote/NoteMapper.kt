package com.example.prismfit.notes.data.remote

import com.example.prismfit.notes.data.remote.dto.NoteDto
import com.example.prismfit.notes.data.remote.dto.NoteRequestDto
import com.example.prismfit.notes.domain.model.Note
import com.example.prismfit.notes.presentation.add_note.model.NoteInput

fun NoteDto.toDomain(): Note {
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