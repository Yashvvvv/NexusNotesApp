package com.example.authproject.frontend.data.repository

import android.util.Log
import com.example.authproject.frontend.data.local.dao.NoteDao
import com.example.authproject.frontend.data.remote.api.NoteApiService
import com.example.authproject.frontend.data.remote.dto.NoteDto
import com.example.authproject.frontend.data.toNote
import com.example.authproject.frontend.data.toNoteEntity
import com.example.authproject.frontend.domain.model.Note
import com.example.authproject.frontend.domain.repository.NoteRepository
import com.example.authproject.frontend.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteApi: NoteApiService,
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<Resource<List<Note>>> {
        return noteDao.getNotes().map { notes ->
            Resource.Success(notes.map { it.toNote() })
        }
    }

    override suspend fun syncNotes() {
        try {
            val remoteNotes = noteApi.getNotes()
                .filter { it.id != null }
            noteDao.deleteAll()
            noteDao.upsertNotes(remoteNotes.map { it.toNoteEntity() })
        } catch (e: Exception) {
            Log.e("NoteRepository", "Couldn't sync notes", e)
        }
    }

    override suspend fun createNote(title: String, content: String): Resource<Unit> {
        return try {
            val noteDto = NoteDto(title = title, content = content)
            noteApi.createNote(noteDto)
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "An unknown error occurred")
        } catch (e: IOException) {
            Resource.Error("Couldn't reach server. Check your internet connection.")
        }
    }
}