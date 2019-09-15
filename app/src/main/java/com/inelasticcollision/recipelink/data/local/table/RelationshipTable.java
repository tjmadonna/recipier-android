/*
 * RelationshipTable.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/29/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local.table;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.inelasticcollision.recipelink.data.models.Collection;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.data.models.Relationship;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public final class RelationshipTable implements BaseColumns {

    // Table name
    public static final String TABLE_NAME = "recipe_collection_relationship";

    // Columns
    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String COLUMN_COLLECTION_ID = "collection_id";

    // Columns for a recipe relationship
    public static final String RELATIONSHIP_COLUMNS =
            TABLE_NAME + "." + _ID + ", " +
                    COLUMN_RECIPE_ID + ", " +
                    COLUMN_COLLECTION_ID;


    // Columns indices for a recipe relationship
    public static final int COL_RELATIONSHIP_ID = 0;
    public static final int COL_RELATIONSHIP_RECIPE_ID = 1;
    public static final int COL_RELATIONSHIP_COLLECTION_ID = 2;

    // Queries

    public static String getAllRelationshipQuery() {
        return "SELECT " + RELATIONSHIP_COLUMNS + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_RECIPE_ID + " ASC";
    }

    public final static Func1<Cursor, List<Relationship>> RELATIONSHIP_MAPPER = new Func1<Cursor, List<Relationship>>() {
        @Override
        public List<Relationship> call(Cursor cursor) {

            if (cursor == null) {
                return new ArrayList<>(0);
            }

            List<Relationship> relationships = new ArrayList<>(cursor.getCount());

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                final Relationship relationship = new Relationship(
                        cursor.getInt(COL_RELATIONSHIP_RECIPE_ID),
                        cursor.getInt(COL_RELATIONSHIP_COLLECTION_ID));

                relationships.add(relationship);

            }

            return relationships;

        }
    };

}
