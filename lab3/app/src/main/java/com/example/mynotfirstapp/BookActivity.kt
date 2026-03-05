package com.example.mynotfirstapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mynotfirstapp.models.Book

class BookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val books = getBooks()

        val originalText = books.joinToString("\n") { "* ${it.name} - ${it.author} -" +
                " ${it.year} г. - ${it.pageCount} стр." }
        findViewById<TextView>(R.id.textOriginal).text = originalText

        val After2000Text = books.filter { it.year > 1999 }.joinToString("\n") { "* ${it.name} - ${it.author} -" +
                " ${it.year} г. - ${it.pageCount} стр." }
        findViewById<TextView>(R.id.textAfter2000).text = After2000Text

        val sortedText = books.sortedBy { it.pageCount }.map { "* ${it.name} – ${it.pageCount} стр." }
        val sortText = sortedText.joinToString("\n")
        findViewById<TextView>(R.id.textSorted).text = sortText

        val nameAuthorText = books.map { "* ${it.name} – ${it.author}" }
        val nAText = nameAuthorText.joinToString("\n")
        findViewById<TextView>(R.id.textNameAuthor).text = nAText

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun getBooks(): List<Book> {
        return listOf(
            Book("1984", "George Orwell", 1949, 328),
            Book("Мастер и Маргарита", "Михаил Булгаков", 1967, 480),
            Book("Великий Гэтсби", "Ф. Скотт Фицджеральд", 1925, 180),
            Book("Хоббит, или Туда и обратно", "Дж. Р. Р. Толкин", 1937, 310),
            Book("Гарри Поттер и философский камень", "Дж. К. Роулинг", 1997, 223),
            Book("Над пропастью во ржи", "Дж. Д. Сэлинджер", 1951, 234),
            Book("О дивный новый мир", "Олдос Хаксли", 1932, 268),
            Book("451 градус по Фаренгейту", "Рэй Брэдбери", 1953, 158),
            Book("Ведьмак: Последнее желание", "Анджей Сапковский", 1993, 288),
            Book("Преступление и наказание", "Федор Достоевский", 1866, 671),
            Book("Жизнь Пи", "Янн Мартел", 2001, 352),
            Book("Бегущий за ветром", "Халед Хоссейни", 2003, 448),
            Book("Облачный атлас", "Дэвид Митчелл", 2004, 704),
            Book("Книжный вор", "Маркус Зусак", 2005, 552),
            Book("Дорога", "Кормак Маккарти", 2006, 288),
            Book("Девушка с татуировкой дракона", "Стиг Ларссон", 2005, 624),
            Book("Шантарам", "Грегори Дэвид Робертс", 2003, 864),
            Book("Цирцея", "Мадлен Миллер", 2018, 400),
            Book("Полночная библиотека", "Мэтт Хейг", 2020, 304),
            Book("Проект «Аве Мария»", "Энди Вейер", 2021, 480)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}