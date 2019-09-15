/*
 * LocalDataProvider.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/28/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import androidx.annotation.Nullable;

import com.inelasticcollision.recipelink.data.models.Recipe;

import java.util.List;

import rx.Observable;

public interface LocalDataProvider {

    Observable<List<Recipe>> loadAllRecipes();

    Observable<List<Recipe>> loadFavoriteRecipes();

    Observable<List<Recipe>> searchRecipes(@Nullable String searchTerm);

    Observable<Recipe> loadRecipe(int id);

    Observable<Long> saveRecipe(Recipe recipe);

    Observable<Boolean> deleteRecipe(int id);

}
