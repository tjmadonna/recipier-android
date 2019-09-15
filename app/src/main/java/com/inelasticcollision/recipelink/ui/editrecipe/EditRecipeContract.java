/*
 * NewRecipeContract.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.editrecipe;

import java.util.List;

interface EditRecipeContract {
    interface View extends com.inelasticcollision.recipelink.base.View<Presenter> {

        void showMainImage(String imageUrl);

        void showTitle(String title);

        void showUrl(String url);

        void showImageLoadingIndicator(boolean show);

        void showErrorMessage();

        void showErrorMessage(String errorMessage);

        void showImagePicker(List<String> images);

        void showFab(boolean favorite);

        void showNotes(String notes);

        void showKeywords(List<String> keywords);

        void showCloseDialog();

        void finishView();

    }

    interface Presenter extends com.inelasticcollision.recipelink.base.Presenter {

        void handleLoadRecipe();

        void handleExtractImages(String url);

        void handleChangeImage();

        void handleImagePickerClick(String image);

        void handleFabClick();

        void handleCloseClick();

        void handleCloseView();

        void handleSaveRecipe(String title, String url, String notes, List<String> keywords);

        EditRecipeSavedState handleSavedState();

    }


}
