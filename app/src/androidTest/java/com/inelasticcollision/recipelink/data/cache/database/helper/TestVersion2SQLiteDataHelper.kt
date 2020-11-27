package com.inelasticcollision.recipelink.data.cache.database.helper

import android.content.ContentValues
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.test.factory.DataFactory.randomBoolean
import com.inelasticcollision.recipelink.test.factory.DataFactory.randomLong
import com.inelasticcollision.recipelink.test.factory.DataFactory.randomString
import com.inelasticcollision.recipelink.util.nullIfEmpty
import java.util.*

object TestVersion2SQLiteDataHelper {

    @JvmStatic
    fun generateRandomContent(size: Int): List<ContentValues> {
        val values = ArrayList<ContentValues>(size)
        for (i in 0 until size) {
            val value = ContentValues(9)
            value.put("date_added", randomLong())
            value.put("date_updated", randomLong())
            value.put("title", randomString())
            value.put("link", randomString())
            value.put("photo_url", if (randomBoolean()) randomString() else "")
            value.put("favorite", if (randomBoolean()) 1 else 0)
            value.put("category", if (randomBoolean()) randomString() else "")
            value.put("keywords", if (randomBoolean()) randomString() else "")
            value.put("notes", if (randomBoolean()) randomString() else "")
            values.add(value)
        }
        return values
    }

    @JvmStatic
    fun mapContentValuesToExpectedRecipes(contentValues: List<ContentValues>): List<Recipe> {
        return contentValues.map { values ->
            return@map Recipe(
                lastModified = Date(values.getAsLong("date_updated")),
                title = values.getAsString("title"),
                url = values.getAsString("link"),
                imageUrl = values.getAsString("photo_url").nullIfEmpty(),
                favorite = values.getAsBoolean("favorite"),
                notes = values.getAsString("notes").nullIfEmpty(),
                tags = values.getAsString("keywords").nullIfEmpty()?.split("|")
            )
        }.sortedBy { recipe ->
            recipe.title
        }
    }
}