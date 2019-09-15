/*
 * NewRecipeInjection.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.editrecipe;

import android.content.Context;

import com.inelasticcollision.recipelink.data.local.helper.BriteDatabaseHelper;
import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.provider.RecipeLocalDataProvider;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.data.remote.RemoteDataProvider;
import com.inelasticcollision.recipelink.data.remote.ExtractionService;
import com.inelasticcollision.recipelink.data.remote.ExtractionRemoteDataProvider;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;

class EditRecipeInjection {

    static void inject(Context context, EditRecipeContract.View view, int recipeId) {

        EditRecipeSavedState state = new EditRecipeSavedState(recipeId, Recipe.DATE_SAVED_NEVER, "", false, new ArrayList<String>(0), "", true, true);

        inject(context, view, state);

    }

    static void inject(Context context, EditRecipeContract.View view, EditRecipeSavedState state) {

        BriteDatabase database = BriteDatabaseHelper.getInstance(context);

        ExtractionService service = ExtractionRemoteDataProvider.buildExtractionService();

        LocalDataProvider localDataProvider = new RecipeLocalDataProvider(database);

        RemoteDataProvider remoteDataProvider = new ExtractionRemoteDataProvider(service);

        EditRecipeContract.Presenter presenter = new EditRecipePresenter(view, localDataProvider, remoteDataProvider, state);

        view.setPresenter(presenter);

    }

}
