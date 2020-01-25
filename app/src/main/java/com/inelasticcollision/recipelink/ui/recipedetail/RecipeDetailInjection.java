/*
 * RecipeDetailInjection.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipedetail;

import android.content.Context;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.db.AppDatabase;

class RecipeDetailInjection {

    static void inject(Context context, RecipeDetailContract.View view, String recipeId) {

        LocalDataProvider localDataProvider = AppDatabase.getInstance(context).recipeDao();

        RecipeDetailContract.Presenter presenter = new RecipeDetailPresenter(view, localDataProvider, recipeId);

        view.setPresenter(presenter);

    }

}
