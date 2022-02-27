package com.inelasticcollision.recipelink.util

fun String.nullIfEmpty(): String? {
    return this.ifEmpty {
        null
    }
}

fun String.capitalizeWords(): String {
    return this.lowercase()
        .split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { char -> char.uppercase() } }
}