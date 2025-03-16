package com.jaixlabs.checksy.util

object NavigationConstants {
    object Screen {
        const val TASK = "task"
        const val SUBTASK = "subtask"
        const val OVERVIEW = "overview"
        const val SETTING = "setting"
        const val NOTIFICATION = "notification"
        const val ADD_NOTES = "add_notes"
    }

    object Key {
        const val TASK_ID = "task_id"
        const val HAS_NOTIFICATION = "has_notification"

        const val SUBTASK_DEEP_LINK = "tasks://com.jaixlabs.checksy/subtaskscreen/{task_id}/{has_notification}"

        const val ADD_UPDATE_TASK_DEEP_LINK = "tasks://com.jaixlabs.checksy/edittask/true"
    }

}