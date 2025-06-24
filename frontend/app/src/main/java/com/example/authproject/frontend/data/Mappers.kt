package com.example.authproject.frontend.data

import com.example.authproject.frontend.data.local.NoteEntity
import com.example.authproject.frontend.data.remote.dto.NoteDto
import com.example.authproject.frontend.domain.model.Note
import java.time.Instant

fun NoteDto.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = this.id!!,
        title = this.title,
        content = this.content,
        color = this.color,
        createdAt = this.createAt?.let { Instant.parse(it) } ?: Instant.now()
    )
}

fun NoteEntity.toNote(): Note {
    return Note(
        id = this.id,
        title = this.title,
        content = this.content,
        color = this.color,
        createdAt = this.createdAt
    )
}
