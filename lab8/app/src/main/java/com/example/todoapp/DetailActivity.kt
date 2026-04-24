package com.example.todoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.content.Intent


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTaskDetail = findViewById<EditText>(R.id.editTaskDetail)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonDel = findViewById<Button>(R.id.buttonDel)

        val taskText = intent.getStringExtra("task_text") ?: "Нет данных"
        val position = intent.getIntExtra("task_position", -1)
        editTaskDetail.setText(taskText)

        buttonSave.setOnClickListener {
            val updatedText = editTaskDetail.text.toString()
            if (updatedText.isNotBlank()) {
                val resultIntent = Intent()
                resultIntent.putExtra("updated_text", updatedText)
                resultIntent.putExtra("task_position", position)

                setResult(RESULT_OK, resultIntent)
                finish() // закрывает текущую активность и возвращает к предыдущей
            }
        }

        buttonDel.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("task_position", position)
            resultIntent.putExtra("is_deleted", true)

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}