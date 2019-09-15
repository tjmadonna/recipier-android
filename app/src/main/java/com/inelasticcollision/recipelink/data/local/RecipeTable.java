/*
 * RecipeTable.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/29/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import androidx.annotation.NonNull;

import com.inelasticcollision.recipelink.data.models.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.functions.Func1;

final class RecipeTable implements BaseColumns {

    // Table name
    static final String TABLE_NAME = "recipe";

    // Columns
    static final String COLUMN_DATE_ADDED = "date_added";
    static final String COLUMN_DATE_UPDATED = "date_updated";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_URL = "link";
    static final String COLUMN_IMAGE_URL = "photo_url";
    static final String COLUMN_FAVORITE = "favorite";
    static final String COLUMN_CATEGORY = "category";
    static final String COLUMN_KEYWORDS = "keywords";
    static final String COLUMN_NOTES = "notes";

    // Columns for a recipe
    static final String RECIPE_COLUMNS =
            TABLE_NAME + "." + _ID + ", " +
                    COLUMN_DATE_ADDED + ", " +
                    COLUMN_TITLE + ", " +
                    COLUMN_URL + ", " +
                    COLUMN_IMAGE_URL + ", " +
                    COLUMN_FAVORITE + ", " +
                    COLUMN_KEYWORDS + ", " +
                    COLUMN_NOTES;


    // Columns indices for a recipe
    static final int COL_RECIPE_ID = 0;
    static final int COL_RECIPE_DATE_ADDED = 1;
    static final int COL_RECIPE_TITLE = 2;
    static final int COL_RECIPE_URL = 3;
    static final int COL_RECIPE_IMAGE_URL = 4;
    static final int COL_RECIPE_FAVORITE = 5;
    static final int COL_RECIPE_KEYWORDS = 6;
    static final int COL_RECIPE_NOTES = 7;

    static final String KEYWORDS_DELIMITER = "|";

    // Queries

    static String getRecipeIdQuery(int id) {
        return "SELECT " + RECIPE_COLUMNS + " FROM " + TABLE_NAME + " WHERE " + TABLE_NAME + "." + _ID + " = " + String.valueOf(id) + " ORDER BY " + COLUMN_TITLE + " ASC";
    }

    static String getAllRecipesQuery() {
        return "SELECT " + RECIPE_COLUMNS + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TITLE + " COLLATE NOCASE";
    }

    static String getFavoriteRecipesQuery() {
        return "SELECT " + RECIPE_COLUMNS + " FROM " + TABLE_NAME + " WHERE " + COLUMN_FAVORITE + " = 1 ORDER BY " + COLUMN_TITLE + " COLLATE NOCASE";
    }

    static String getRecipesSearchQuery(String searchTerm) {
        return "SELECT " + RECIPE_COLUMNS + " FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " LIKE '%" + searchTerm + "%' OR " + COLUMN_KEYWORDS + " LIKE '%" + searchTerm + "%' ORDER BY " + COLUMN_TITLE + " COLLATE NOCASE";
    }

    static String getRecipeWhereClause(int id) {
        return TABLE_NAME + "." + _ID + " = " + String.valueOf(id);
    }

    static ContentValues getRecipeAsContentValues(Recipe recipe) {

        final long currentTime = new Date().getTime();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE_ADDED, recipe.getDateAdded() == Recipe.DATE_SAVED_NEVER ? currentTime : recipe.getDateAdded());
        contentValues.put(COLUMN_DATE_UPDATED, currentTime);
        contentValues.put(COLUMN_TITLE, recipe.getTitle());
        contentValues.put(COLUMN_URL, recipe.getUrl());
        contentValues.put(COLUMN_IMAGE_URL, recipe.getImageUrl());
        contentValues.put(COLUMN_FAVORITE, recipe.getFavoriteAsInt());
        contentValues.put(COLUMN_CATEGORY, "");
        contentValues.put(COLUMN_KEYWORDS, keywordsListToString(recipe.getKeywords()));
        contentValues.put(COLUMN_NOTES, recipe.getNotes());

        return contentValues;

    }

    @NonNull
    final static Func1<Cursor, Recipe> ONE_RECIPE_MAPPER = new Func1<Cursor, Recipe>() {
        @Override
        public Recipe call(Cursor cursor) {

            if (cursor == null) {
                return null;
            }

            return new Recipe(
                    cursor.getInt(COL_RECIPE_ID),
                    cursor.getLong(COL_RECIPE_DATE_ADDED),
                    cursor.getString(COL_RECIPE_TITLE),
                    cursor.getString(COL_RECIPE_URL),
                    cursor.getString(COL_RECIPE_IMAGE_URL),
                    cursor.getInt(COL_RECIPE_FAVORITE),
                    keywordsStringToList(cursor.getString(COL_RECIPE_KEYWORDS)),
                    cursor.getString(COL_RECIPE_NOTES));

        }
    };

    @NonNull
    final static Func1<Cursor, List<Recipe>> RECIPE_MAPPER = new Func1<Cursor, List<Recipe>>() {
        @Override
        public List<Recipe> call(Cursor cursor) {

            if (cursor == null) {
                return Collections.emptyList();
            }

            List<Recipe> recipes = new ArrayList<>(cursor.getCount());

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Recipe recipe = new Recipe(
                        cursor.getInt(COL_RECIPE_ID),
                        cursor.getLong(COL_RECIPE_DATE_ADDED),
                        cursor.getString(COL_RECIPE_TITLE),
                        cursor.getString(COL_RECIPE_URL),
                        cursor.getString(COL_RECIPE_IMAGE_URL),
                        cursor.getInt(COL_RECIPE_FAVORITE),
                        keywordsStringToList(cursor.getString(COL_RECIPE_KEYWORDS)),
                        cursor.getString(COL_RECIPE_NOTES));

                recipes.add(recipe);

            }

            return recipes;
        }
    };

    static String keywordsListToString(List<String> keywords) {

        if (keywords == null || keywords.isEmpty()) {
            return "";
        }

        int i = 0;

        StringBuilder keywordsBuilder = new StringBuilder();

        for (String keyword : keywords) {

            if (i > 0) {
                keywordsBuilder.append(KEYWORDS_DELIMITER);
            }

            keywordsBuilder.append(keyword);

            i++;

        }

        return keywordsBuilder.toString();

    }

    static List<String> keywordsStringToList(String keywords) {

        if (keywords == null || keywords.isEmpty()) {
            return Collections.emptyList();
        }

        String[] keywordsArray = keywords.split("\\" + KEYWORDS_DELIMITER);

        return new ArrayList<>(Arrays.asList(keywordsArray));

    }

}
