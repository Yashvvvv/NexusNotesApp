package com.example.authproject.frontend.data.repository

import android.util.Log
import com.example.authproject.frontend.data.local.dao.NoteDao
import com.example.authproject.frontend.data.remote.api.NoteApiService
import com.example.authproject.frontend.data.remote.dto.NoteRequest
import com.example.authproject.frontend.data.toNote
import com.example.authproject.frontend.data.toNoteEntity
import com.example.authproject.frontend.domain.model.Note
import com.example.authproject.frontend.domain.repository.NoteRepository
import com.example.authproject.frontend.util.Resource
import com.example.authproject.frontend.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

class NoteRepositoryImpl @Inject constructor(
    private val noteApi: NoteApiService,
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<Resource<List<Note>>> = networkBoundResource(
        query = {
            noteDao.getNotes().map { notes -> notes.map { it.toNote() } }
        },
        fetch = {
            noteApi.getNotes()
        },
        saveFetchResult = { remoteNotes ->
            val filteredNotes = remoteNotes.filter { it.id != null }
            noteDao.deleteAll()
            noteDao.upsertNotes(filteredNotes.map { it.toNoteEntity() })
        }
    )

    override suspend fun createNote(title: String, content: String): Resource<Unit> {
        return try {
            val newNoteRequest = NoteRequest(
                id = null,
                title = title,
                content = content,
                color = Random.nextLong()
            )
            val newNote = noteApi.createNote(newNoteRequest)
            noteDao.upsertNotes(listOf(newNote.toNoteEntity()))
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "An unknown error occurred")
        } catch (e: IOException) {
            Resource.Error("Couldn't reach server. Check your internet connection.")
        }
    }

    override suspend fun deleteNote(noteId: String): Resource<Unit> {
        return try {
            noteApi.deleteNote(noteId)
            noteDao.deleteNote(noteId)
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "An unknown error occurred")
        } catch (e: IOException) {
            Resource.Error("Couldn't reach server. Check your internet connection.")
        }
    }
}