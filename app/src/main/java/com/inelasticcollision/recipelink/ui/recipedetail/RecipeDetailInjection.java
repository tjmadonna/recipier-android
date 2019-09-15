/*
 * RecipeDetailInjection.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipedetail;

import android.content.Context;

import com.inelasticcollision.recipelink.data.local.helper.BriteDatabaseHelper;
import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.provider.RecipeLocalDataProvider;
import com.squareup.sqlbrite.BriteDatabase;

class RecipeDetailInjection {

    static void inject(Context context, RecipeDetailContract.View view, int recipeId) {

        BriteDatabase database = BriteDatabaseHelper.getInstance(context);

        LocalDataProvider localDataProvider = new RecipeLocalDataProvider(database);

        RecipeDetailContract.Presenter presenter = new RecipeDetailPresenter(view, localDataProvider, recipeId);

        view.setPresenter(presenter);

    }

}