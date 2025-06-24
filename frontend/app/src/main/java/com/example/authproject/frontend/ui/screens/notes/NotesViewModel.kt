package com.example.authproject.frontend.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authproject.frontend.data.local.datastore.TokenManager
import com.example.authproject.frontend.domain.model.Note
import com.example.authproject.frontend.domain.repository.NoteRepository
import com.example.authproject.frontend.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotesState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<Note>? = null,
)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> = _state.asStateFlow()

    private val _createNoteState = MutableStateFlow<Resource<Unit>?>(null)
    val createNoteState: StateFlow<Resource<Unit>?> = _createNoteState

    init {
        getNotes()
    }

    private fun getNotes() {
        noteRepository.getNotes().onEach { result ->
            _state.value = when (result) {
                is Resource.Success -> {
                    state.value.copy(
                        notes = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    state.value.copy(
                        error = result.message,
                        isLoading = false,
                        notes = result.data ?: emptyList()
                    )
                }
                is Resource.Loading -> {
                    state.value.copy(
                        isLoading = true,
                        notes = result.data ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearTokens()
        }
    }

    fun createNote(title: String, content: String) {
        viewModelScope.launch {
            _createNoteState.value = Resource.Loading()
            val result = noteRepository.createNote(title, content)
            _createNoteState.value = result
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }

    fun onLogout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            tokenManager.clearTokens()
            onLoggedOut()
        }
    }
} 