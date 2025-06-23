package com.example.authproject.frontend.data.repository

import com.example.authproject.frontend.data.remote.api.AuthApiService
import com.example.authproject.frontend.data.remote.dto.AuthRequest
import com.example.authproject.frontend.data.remote.dto.TokenPair
import com.example.authproject.frontend.domain.repository.AuthRepository
import com.example.authproject.frontend.util.Resource
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRepository {
    override suspend fun register(email: String, password: String): Resource<Unit> {
        return try {
            api.register(AuthRequest(email, password))
            Resource.Success(Unit)
        } catch (e: HttpException) {
            val errorResponse = e.response()?.errorBody()?.string()
            val message = try {
                val json = JSONObject(errorResponse)
                json.getJSONArray("errors").getString(0)
            } catch (e: Exception) {
                e.message ?: "An unknown error occurred."
            }
            Resource.Error(message)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun login(email: String, password: String): Resource<TokenPair> {
        return try {
            val tokens = api.login(AuthRequest(email, password))
            Resource.Success(tokens)
        } catch (e: HttpException) {
            Resource.Error("Invalid email or password.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun refreshToken(token: String): Resource<TokenPair> {
        return try {
            val tokens = api.refresh(token)
            Resource.Success(tokens)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}
