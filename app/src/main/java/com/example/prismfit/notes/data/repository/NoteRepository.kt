package com.example.prismfit.notes.data.repository

import com.example.prismfit.notes.domain.model.Note
import com.example.prismfit.notes.data.network.mapper.toDomain
import com.example.prismfit.notes.data.network.mapper.toRequestDto
import com.example.prismfit.notes.data.network.source.NoteDataSource
import com.example.prismfit.notes.presentation.add_note.model.NoteInput
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val dataSource: NoteDataSource
) {

    suspend fun getNotes(): List<Note> {
        return dataSource.getNotes().map { it.toDomain() }
    }

    suspend fun saveNote(input: NoteInput): Note {
        return dataSource.saveNote(input.toRequestDto()).toDomain()
    }

    suspend fun deleteNote(id: String) {
        dataSource.deleteNote(id)
    }
}