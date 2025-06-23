package com.example.authproject.frontend.data.remote

import com.example.authproject.frontend.data.local.datastore.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = runBlocking { tokenManager.getAccessToken().first() }

        val newRequest = if (accessToken != null && !request.url.encodedPath.contains("auth")) {
            request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            request
        }
        return chain.proceed(newRequest)
    }
} 