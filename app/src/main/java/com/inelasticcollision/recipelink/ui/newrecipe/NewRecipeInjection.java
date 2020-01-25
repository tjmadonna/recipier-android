/*
 * NewRecipeInjection.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.newrecipe;

import android.content.Context;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.db.AppDatabase;
import com.inelasticcollision.recipelink.data.remote.ExtractionRemoteDataProvider;
import com.inelasticcollision.recipelink.data.remote.RemoteDataProvider;

import java.util.ArrayList;

class NewRecipeInjection {

    static void inject(Context context, NewRecipeContract.View view, String title, String url) {

        NewRecipeSavedState state = new NewRecipeSavedState(new ArrayList<String>(0), "", false, true, title, url);

        inject(context, view, state);

    }

    static void inject(Context context, NewRecipeContract.View view, NewRecipeSavedState state) {

        LocalDataProvider localDataProvider = AppDatabase.getInstance(context).recipeDao();

        RemoteDataProvider remoteDataProvider = ExtractionRemoteDataProvider.getInstance();

        NewRecipeContract.Presenter presenter = new NewRecipePresenter(view, localDataProvider, remoteDataProvider, state);

        view.setPresenter(presenter);

    }

}
