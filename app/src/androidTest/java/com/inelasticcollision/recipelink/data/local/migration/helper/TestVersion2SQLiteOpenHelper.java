package com.inelasticcollision.recipelink.data.local.migration.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import static com.inelasticcollision.recipelink.data.local.migration.helper.TestVersion2RecipeTable.*;

public final class TestVersion2SQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public TestVersion2SQLiteOpenHelper(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_DATE_ADDED + " INTEGER NOT NULL, " +
                COLUMN_DATE_UPDATED + " INTEGER NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_URL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                COLUMN_FAVORITE + " INTEGER NOT NULL, " +
                COLUMN_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_KEYWORDS + " TEXT NOT NULL, " +
                COLUMN_NOTES + " TEXT NOT NULL, " +
                " UNIQUE (" + COLUMN_URL + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertValues(List<TestVersion2Recipe> values) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        for (TestVersion2Recipe value : values) {
            db.insert(TABLE_NAME, null, value.getContentValues());
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
}
