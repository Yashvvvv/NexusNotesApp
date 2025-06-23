package com.example.authproject.frontend.di

import com.example.authproject.frontend.data.repository.AuthRepositoryImpl
import com.example.authproject.frontend.domain.repository.AuthRepository
import com.example.authproject.frontend.data.repository.NoteRepositoryImpl
import com.example.authproject.frontend.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ): NoteRepository
} 