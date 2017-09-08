/*
 * NewRecipeContract.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.newrecipe;

import java.util.List;

interface NewRecipeContract {
    interface View extends com.inelasticcollision.recipelink.base.View<Presenter> {

        void showMainImage(String imageUrl);

        void showTitle(String title);

        void showUrl(String url);

        void showMainLoadingIndicator(boolean show);

        void showImageLoadingIndicator(boolean show);

        void showExtractionErrorMessage();

        void showDatabaseErrorMessage();

        void showImagePicker(List<String> images);

        void showFab(boolean favorite);

        void showCloseDialog();

        void finishView();

    }

    interface Presenter extends com.inelasticcollision.recipelink.base.Presenter {

        void handleExtractRecipe(String title, String url);

        void handleChangeImage();

        void handleImagePickerClick(String image);

        void handleFabClick();

        void handleCloseClick();

        void handleCloseView();

        void handleSaveRecipe(String title, String url, String notes, List<String> keywords);

        NewRecipeSavedState handleSavedState();

    }


}
