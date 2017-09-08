/*
 * DatabaseTestHelper.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/29/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import com.inelasticcollision.recipelink.data.models.Collection;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.data.models.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class DatabaseTestHelper {

    static ContentValues[] getContentValuesFromRecipeList(@NonNull List<Recipe> recipes) {

        int index = 0;

        ContentValues[] contentValuesArray = new ContentValues[recipes.size()];

        long currentTime = new Date().getTime();

        for (Recipe recipe : recipes) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(RecipeTable.COLUMN_DATE_ADDED, recipe.getDateAdded() == Recipe.DATE_SAVED_NEVER ? currentTime : recipe.getDateAdded());
            contentValues.put(RecipeTable.COLUMN_DATE_UPDATED, currentTime);
            contentValues.put(RecipeTable.COLUMN_TITLE, recipe.getTitle());
            contentValues.put(RecipeTable.COLUMN_URL, recipe.getUrl());
            contentValues.put(RecipeTable.COLUMN_IMAGE_URL, recipe.getImageUrl());
            contentValues.put(RecipeTable.COLUMN_FAVORITE, recipe.getFavoriteAsInt());
            contentValues.put(RecipeTable.COLUMN_CATEGORY, "");
            contentValues.put(RecipeTable.COLUMN_KEYWORDS, keywordsListToString(recipe.getKeywords()));
            contentValues.put(RecipeTable.COLUMN_NOTES, recipe.getNotes());

            contentValuesArray[index] = contentValues;

            index++;
        }

        return contentValuesArray;

    }

    static ContentValues[] getContentValuesFromCollectionList(@NonNull List<Collection> collections) {

        int index = 0;

        ContentValues[] contentValuesArray = new ContentValues[collections.size()];

        for (Collection collection : collections) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CollectionTable.COLUMN_TITLE, collection.getTitle());
            contentValues.put(CollectionTable.COLUMN_PHOTO_ID, "");

            contentValuesArray[index] = contentValues;

            index++;
        }

        return contentValuesArray;

    }

    static ContentValues[] getContentValuesFromRelationship(long collectionId, long[] recipeIds) {

        int index = 0;

        ContentValues[] contentValuesArray = new ContentValues[recipeIds.length];

        for (long recipeId : recipeIds) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(RelationshipTable.COLUMN_COLLECTION_ID, collectionId);
            contentValues.put(RelationshipTable.COLUMN_RECIPE_ID, recipeId);

            contentValuesArray[index] = contentValues;

            index++;
        }

        return contentValuesArray;

    }

    static ContentValues[] getContentValuesFromRelationship(List<Relationship> relationships) {

        int index = 0;

        ContentValues[] contentValuesArray = new ContentValues[relationships.size()];

        for (Relationship relationship : relationships) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(RelationshipTable.COLUMN_COLLECTION_ID, relationship.getCollectionId());
            contentValues.put(RelationshipTable.COLUMN_RECIPE_ID, relationship.getRecipeId());

            contentValuesArray[index] = contentValues;

            index++;

        }

        return contentValuesArray;

    }

    static String keywordsListToString(List<String> keywords) {

        if (keywords == null || keywords.isEmpty()) {
            return "";
        }

        int i = 0;

        StringBuilder keywordsBuilder = new StringBuilder();

        for (String keyword : keywords) {

            if (keyword.isEmpty()) {
                continue;
            }

            if (i > 0) {
                keywordsBuilder.append(RecipeTable.KEYWORDS_DELIMITER);
            }

            keywordsBuilder.append(keyword);

            i++;

        }

        String stringKeywords = keywordsBuilder.toString();

        return stringKeywords;

    }

    static List<String> keywordsStringToList(String keywords) {

        if (keywords == null) {
            return new ArrayList<>(0);
        }

        String[] keywordsArray = keywords.split("\\" + RecipeTable.KEYWORDS_DELIMITER);

        return new ArrayList<>(Arrays.asList(keywordsArray));

    }

}
