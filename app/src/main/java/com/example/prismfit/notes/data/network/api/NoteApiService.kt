package com.example.prismfit.notes.data.network.api

import com.example.prismfit.notes.data.network.model.NoteNetworkModel
import com.example.prismfit.notes.data.network.model.NoteRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApiService {

    @GET("/notes")
    suspend fun getNotes(): List<NoteNetworkModel>

    @POST("/notes")
    suspend fun saveNote(@Body request: NoteRequestDto): NoteNetworkModel

    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Path("id") id: String)
}