package com.example.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.data.local.AppDatabase
import com.example.compose.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val database: AppDatabase
) : ViewModel() {

    private val taskDao = database.taskDao()

    val tasks: StateFlow<List<TaskEntity>> = taskDao.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(title: String, dueDate: Long?) {
        viewModelScope.launch {
            val task = TaskEntity(title = title, dueDate = dueDate)
            taskDao.insertTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }

    fun updateTask(task: TaskEntity, newText: String) {
        viewModelScope.launch {
            taskDao.updateTask(task.copy(title = newText))
        }
    }

    fun toggleTaskCompletion(task: TaskEntity, isCompleted: Boolean) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = isCompleted)
            taskDao.updateTask(updatedTask)
        }
    }

    fun restoreTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            taskDao.deleteAll()
        }
    }

    fun deleteTaskByIndex(index: Int) {
        val currentList = tasks.value
        if (index in currentList.indices) {
            deleteTask(currentList[index])
        }
    }
}