package com.example.authproject.frontend.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authproject.frontend.domain.repository.AuthRepository
import com.example.authproject.frontend.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<Resource<Unit>?>(null)
    val registerState = _registerState.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading()
            val result = authRepository.register(email, password)
            if (result is Resource.Success) {
                _registerState.value = Resource.Success(Unit)
            } else {
                _registerState.value = Resource.Error(result.message ?: "Unknown error")
            }
        }
    }
} 