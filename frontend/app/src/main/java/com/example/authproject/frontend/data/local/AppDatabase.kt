package com.example.authproject.frontend.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.authproject.frontend.data.local.dao.NoteDao

@Database(
    entities = [NoteEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
} 