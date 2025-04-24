package com.example.prismfit.notes.data.remote

import com.example.prismfit.notes.data.model.Note
import com.example.prismfit.notes.data.model.NoteRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApi {

    @GET("/notes")
    suspend fun getNotes(): List<Note>

    @POST("/notes")
    suspend fun saveNote(@Body request: NoteRequest): Note

    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Path("id") id: String)
}