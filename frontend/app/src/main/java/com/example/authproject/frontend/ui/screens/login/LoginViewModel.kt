package com.example.authproject.frontend.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authproject.frontend.data.local.datastore.TokenManager
import com.example.authproject.frontend.domain.repository.AuthRepository
import com.example.authproject.frontend.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<Unit>?>(null)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            val result = authRepository.login(email, password)
            if (result is Resource.Success) {
                result.data?.let {
                    tokenManager.saveTokens(it.accessToken, it.refreshToken)
                }
                _loginState.value = Resource.Success(Unit)
            } else {
                _loginState.value = Resource.Error(result.message ?: "Unknown error")
            }
        }
    }
} 