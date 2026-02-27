package com.example.myfirstapp.utils

import org.junit.Assert.*
import org.junit.Test

class StudentTest {

    @Test
    fun formatStudentName_correct() {
        val student = Student("Иван", "Иванов", "ПИ-101", 4.8)
        assertEquals("Иванов И. (группа ПИ-101)", student.formatStudentName())
    }

    @Test
    fun getStatus_correct() {
        val excellent = Student("Аня", "Петрова", "ПИ-101", 4.5)
        val good = Student("Борис", "Сидоров", "ПИ-101", 3.8)
        val satisfactory = Student("Виктор", "Кузнецов", "ПИ-101", 3.0)
        val failing = Student("Глеб", "Белов", "ПИ-101", 2.0)

        assertEquals("отличник", excellent.getStatus())
        assertEquals("хорошист", good.getStatus())
        assertEquals("троечник", satisfactory.getStatus())
        assertEquals("двоечник", failing.getStatus())
    }

    @Test
    fun formatStudentName_emptyFirstName() {
        val student = Student("", "Смит", "Inter-1", 4.0)
        assertEquals("Смит  (группа Inter-1)", student.formatStudentName())
    }
}