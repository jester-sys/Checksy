package com.jaixlabs.checksy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jaixlabs.checksy.util.AppConstants.NOTIFICATION_TABLE

@Entity(tableName = NOTIFICATION_TABLE)
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val taskId: Int,
    val title: String,
    val date: Long
)