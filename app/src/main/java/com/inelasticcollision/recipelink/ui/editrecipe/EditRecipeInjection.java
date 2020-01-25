/*
 * NewRecipeInjection.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.editrecipe;

import android.content.Context;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.remote.RemoteDataProvider;
import com.inelasticcollision.recipelink.di.AppDependencyProvider;

import java.util.ArrayList;

class EditRecipeInjection {

    static void inject(Context context, EditRecipeContract.View view, String recipeId) {

        EditRecipeSavedState state = new EditRecipeSavedState(recipeId, "", false, new ArrayList<String>(0), "", true, true);

        inject(context, view, state);

    }

    static void inject(Context context, EditRecipeContract.View view, EditRecipeSavedState state) {

        LocalDataProvider localDataProvider = AppDependencyProvider.provideLocalDataProvider(context);

        RemoteDataProvider remoteDataProvider = AppDependencyProvider.provideRemoteDataProvider();

        EditRecipeContract.Presenter presenter = new EditRecipePresenter(view, localDataProvider, remoteDataProvider, state);

        view.setPresenter(presenter);

    }

}
