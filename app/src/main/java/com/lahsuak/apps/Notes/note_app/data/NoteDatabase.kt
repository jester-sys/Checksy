package com.example.note_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.note_app.model.Note
import com.lahsuak.apps.Notes.note_app.util.StringListConverter


@Database(entities = [Note::class], version = 5, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class NoteDatabase:RoomDatabase() {
    abstract fun noteDao():NoteDatabaseDao

}