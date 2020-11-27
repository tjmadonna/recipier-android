package com.inelasticcollision.recipelink.data.cache.converter

import androidx.room.TypeConverter
import java.util.*

object DateTypeConverter {

    @JvmStatic
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @JvmStatic
    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }
}