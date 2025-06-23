package com.example.authproject.frontend.domain.repository

import com.example.authproject.frontend.data.remote.dto.TokenPair
import com.example.authproject.frontend.util.Resource

interface AuthRepository {
    suspend fun register(email: String, password: String): Resource<Unit>
    suspend fun login(email: String, password: String): Resource<TokenPair>
    suspend fun refreshToken(token: String): Resource<TokenPair>
}
