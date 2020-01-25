package com.inelasticcollision.recipelink.data.local.migration;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.UUID;

public final class AppDatabaseMigration {

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            // Delete collections and relationships tables
            db.execSQL("DROP TABLE IF EXISTS collection;");
            db.execSQL("DROP TABLE IF EXISTS recipe_collection_relationship;");

            // Create recipes table
            db.execSQL("CREATE TABLE IF NOT EXISTS recipes (" +
                    "id TEXT NOT NULL, " +
                    "last_modified INTEGER NOT NULL, " +
                    "title TEXT NOT NULL, " +
                    "url TEXT NOT NULL, " +
                    "image_url TEXT, " +
                    "favorite INTEGER NOT NULL, " +
                    "notes TEXT, " +
                    "tags TEXT, " +
                    "PRIMARY KEY(id));");

            Cursor cursor = db.query("SELECT date_updated, title, link, photo_url, favorite, notes, keywords FROM recipe;");
            db.beginTransaction();
            try {
                while (cursor.moveToNext()) {
                    ContentValues values = new ContentValues(8);
                    values.put("id", UUID.randomUUID().toString());
                    values.put("last_modified", cursor.getLong(0));
                    values.put("title", cursor.getString(1));
                    values.put("url", cursor.getString(2));
                    values.put("image_url", cursor.getString(3));
                    values.put("favorite", cursor.getInt(4));
                    values.put("notes", cursor.getString(5));
                    values.put("tags", cursor.getString(6));
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
                cursor.close();
            }

            db.execSQL("DROP TABLE IF EXISTS recipe;");
        }
    };

}