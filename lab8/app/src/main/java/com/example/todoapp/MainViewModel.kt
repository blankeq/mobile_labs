package com.example.todoapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class Task(
    val text: String,
    val isChecked: Int = 0
)

class MainViewModel : ViewModel() {

    // Приватный изменяемый StateFlow с начальным значением (пустой список)
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    // Публичный неизменяемый StateFlow для подписки из UI
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    // Добавление новой задачи
    fun addTask(task: Task) {
        val currentList = _tasks.value.toMutableList()
        currentList.add(task)
        _tasks.value = currentList
    }

    // Удаление задачи по индексу
    fun deleteTask(index: Int) {
        val currentList = _tasks.value.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _tasks.value = currentList
        }
    }

    // Обновление текста задачи
    fun updateTask(index: Int, newText: String) {
        val currentList = _tasks.value.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = Task(newText)
            _tasks.value = currentList
        }
    }

    fun restoreTask(index: Int, task: Task) {
        val currentList = _tasks.value.toMutableList()

        val safeIndex = index.coerceIn(0, currentList.size)
        currentList.add(safeIndex, task)
        _tasks.value = currentList
    }

    fun toggleTask(index: Int, isChecked: Boolean) {
        val currentList = _tasks.value.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = currentList[index].copy(isChecked = if (isChecked) 1 else 0)
            _tasks.value = currentList
        }
    }

    fun clearTasks() {
        val currentList = _tasks.value.toMutableList()
        currentList.clear()
        _tasks.value = currentList
    }

    fun addAllTasks(tasks: List<Task>) {
        val currentList = _tasks.value.toMutableList()
        currentList.addAll(tasks)
        _tasks.value = currentList
    }

    // Вспомогательный метод для инициализации тестовыми данными (если нужно)
    fun loadTestData() {
        _tasks.value = listOf<Task>(
            Task("Купить продукты"),
            Task("Сделать ДЗ по Android"),
            Task("Позвонить маме"),
            Task("Записаться к врачу")
        )
    }
}