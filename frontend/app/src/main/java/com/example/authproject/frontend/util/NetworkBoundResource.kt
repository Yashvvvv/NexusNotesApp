package com.example.authproject.frontend.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException


inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            val fetchedData = fetch()
            saveFetchResult(fetchedData)
            query().map { Resource.Success(it) }
        } catch (e: HttpException) {
            query().map { Resource.Error("HTTP Error: ${e.code()}", it) }
        } catch (e: IOException) {
            query().map { Resource.Error("Network Error", it) }
        } catch (e: Exception) {
            query().map { Resource.Error(e.message ?: "An unknown error occurred", it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
} 