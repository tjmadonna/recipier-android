package com.inelasticcollision.recipelink.data.cache.database.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.COLUMN_CATEGORY
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.COLUMN_DATE_ADDED
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.COLUMN_DATE_UPDATED
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.COLUMN_FAVORITE
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.COLUMN_IMAGE_URL
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.COLUMN_KEYWORDS
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.COLUMN_NOTES
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.COLUMN_TITLE
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.COLUMN_URL
import com.inelasticcollision.recipelink.data.cache.database.helper.TestVersion2RecipeTable.TABLE_NAME

class TestVersion2SQLiteOpenHelper(context: Context?, name: String?) :
    SQLiteOpenHelper(context, name, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createRecipeTable = "CREATE TABLE " + TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                COLUMN_DATE_ADDED + " INTEGER NOT NULL, " +
                COLUMN_DATE_UPDATED + " INTEGER NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_URL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                COLUMN_FAVORITE + " INTEGER NOT NULL, " +
                COLUMN_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_KEYWORDS + " TEXT NOT NULL, " +
                COLUMN_NOTES + " TEXT NOT NULL, " +
                " UNIQUE (" + COLUMN_URL + ") ON CONFLICT REPLACE);"
        db.execSQL(createRecipeTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insertValues(values: List<ContentValues>) {
        val db = writableDatabase
        db.beginTransaction()
        for (value in values) {
            db.insert(TABLE_NAME, null, value)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 2
    }
}