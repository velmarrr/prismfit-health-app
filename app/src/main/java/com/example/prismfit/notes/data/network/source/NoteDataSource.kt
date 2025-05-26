package com.example.prismfit.notes.data.network.source

import com.example.prismfit.notes.data.network.api.NoteApiService
import com.example.prismfit.notes.data.network.model.NoteNetworkModel
import com.example.prismfit.notes.data.network.model.NoteRequestDto
import javax.inject.Inject

class NoteDataSource @Inject constructor(
    private val apiService: NoteApiService
) {

    suspend fun getNotes(): List<NoteNetworkModel> {
        return apiService.getNotes()
    }

    suspend fun saveNote(request: NoteRequestDto): NoteNetworkModel {
        return apiService.saveNote(request)
    }

    suspend fun deleteNote(id: String) {
        apiService.deleteNote(id)
    }
}