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
        createdAt = this.createdAt ?: Instant.now()
    )
}

fun NoteEntity.toNote(): Note {
    return Note(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt
    )
}
