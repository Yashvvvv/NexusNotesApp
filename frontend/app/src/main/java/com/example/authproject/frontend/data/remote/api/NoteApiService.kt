package com.example.authproject.frontend.data.remote.api

import com.example.authproject.frontend.data.remote.dto.NoteDto
import com.example.authproject.frontend.data.remote.dto.NoteRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApiService {

    @POST("/notes")
    suspend fun createNote(@Body note: NoteRequest): NoteDto

    @GET("/notes")
    suspend fun getNotes(): List<NoteDto>

    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Path("id") id: String)
}