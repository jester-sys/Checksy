package com.jaixlabs.checksy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jaixlabs.checksy.data.model.Notification
import com.jaixlabs.checksy.data.model.SubTask
import com.jaixlabs.checksy.data.model.Task

@Database(
    entities = [Task::class, SubTask::class, Notification::class],
    version = 6
)
abstract class TaskDatabase : RoomDatabase() {
    abstract val dao: TaskDao
    abstract val notificationDao: NotificationDao
}
