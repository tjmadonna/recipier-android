/*
 * RecipesContract.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.recipes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.inelasticcollision.recipelink.data.models.Recipe;

import java.util.List;

interface RecipesContract {

    interface View extends com.inelasticcollision.recipelink.base.View<Presenter> {

        void showRecipes(@NonNull List<Recipe> recipes);

        void showEmptyContentMessage();

        void showLearnHowWebPage();

    }

    interface Presenter extends com.inelasticcollision.recipelink.base.Presenter {

        void handleLoadRecipes(int sortType, @Nullable String search);

        void handleLearnHowButtonClicked();

    }

}
