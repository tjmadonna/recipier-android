package com.inelasticcollision.recipelink.data.cache.database.helper

import android.provider.BaseColumns

internal object TestVersion2RecipeTable : BaseColumns {
    // Table name
    const val TABLE_NAME = "recipe"

    // Columns
    const val COLUMN_DATE_ADDED = "date_added"
    const val COLUMN_DATE_UPDATED = "date_updated"
    const val COLUMN_TITLE = "title"
    const val COLUMN_URL = "link"
    const val COLUMN_IMAGE_URL = "photo_url"
    const val COLUMN_FAVORITE = "favorite"
    const val COLUMN_CATEGORY = "category"
    const val COLUMN_KEYWORDS = "keywords"
    const val COLUMN_NOTES = "notes"
}