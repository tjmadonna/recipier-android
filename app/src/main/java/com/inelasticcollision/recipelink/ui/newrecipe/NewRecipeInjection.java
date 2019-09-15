/*
 * NewRecipeInjection.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.newrecipe;

import android.content.Context;

import com.inelasticcollision.recipelink.data.local.BriteDatabaseHelper;
import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.RecipeLocalDataProvider;
import com.inelasticcollision.recipelink.data.remote.RemoteDataProvider;
import com.inelasticcollision.recipelink.data.remote.ExtractionService;
import com.inelasticcollision.recipelink.data.remote.ExtractionRemoteDataProvider;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;

class NewRecipeInjection {

    static void inject(Context context, NewRecipeContract.View view, String title, String url) {

        NewRecipeSavedState state = new NewRecipeSavedState(new ArrayList<String>(0), "", false, true, title, url);

        inject(context, view, state);

    }

    static void inject(Context context, NewRecipeContract.View view, NewRecipeSavedState state) {

        BriteDatabase database = BriteDatabaseHelper.getInstance(context);

        ExtractionService service = ExtractionRemoteDataProvider.buildExtractionService();

        LocalDataProvider localDataProvider = new RecipeLocalDataProvider(database);

        RemoteDataProvider remoteDataProvider = new ExtractionRemoteDataProvider(service);

        NewRecipeContract.Presenter presenter = new NewRecipePresenter(view, localDataProvider, remoteDataProvider, state);

        view.setPresenter(presenter);

    }

}
