/*
 * LocalDataProvider.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/28/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.inelasticcollision.recipelink.data.models.Recipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface LocalDataProvider {

    Observable<List<Recipe>> getAllRecipes();

    Observable<List<Recipe>> getFavoriteRecipes();

    Observable<List<Recipe>> getRecipesBySearchTerm(@Nullable String searchTerm);

    Single<Recipe> getRecipeById(@NonNull String id);

    Completable saveRecipe(@NonNull Recipe recipe);

    Completable updateRecipe(@NonNull Recipe recipe);

    Completable deleteRecipe(@NonNull String id);

}
