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
import com.inelasticcollision.recipelink.data.remote.RemoteDataProvider;
import com.inelasticcollision.recipelink.di.AppDependencyProvider;

import java.util.ArrayList;

class NewRecipeInjection {

    static void inject(Context context, NewRecipeContract.View view, String title, String url) {

        NewRecipeSavedState state = new NewRecipeSavedState(new ArrayList<String>(0), "", false, true, title, url);

        inject(context, view, state);

    }

    static void inject(Context context, NewRecipeContract.View view, NewRecipeSavedState state) {

        LocalDataProvider localDataProvider = AppDependencyProvider.provideLocalDataProvider(context);

        RemoteDataProvider remoteDataProvider = AppDependencyProvider.provideRemoteDataProvider();

        NewRecipeContract.Presenter presenter = new NewRecipePresenter(view, localDataProvider, remoteDataProvider, state);

        view.setPresenter(presenter);

    }

}
