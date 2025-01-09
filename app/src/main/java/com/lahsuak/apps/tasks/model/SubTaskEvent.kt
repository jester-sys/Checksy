package com.jaixlabs.checksy.model

import com.jaixlabs.checksy.data.model.SubTask

sealed class SubTaskEvent {
    object Initial : SubTaskEvent()
    data class ShowUndoDeleteTaskMessage(val subTask: SubTask) : SubTaskEvent()
    object NavigateToAllCompletedScreen : SubTaskEvent()
}

