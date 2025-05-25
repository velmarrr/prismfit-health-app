package com.example.prismfit.notes.data.remote

import com.example.prismfit.notes.data.remote.dto.NoteDto
import com.example.prismfit.notes.data.remote.dto.NoteRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApi {

    @GET("/notes")
    suspend fun getNotes(): List<NoteDto>

    @POST("/notes")
    suspend fun saveNote(@Body request: NoteRequestDto): NoteDto

    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Path("id") id: String)
}