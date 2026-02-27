package com.example.myfirstapp.utils

fun String.isValidEmail(): Boolean {
    return this.contains("@") && this.contains(".")
}

fun formatAuthorName(fullName: String): String {
    val parts = fullName.split(" ").filter { it.isNotBlank() }
    return when (parts.size) {
        1 -> parts[0]
        2 -> "${parts[0]} ${parts[1].first()}."
        3 -> "${parts[0]} ${parts[1].first()}.${parts[2].first()}."
        else -> fullName
    }
}

fun applyDiscount(price: Double, discountPercent: Double): Double {
    require(discountPercent in 0.0..100.0) { "Скидка должна быть от 0 до 100" }
    return price * (1 - discountPercent / 100)
}