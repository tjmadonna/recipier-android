/*
 * CollectionTable.java
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

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public final class CollectionTable implements BaseColumns {

    // Table name
    public static final String TABLE_NAME = "collection";

    // Columns
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PHOTO_ID = "photo_id";

    // Columns for a collection
    public static final String COLLECTION_COLUMNS =
            TABLE_NAME + "." + _ID  + ", " +
                    COLUMN_TITLE;

    // Columns indices a collection
    public static final int COL_COLLECTION_ID = 0;
    public static final int COL_COLLECTION_TITLE = 1;

    // Queries

    public static String getAllCollectionsQuery() {
        return "SELECT " + COLLECTION_COLUMNS + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TITLE + " ASC";
    }

    public final static Func1<Cursor, List<Collection>> COLLECTION_MAPPER = new Func1<Cursor, List<Collection>>() {
        @Override
        public List<Collection> call(Cursor cursor) {

            if (cursor == null) {
                return new ArrayList<>(0);
            }

            List<Collection> collections = new ArrayList<>(cursor.getCount());

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                final Collection collection = new Collection(
                        cursor.getInt(COL_COLLECTION_ID),
                        cursor.getString(COL_COLLECTION_TITLE));

                collections.add(collection);

            }

            return collections;
        }
    };

}
