package com.example.mynotfirstapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mynotfirstapp.models.Product

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val products = getProducts()

        val originalText = products.joinToString("\n") { "* ${it.name} - ${it.price} руб." +
                " (${if (it.inStock) "есть в наличии" else "нет в наличии"})" }
        findViewById<TextView>(R.id.textOriginal).text = originalText

        val inStockText = products.filter { it.inStock }.joinToString("\n") { "* ${it.name} - ${it.price} руб." +
                " (${if (it.inStock) "есть в наличии" else "нет в наличии"})" }
        findViewById<TextView>(R.id.textInStock).text = inStockText

        val sorted_1Text = products.sortedBy { it.name }.sortedBy { it.price }.map { "* ${it.name} – ${it.price} руб." }
        val sort_1Text = sorted_1Text.joinToString("\n")
        findViewById<TextView>(R.id.textSorted_1).text = sort_1Text

        val sorted_2Text = products.filter { it.price < 2000.0 }.sortedBy { it.name }.map { "* ${it.name} – ${it.price} руб." }
        val sort_2Text = sorted_2Text.joinToString("\n")
        findViewById<TextView>(R.id.textSorted_2).text = sort_2Text

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true);
    }

    private fun getProducts(): List<Product> {
        return listOf(
            Product("Ноутбук", "Электроника", 75000.0, true),
            Product("Мышь", "Электроника", 1500.0, true),
            Product("Книга 'Котлин'", "Книги", 1200.0, false),
            Product("Флешка 64GB", "Электроника", 2000.0, true),
            Product("Блокнот", "Канцелярия", 300.0, true),
            Product("Ручка", "Канцелярия", 50.0, false),
            Product("Монитор", "Электроника", 25000.0, true)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}