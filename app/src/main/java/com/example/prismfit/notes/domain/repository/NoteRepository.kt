package com.example.prismfit.notes.domain.repository

import com.example.prismfit.notes.domain.model.Note
import com.example.prismfit.notes.data.remote.NoteApi
import com.example.prismfit.notes.data.remote.toDomain
import com.example.prismfit.notes.data.remote.toRequestDto
import com.example.prismfit.notes.presentation.add_note.model.NoteInput
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val api: NoteApi
) {

    suspend fun getNotes(): List<Note> {
        return api.getNotes()
            .map { it.toDomain() }
    }

    suspend fun saveNote(input: NoteInput): Note {
        return api.saveNote(input.toRequestDto()).toDomain()
    }

    suspend fun deleteNote(id: String) {
        api.deleteNote(id)
    }
}