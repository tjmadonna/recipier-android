package com.inelasticcollision.recipelink.data.cache.database

import android.content.ContentValues
import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.inelasticcollision.recipelink.data.cache.converter.DateTypeConverter
import com.inelasticcollision.recipelink.data.cache.converter.TagListTypeConverter
import com.inelasticcollision.recipelink.data.cache.dao.RecipeDao
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.util.nullIfEmpty
import java.util.*

@Database(entities = [Recipe::class], version = 3, exportSchema = true)
@TypeConverters(DateTypeConverter::class, TagListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {

        private const val DATABASE_NAME = "recipecollection.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val tempSynInstance = INSTANCE
                if (tempSynInstance != null) {
                    return tempSynInstance
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(AppDatabaseMigration.MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

object AppDatabaseMigration {

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create new table and index
            database.execSQL("CREATE TABLE IF NOT EXISTS recipes (id TEXT NOT NULL, last_modified INTEGER NOT NULL, title TEXT NOT NULL, url TEXT NOT NULL, image_url TEXT, favorite INTEGER NOT NULL, notes TEXT, tags TEXT, PRIMARY KEY(id))")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_recipes_title ON recipes (title)")

            val queryCursor =
                database.query("SELECT date_updated, title, link, photo_url, favorite, notes, keywords FROM recipe")
            if (queryCursor.count > 0) {
                queryCursor.use { cursor ->
                    database.beginTransaction()
                    while (cursor.moveToNext()) {
                        val lastModified = cursor.getLong(0)
                        val title = cursor.getString(1)
                        val url = cursor.getString(2)
                        val imageUrl = cursor.getString(3)
                        val favorite = cursor.getInt(4)
                        val notes = cursor.getString(5)
                        val tags = cursor.getString(6)

                        val contentValues = ContentValues(8)
                        contentValues.put("id", UUID.randomUUID().toString())
                        contentValues.put("last_modified", lastModified)
                        contentValues.put("title", title)
                        contentValues.put("url", url)
                        contentValues.put("image_url", imageUrl.nullIfEmpty())
                        contentValues.put("favorite", favorite)
                        contentValues.put("notes", notes.nullIfEmpty())
                        contentValues.put("tags", tags.nullIfEmpty())
                        database.insert("recipes", OnConflictStrategy.REPLACE, contentValues)
                    }
                    database.setTransactionSuccessful()
                    database.endTransaction()
                }
            }

            // Delete old tables if they exist
            database.execSQL("DROP TABLE IF EXISTS recipe")
            database.execSQL("DROP TABLE IF EXISTS collection")
            database.execSQL("DROP TABLE IF EXISTS recipe_collection_relationship")
        }
    }
}