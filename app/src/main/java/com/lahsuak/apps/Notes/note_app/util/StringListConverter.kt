package com.lahsuak.apps.Notes.note_app.util

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromStringList(stringList: List<String>): String {
        return stringList.joinToString(",")
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return data.split(",")
    }
}