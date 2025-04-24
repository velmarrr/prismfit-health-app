package com.example.prismfit.notes.data.repository

import com.example.prismfit.notes.data.model.Note
import com.example.prismfit.notes.data.model.NoteRequest
import com.example.prismfit.notes.data.remote.NoteApi
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val api: NoteApi
) {

    suspend fun getNotes(): List<Note> = api.getNotes()

    suspend fun saveNote(request: NoteRequest): Note {
        return api.saveNote(request)
    }

    suspend fun deleteNote(id: String) {
        api.deleteNote(id)
    }
}