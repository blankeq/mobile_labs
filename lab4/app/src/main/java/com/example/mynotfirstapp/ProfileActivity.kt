package com.example.mynotfirstapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        var isEditing = false

        val btnEdit = findViewById<Button>(R.id.buttonEdit)
        val textName = findViewById<TextView>(R.id.textName)
        val editName = findViewById<EditText>(R.id.editName)
        val textStatus = findViewById<TextView>(R.id.textStatus)
        val editStatus = findViewById<EditText>(R.id.editStatus)
        val textPhone = findViewById<TextView>(R.id.textPhone)
        val textEmail = findViewById<TextView>(R.id.textEmail)
        val layout = findViewById<ConstraintLayout>(R.id.main)
        val constraintSet = ConstraintSet()

        btnEdit.setOnClickListener {
            if (!isEditing) {
                Toast.makeText(this, R.string.toast_message, Toast.LENGTH_SHORT).show()

                editName.setText(textName.text)
                editStatus.setText(textStatus.text)
                btnEdit.text = "Сохранить"

                textName.visibility = View.GONE
                textStatus.visibility = View.GONE
                //textPhone.visibility = View.GONE
                //textEmail.visibility = View.GONE
                editName.visibility = View.VISIBLE
                editStatus.visibility = View.VISIBLE

                isEditing = true
            }
            else {
                val newName = editName.text.trim()
                val newStatus = editStatus.text.trim()

                if (newName.isEmpty() || newStatus.isEmpty()) {
                    Toast.makeText(this, "Имя или статус не могут быть пустыми", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                textName.setText(newName)
                textStatus.setText(newStatus)
                btnEdit.text = "Редактировать"

                textName.visibility = View.VISIBLE
                textStatus.visibility = View.VISIBLE
                editName.visibility = View.GONE
                editStatus.visibility = View.GONE

                isEditing = false

                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
            }

        }

        var profilePhoto = findViewById<ImageView>(R.id.imageAvatar)

        val pickPhoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                profilePhoto.setImageURI(uri)
            } else {
                Toast.makeText(this, "Ничего не выбрано", Toast.LENGTH_SHORT).show()
            }
        }

        profilePhoto.setOnClickListener {
            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}