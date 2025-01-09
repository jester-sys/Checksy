package com.jaixlabs.checksy.model

import com.jaixlabs.checksy.data.model.Task

sealed class TaskEvent {
    object Initial:TaskEvent()
    data class ShowUndoDeleteTaskMessage(val task: Task) : TaskEvent()
    object NavigateToAllCompletedScreen : TaskEvent()
}