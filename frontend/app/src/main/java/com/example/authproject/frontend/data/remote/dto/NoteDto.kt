package com.example.authproject.frontend.data.remote.dto

import java.time.Instant

data class NoteDto(
    val id: String? = null,
    val title: String,
    val content: String,
    val color: Long,
    val createAt: String? = null
)

data class NoteRequest(
    val id: String?,
    val title: String,
    val content: String,
    val color: Long
) 