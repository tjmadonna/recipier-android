/*
 * RecipesInjection.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipes;

import android.content.Context;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.db.AppDatabase;

class RecipesInjection {

    static void inject(Context context, RecipesContract.View view) {

        LocalDataProvider localDataProvider = AppDatabase.getInstance(context).recipeDao();

        RecipesContract.Presenter presenter = new RecipesPresenter(view, localDataProvider);

        view.setPresenter(presenter);

    }

}
