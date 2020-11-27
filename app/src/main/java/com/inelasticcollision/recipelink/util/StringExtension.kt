package com.inelasticcollision.recipelink.util

import java.util.*

fun String.nullIfEmpty(): String? {
    return if (this.isNotEmpty()) {
        this
    } else {
        null
    }
}

fun String.capitalizeWords(locale: Locale = Locale.getDefault()): String {
    return this.toLowerCase(locale)
        .split(" ")
        .joinToString(" ") { word -> word.capitalize(locale) }
}