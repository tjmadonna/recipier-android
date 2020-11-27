package com.inelasticcollision.recipelink.data.cache.converter

import androidx.room.TypeConverter

object TagListTypeConverter {

    private const val DELIMITER = "|"

    @JvmStatic
    @TypeConverter
    fun fromTagList(tagList: List<String>?): String? {
        return if (tagList!= null && tagList.isNotEmpty()) {
            tagList.joinToString(DELIMITER)
        } else {
            null
        }
    }

    @JvmStatic
    @TypeConverter
    fun toTagList(tagString: String?): List<String>? {
        return tagString?.split(DELIMITER)
    }
}