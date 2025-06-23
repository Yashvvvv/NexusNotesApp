package com.example.authproject.frontend.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.authproject.frontend.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNotes(notes: List<NoteEntity>)

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: String)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}
