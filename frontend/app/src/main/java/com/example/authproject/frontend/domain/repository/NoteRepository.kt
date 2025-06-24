package com.example.authproject.frontend.domain.repository

import com.example.authproject.frontend.domain.model.Note
import com.example.authproject.frontend.util.Resource
import kotlinx.coroutines.flow.Flow
 
interface NoteRepository {
    fun getNotes(): Flow<Resource<List<Note>>>
    suspend fun createNote(title: String, content: String): Resource<Unit>
    suspend fun deleteNote(noteId: String): Resource<Unit>
} 