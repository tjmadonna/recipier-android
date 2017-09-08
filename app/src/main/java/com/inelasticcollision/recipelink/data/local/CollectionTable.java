/*
 * CollectionTable.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/29/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.inelasticcollision.recipelink.data.models.Collection;
import com.inelasticcollision.recipelink.data.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

final class CollectionTable implements BaseColumns {

    // Table name
    static final String TABLE_NAME = "collection";

    // Columns
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_PHOTO_ID = "photo_id";

    // Columns for a collection
    static final String COLLECTION_COLUMNS =
            TABLE_NAME + "." + _ID  + ", " +
                    COLUMN_TITLE;

    // Columns indices a collection
    static final int COL_COLLECTION_ID = 0;
    static final int COL_COLLECTION_TITLE = 1;

    // Queries

    static String getAllCollectionsQuery() {
        return "SELECT " + COLLECTION_COLUMNS + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TITLE + " ASC";
    }

    final static Func1<Cursor, List<Collection>> COLLECTION_MAPPER = new Func1<Cursor, List<Collection>>() {
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
