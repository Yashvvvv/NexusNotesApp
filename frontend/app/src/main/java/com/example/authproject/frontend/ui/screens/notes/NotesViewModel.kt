package com.example.authproject.frontend.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authproject.frontend.data.local.datastore.TokenManager
import com.example.authproject.frontend.data.repository.NoteRepositoryImpl
import com.example.authproject.frontend.domain.model.Note
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
    val error: String? = null
)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepositoryImpl, // Use Impl to access sync
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state = _state.asStateFlow()

    private val _createNoteState = MutableStateFlow<Resource<Unit>?>(null)
    val createNoteState: StateFlow<Resource<Unit>?> = _createNoteState

    init {
        getNotes()
    }

    fun getNotes() {
        viewModelScope.launch {
            noteRepository.syncNotes()
        }
        noteRepository.getNotes().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        notes = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun syncNotes() {
        viewModelScope.launch {
            noteRepository.syncNotes()
        }
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
            if(result is Resource.Success){
                noteRepository.syncNotes()
            }
        }
    }

    fun onLogout(onLoggedOut: () -> Unit) {
        // ... existing code ...
    }
} 