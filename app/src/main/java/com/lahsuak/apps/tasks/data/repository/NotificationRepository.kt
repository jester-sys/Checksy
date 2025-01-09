package com.jaixlabs.checksy.data.repository

import com.jaixlabs.checksy.data.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun insertNotification(notification: Notification)

    suspend fun deleteNotification(notification: Notification)

    fun getAllNotifications(
        isAscOrder: Boolean,
    ): Flow<List<Notification>>

    suspend fun getById(id: Int): Notification

    suspend fun deleteAllNotifications()
}