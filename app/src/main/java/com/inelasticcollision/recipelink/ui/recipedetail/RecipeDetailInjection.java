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
import com.inelasticcollision.recipelink.di.AppDependencyProvider;

class RecipeDetailInjection {

    static void inject(Context context, RecipeDetailContract.View view, int recipeId) {

        LocalDataProvider localDataProvider = AppDependencyProvider.provideLocalDataProvider(context);

        RecipeDetailContract.Presenter presenter = new RecipeDetailPresenter(view, localDataProvider, recipeId);

        view.setPresenter(presenter);

    }

}
