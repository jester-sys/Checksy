package com.jaixlabs.checksy.util.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jaixlabs.checksy.util.AppConstants
import com.jaixlabs.checksy.util.NotificationUtil

class ReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    override fun doWork(): Result {
        NotificationUtil.createNotification(
            context,
            inputData.getInt(AppConstants.WorkManager.ID_KEY, 0),
            inputData.getString(AppConstants.WorkManager.MESSAGE_KEY) ?: "",
            inputData.getString(AppConstants.WorkManager.PARENT_TITLE_KEY),
            inputData.getBoolean(AppConstants.WorkManager.STATUS_KEY, false),
            inputData.getLong(AppConstants.WorkManager.START_DATE_KEY, 0L),
            inputData.getLong(AppConstants.WorkManager.END_DATE_KEY, -1L),
        )
        return Result.success()
    }
}