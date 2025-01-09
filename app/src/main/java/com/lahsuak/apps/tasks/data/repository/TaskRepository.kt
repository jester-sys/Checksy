package com.jaixlabs.checksy.data.repository

import com.jaixlabs.checksy.model.SortOrder
import com.jaixlabs.checksy.data.model.SubTask
import com.jaixlabs.checksy.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    fun getAllTasks(
        searchQuery: String,
        sortOrder: SortOrder
    ): Flow<List<Task>>

    suspend fun getById(id: Int): Task

    suspend fun deleteAllCompletedTask()

    suspend fun deleteAllTasks()

    //subtask methods
    suspend fun insertSubTask(subTask: SubTask)

    suspend fun deleteSubTask(subTask: SubTask)

    suspend fun updateSubTask(subTask: SubTask)

    fun getAllSubTasks(
        id: Int,
        query: String,
        sortOrder: SortOrder
    ): Flow<List<SubTask>>

    suspend fun deleteAllCompletedSubTask(id: Int)

    suspend fun deleteAllSubTasks(id: Int)

    suspend fun getBySubTaskId(id: Int): SubTask
}