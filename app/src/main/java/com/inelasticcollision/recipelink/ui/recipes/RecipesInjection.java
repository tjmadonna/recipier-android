/*
 * RecipesInjection.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipes;

import android.content.Context;

import com.inelasticcollision.recipelink.data.local.helper.BriteDatabaseHelper;
import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.provider.RecipeLocalDataProvider;
import com.squareup.sqlbrite.BriteDatabase;

class RecipesInjection {

    static void inject(Context context, RecipesContract.View view) {

        BriteDatabase database = BriteDatabaseHelper.getInstance(context);

        LocalDataProvider dataProvider = new RecipeLocalDataProvider(database);

        RecipesContract.Presenter presenter = new RecipesPresenter(view, dataProvider);

        view.setPresenter(presenter);

    }

}
