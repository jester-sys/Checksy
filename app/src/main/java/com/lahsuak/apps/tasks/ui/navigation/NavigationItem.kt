package com.jaixlabs.checksy.ui.navigation

import com.jaixlabs.checksy.util.NavigationConstants.Screen.NOTIFICATION
import com.jaixlabs.checksy.util.NavigationConstants.Screen.OVERVIEW
import com.jaixlabs.checksy.util.NavigationConstants.Screen.SETTING
import com.jaixlabs.checksy.util.NavigationConstants.Screen.SUBTASK
import com.jaixlabs.checksy.util.NavigationConstants.Screen.TASK

sealed class NavigationItem(val route: String) {
    object Task : NavigationItem(TASK)
    object SubTask : NavigationItem(SUBTASK)
    object Overview : NavigationItem(OVERVIEW)
    object Setting : NavigationItem(SETTING)
    object Notification : NavigationItem(NOTIFICATION)

    object AddNotes : NavigationItem(NOTIFICATION)

}