package com.example.authproject.frontend.di

import android.app.Application
import androidx.room.Room
import com.example.authproject.frontend.data.local.AppDatabase
import com.example.authproject.frontend.data.local.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "app_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: AppDatabase): NoteDao {
        return db.noteDao()
    }
} 