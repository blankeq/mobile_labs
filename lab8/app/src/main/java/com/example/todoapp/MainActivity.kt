package com.example.todoapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var counter = 0
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: TaskAdapter
    private val editTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val updatedText = data?.getStringExtra("updated_text")
            val position = data?.getIntExtra("task_position", -1) ?: -1
            val isDeleted = data?.getBooleanExtra("is_deleted", false) ?: false

            if (position != -1) {
                if (isDeleted) {
                    viewModel.deleteTask(position)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position, viewModel.tasks.value.size - position)
                    Toast.makeText(this, "Задача удалена", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (updatedText != null) {
                        viewModel.updateTask(position, updatedText)
                        adapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textCounter = findViewById<TextView>(R.id.textCounter)
        val buttonIncrement = findViewById<Button>(R.id.buttonIncrement)
        val buttonReset = findViewById<Button>(R.id.buttonReset)

        updateCounterDisplay(textCounter)

        buttonIncrement.setOnClickListener {
            counter++
            updateCounterDisplay(textCounter)
        }

        buttonReset.setOnClickListener {
            counter = 0
            updateCounterDisplay(textCounter)
        }

        val editTextInput = findViewById<EditText>(R.id.editTextInput)
        val buttonShow = findViewById<Button>(R.id.buttonShow)
        val textEntered = findViewById<TextView>(R.id.textEntered)

        buttonShow.setOnClickListener {
            val inputText = editTextInput.text.toString()
            textEntered.text = getString(R.string.label_entered) + " $inputText"
        }

        val editTextTask = findViewById<EditText>(R.id.editTextTask)
        val buttonAddTask = findViewById<Button>(R.id.buttonAddTask)
        val buttonDelLastTask = findViewById<Button>(R.id.buttonDelLastTask)
        val buttonDelTasks = findViewById<Button>(R.id.buttonDelTasks)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTasks)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(tasks = mutableListOf<Task>(),
            onItemLongClick = { position ->
                showEditDialog(position)
            },
            onItemClick = { position ->
                val taskText = viewModel.tasks.value[position].text
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("task_text", taskText)
                intent.putExtra("task_position", position)
                editTaskLauncher.launch(intent)
            },
            onCheckedChange = { position, isChecked ->
                viewModel.toggleTask(position, isChecked)
            }
        )
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasks.collect { tasks ->
                    adapter.updateData(tasks) // предполагаем, что у адаптера есть такой метод
                }
            }
        }

        if (viewModel.tasks.value.isEmpty()) {
            viewModel.loadTestData()
        }

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            var deletedTask: Task? = null
            var deletedPosition: Int = -1

            override fun onMove(
                rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val tasks = viewModel.tasks.value

                deletedTask = tasks[position]
                deletedPosition = position

                viewModel.deleteTask(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, viewModel.tasks.value.size)

                Snackbar.make(
                    findViewById(R.id.main),
                    "Задача удалена",
                    Snackbar.LENGTH_LONG
                ).setAction("Отмена") {
                    deletedTask?.let { task ->
                        if (deletedPosition != -1) {
                            viewModel.restoreTask(deletedPosition, task)
                            adapter.notifyItemInserted(deletedPosition)
                            adapter.notifyItemRangeChanged(deletedPosition, viewModel.tasks.value.size - deletedPosition)
                        }
                    }
                    deletedTask = null
                    deletedPosition = -1
                }.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        buttonAddTask.setOnClickListener {
            val task = editTextTask.text.toString()
            if (task.isNotBlank()) {
                viewModel.addTask(Task(task))
                adapter.notifyItemInserted(viewModel.tasks.value.size - 1)
                editTextTask.text.clear()
            } else {
                Toast.makeText(this, "Введите задачу", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDelLastTask.setOnClickListener {
            if (!viewModel.tasks.value.isEmpty()) {
                viewModel.deleteTask(viewModel.tasks.value.lastIndex)
                adapter.notifyItemRemoved(viewModel.tasks.value.size)
                editTextTask.text.clear()
            } else {
                Toast.makeText(this, "Список задач пуст", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDelTasks.setOnClickListener {
            if (!viewModel.tasks.value.isEmpty()) {
                viewModel.clearTasks()
                adapter.notifyDataSetChanged()
                editTextTask.text.clear()
            } else {
                Toast.makeText(this, "Список задач пуст", Toast.LENGTH_SHORT).show()
            }
        }

        val textTaskId = findViewById<EditText>(R.id.editTextTaskId)
        val buttonDelTaskById = findViewById<Button>(R.id.buttonDelTaskById)

        buttonDelTaskById.setOnClickListener {
            val id = textTaskId.text.toString().toIntOrNull()

            if (id != null) {
                if (!viewModel.tasks.value.isEmpty()) {
                    if ((id - 1) >= 0 && (id - 1) < viewModel.tasks.value.size) {
                        viewModel.deleteTask(id - 1)
                        adapter.notifyItemRemoved(id - 1)
                        adapter.notifyItemRangeChanged(id - 1, viewModel.tasks.value.size - id - 1)
                        editTextTask.text.clear()
                    } else {
                        Toast.makeText(this, "Такой номер задачи не существует", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Список задач пуст", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Номер должен быть целым числом", Toast.LENGTH_SHORT).show()
            }
        }

//        if (savedInstanceState != null) {
//            val savedTasks = savedInstanceState.getStringArrayList("tasks")
//            val savedChecks = savedInstanceState.getIntegerArrayList("isChecked")
//            if (savedTasks != null && savedChecks != null) {
//                var savedTaskObjects = emptyList<Task>()
//                for (i in  0..<savedTasks.size) {
//                    savedTaskObjects += Task(savedTasks[i], savedChecks[i])
//                }
//                viewModel.clearTasks()
//                viewModel.addAllTasks(savedTaskObjects)
//                adapter.notifyDataSetChanged()
//            }
//        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putInt("counter", counter)
//
//        var tasksText = ArrayList<String>()
//        for (i in  0..<viewModel.tasks.value.size) {
//            tasksText.add(viewModel.tasks.value[i].text)
//        }
//        var tasksIsChecked = ArrayList<Int>()
//        for (i in  0..<viewModel.tasks.value.size) {
//            tasksIsChecked.add(viewModel.tasks.value[i].isChecked)
//        }
//        outState.putStringArrayList("tasks", tasksText)
//        outState.putIntegerArrayList("isChecked", tasksIsChecked)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        counter = savedInstanceState.getInt("counter")
//        viewModel.clearTasks()
//
//        var tasksText = savedInstanceState.getStringArrayList("tasks") ?: emptyList()
//        var tasksIsChecked = savedInstanceState.getIntegerArrayList("isChecked") ?: emptyList()
//        if (tasksText != null && tasksIsChecked != null) {
//            var savedTaskObjects = emptyList<Task>()
//            for (i in  0..<tasksText.size) {
//                savedTaskObjects += Task(tasksText[i], tasksIsChecked[i])
//            }
//            viewModel.clearTasks()
//            viewModel.addAllTasks(savedTaskObjects)
//        }
//
//        updateCounterDisplay(findViewById(R.id.textCounter))
//        adapter.notifyDataSetChanged()
//    }

    private fun updateCounterDisplay(textView: TextView) {
        textView.text = getString(R.string.counter_text, counter)
    }

    private fun updateTasksDisplay(tasks: MutableList<String>, textTasks: TextView) {
        if (tasks.isEmpty()) {
            textTasks.text = getString(R.string.label_tasks)
        } else {
            textTasks.text = "Список задач:\n" + tasks.withIndex().joinToString("\n") { (index, task) -> "${index + 1}. $task" }
        }
    }

    fun showEditDialog(position: Int) {
        val task = viewModel.tasks.value[position]
        val editText = EditText(this)
        editText.setText(task.text)

        AlertDialog.Builder(this)
            .setTitle("Редактировать задачу")
            .setView(editText)
            .setPositiveButton("Сохранить") { _, _ ->
                val newTitle = editText.text.toString()
                if (newTitle.isNotEmpty()) {
                    viewModel.updateTask(position, newTitle)
                    adapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

}