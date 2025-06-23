package com.example.authproject.frontend.data.remote.dto

data class AuthRequest(
    val email: String,
    val password: String
)

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
) 