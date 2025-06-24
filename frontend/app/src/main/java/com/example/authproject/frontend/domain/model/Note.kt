package com.example.authproject.frontend.domain.model

import java.time.Instant

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant
) 