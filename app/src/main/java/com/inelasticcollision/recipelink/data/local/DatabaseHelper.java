/*
 * DatabaseHelper.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/29/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inelasticcollision.recipelink.data.local.table.CollectionTable;
import com.inelasticcollision.recipelink.data.local.table.RecipeTable;
import com.inelasticcollision.recipelink.data.local.table.RelationshipTable;

class DatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "recipecollection.db";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createRecipeTable(db);
        createCollectionTable(db);
        createRecipeCategoryRelationshipTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion) {
            case 1:
                createCollectionTable(db);
                createRecipeCategoryRelationshipTable(db);
            case 2:

        }

    }

    private void createRecipeTable(SQLiteDatabase db) {
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeTable.TABLE_NAME + " (" +
                RecipeTable._ID + " INTEGER PRIMARY KEY, " +
                RecipeTable.COLUMN_DATE_ADDED + " INTEGER NOT NULL, " +
                RecipeTable.COLUMN_DATE_UPDATED + " INTEGER NOT NULL, " +
                RecipeTable.COLUMN_TITLE + " TEXT NOT NULL, " +
                RecipeTable.COLUMN_URL + " TEXT UNIQUE NOT NULL, " +
                RecipeTable.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                RecipeTable.COLUMN_FAVORITE + " INTEGER NOT NULL, " +
                RecipeTable.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                RecipeTable.COLUMN_KEYWORDS + " TEXT NOT NULL, " +
                RecipeTable.COLUMN_NOTES + " TEXT NOT NULL, " +
                " UNIQUE (" + RecipeTable.COLUMN_URL + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    private void createCollectionTable(SQLiteDatabase db) {
        final String SQL_CREATE_COLLECTION_TABLE = "CREATE TABLE " + CollectionTable.TABLE_NAME + " (" +
                CollectionTable._ID + " INTEGER PRIMARY KEY, " +
                CollectionTable.COLUMN_TITLE + " TEXT NOT NULL, " +
                CollectionTable.COLUMN_PHOTO_ID + " TEXT NOT NULL, " +
                " UNIQUE (" + CollectionTable.COLUMN_TITLE + ") ON CONFLICT ABORT);";

        db.execSQL(SQL_CREATE_COLLECTION_TABLE);
    }

    private void createRecipeCategoryRelationshipTable(SQLiteDatabase db) {
        final String SQL_CREATE_RELATIONSHIP_TABLE = "CREATE TABLE " + RelationshipTable.TABLE_NAME + " (" +
                RelationshipTable._ID + " INTEGER PRIMARY KEY, " +
                RelationshipTable.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RelationshipTable.COLUMN_COLLECTION_ID + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_RELATIONSHIP_TABLE);
    }

}
