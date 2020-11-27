package com.inelasticcollision.recipelink.test.factory

import java.util.concurrent.ThreadLocalRandom

object DataFactory {

    fun randomUuid(): String {
        return java.util.UUID.randomUUID().toString()
    }

    fun randomString(): String {
        return randomUuid()
    }

    fun randomInt(upperBound: Int = 1000): Int {
        return ThreadLocalRandom.current().nextInt(0, upperBound + 1)
    }

    fun randomLong(): Long {
        return randomInt().toLong()
    }

    fun randomBoolean(): Boolean {
        return Math.random() < 0.5
    }

    fun makeStringList(count: Int): List<String> {
        return mutableListOf<String>().apply {
            repeat(count) {
                add(randomUuid())
            }
        }
    }
}