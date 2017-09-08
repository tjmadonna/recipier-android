/*
 * RecipeDetailContract.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.recipedetail;

import java.util.List;

interface RecipeDetailContract {

    interface View extends com.inelasticcollision.recipelink.base.View<Presenter> {

        void showMainImage(String imageUrl);

        void showTitle(String title);

        void showUrl(String url);

        void showNotes(String notes);

        void showEmptyNotes();

        void showFavoriteIcon(boolean favorite);

        void showKeywords(List<String> keywords);

        void showEmptyKeywords();

        void showImageLoadingIndicator(boolean show);

        void showErrorMessage();

        void showEditRecipeActivity(int recipeId);

        void showDeleteRecipeDialog();

        void finishView();

    }

    interface Presenter extends com.inelasticcollision.recipelink.base.Presenter {

        void handleLoadRecipes();

        void handleEditRecipeClick();

        void handleDeleteRecipeClick();

        void handleDeleteRecipe();

    }

}
