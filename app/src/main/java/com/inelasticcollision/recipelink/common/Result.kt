package com.inelasticcollision.recipelink.common

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val throwable: Throwable) : Result<Nothing>()
}