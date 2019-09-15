/*
 * BriteDatabaseHelper.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local.helper;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

public class BriteDatabaseHelper {

    private static BriteDatabase INSTANCE;

    public static BriteDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            DatabaseHelper helper = new DatabaseHelper(context);

            SqlBrite sqlBrite = new SqlBrite.Builder().build();

            INSTANCE = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());

        }

        return INSTANCE;
    }

}
