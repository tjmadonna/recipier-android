package com.inelasticcollision.recipelink.ui.fragment.addrecipe

import android.content.Intent
import androidx.lifecycle.SavedStateHandle

class AddRecipeSavedStateManager(
    private val savedStateHandle: SavedStateHandle
) {

    companion object {
        private const val URL_SAVED_STATE = Intent.EXTRA_TEXT
        private const val LOADING_SAVED_STATE = "loadingSavedState"
        private const val TITLE_SAVED_STATE = "titleSavedState"
        private const val SELECTED_IMAGE_SAVED_STATE = "selectedSavedImageState"
        private const val IMAGES_SAVED_STATE = "imagesSavedState"
        private const val FAVORITE_SAVED_STATE = "favoriteSavedState"
        private const val NOTES_SAVED_STATE = "notesSavedState"
        private const val TAGS_SAVED_STATE = "tagsSavedState"
    }

    var urlSavedState: String?
        get() = savedStateHandle.get(URL_SAVED_STATE)
        set(value) = savedStateHandle.set(URL_SAVED_STATE, value)

    var loadingSavedState: Boolean
        get() = savedStateHandle.get(LOADING_SAVED_STATE) ?: true
        set(value) = savedStateHandle.set(LOADING_SAVED_STATE, value)

    var titleSavedState: String?
        get() = savedStateHandle.get(TITLE_SAVED_STATE)
        set(value) = savedStateHandle.set(TITLE_SAVED_STATE, value)

    var selectedImageSavedState: String?
        get() = savedStateHandle.get(SELECTED_IMAGE_SAVED_STATE)
        set(value) = savedStateHandle.set(SELECTED_IMAGE_SAVED_STATE, value)

    var imagesSavedState: List<String>?
        get() = savedStateHandle.get(IMAGES_SAVED_STATE)
        set(value) = savedStateHandle.set(IMAGES_SAVED_STATE, value)

    var favoriteSavedState: Boolean
        get() = savedStateHandle.get(FAVORITE_SAVED_STATE) ?: false
        set(value) = savedStateHandle.set(FAVORITE_SAVED_STATE, value)

    var notesSavedState: String?
        get() = savedStateHandle.get(NOTES_SAVED_STATE)
        set(value) = savedStateHandle.set(NOTES_SAVED_STATE, value)

    var tagsSavedState: List<String>?
        get() = savedStateHandle.get(TAGS_SAVED_STATE)
        set(value) = savedStateHandle.set(TAGS_SAVED_STATE, value)
}