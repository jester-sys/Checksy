package com.example.note_app.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

//@Parcelize
//@Entity(tableName = "Note_table")
//data class Note(
//    @PrimaryKey
//    @ColumnInfo(name = "note_id")
//    val id: String,
//
//    @ColumnInfo(name = "note_title")
//    val title: String,
//
//    @ColumnInfo(name = "note_description")
//    val note: String,
//
//    @ColumnInfo(name = "note_image")
//    val imageList: List<String>?,
//
//    @ColumnInfo(name = "note_time")
//    val timeStamp: String,
//
//    // ✅ Add `category` field to fix sorting issue
//    @ColumnInfo(name = "note_category")
//    val category: String = "General",
//
//    // ✅ New Columns for Lock Feature
//    @ColumnInfo(name = "isLocked")
//    val isLocked: Boolean = false,
//
//    @ColumnInfo(name = "password")
//    val password: String? = null
//
//) : Parcelable

@Parcelize
@Entity(tableName = "Note_table")
data class Note(
    @PrimaryKey
    @ColumnInfo(name = "note_id")
    val id: String,

    @ColumnInfo(name = "note_title")
    val title: String,

    @ColumnInfo(name = "note_description")
    val note: String,

    @ColumnInfo(name = "note_image")
    val imageList: List<String>?,

    @ColumnInfo(name = "note_time")
    val timeStamp: String,

    @ColumnInfo(name = "note_category")
    val category: String = "General",

    @ColumnInfo(name = "isLocked")
    val isLocked: Boolean = false,

    @ColumnInfo(name = "password")
    val password: String? = null,

    // ✅ New Fields for Customization
    @ColumnInfo(name = "is_bold")
    val isBold: Boolean = false,

    @ColumnInfo(name = "text_size")
    val textSize: Float = 16f,

    @ColumnInfo(name = "background_color")
    @ColorInt val backgroundColor: Int = 0xFFFFF8E1.toInt(),

    @ColumnInfo(name = "selected_font")
    val selectedFont: String = "Default"

) : Parcelable {
    // ✅ Convert textSize from Float to TextUnit (sp)
    fun getTextSize(): TextUnit {
        return textSize.sp
    }

}