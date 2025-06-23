package com.example.authproject.frontend.data.remote.api

import com.example.authproject.frontend.data.remote.dto.AuthRequest
import com.example.authproject.frontend.data.remote.dto.TokenPair
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/auth/register")
    suspend fun register(@Body body: AuthRequest)

    @POST("/auth/login")
    suspend fun login(@Body body: AuthRequest): TokenPair

    @POST("/auth/refresh")
    suspend fun refresh(@Body refreshToken: String): TokenPair
} 