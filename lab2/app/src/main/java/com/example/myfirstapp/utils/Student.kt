package com.example.myfirstapp.utils

data class Student(
    val name: String,
    val surname: String,
    val groupName: String,
    val avgMark: Double,
) {
    fun formatStudentName(): String {
        val fName = if (name.isNotEmpty()) "${name.first()}." else ""
        val sName = if (surname.isNotEmpty()) "${surname}" else ""
        val group = if (groupName.isNotEmpty()) "${groupName}" else ""

        return "$sName $fName (группа $group)"
    }

    fun getStatus(): String {
        return when {
            avgMark >= 4.5 -> "отличник"
            avgMark >= 3.5 -> "хорошист"
            avgMark >= 2.5 -> "троечник"
            else -> "двоечник"
        }
    }
}


