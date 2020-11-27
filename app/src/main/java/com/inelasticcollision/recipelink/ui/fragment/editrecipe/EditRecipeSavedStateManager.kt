package com.inelasticcollision.recipelink.ui.fragment.editrecipe

import androidx.lifecycle.SavedStateHandle

class EditRecipeSavedStateManager(
    private val savedStateHandle: SavedStateHandle
) {

    companion object {
        private const val ID_SAVED_STATE = "recipeId"
        private const val IMAGES_LOADING_SAVED_STATE = "imageLoadingSavedState"
        private const val URL_SAVED_STATE = "urlSavedState"
        private const val TITLE_SAVED_STATE = "titleSavedState"
        private const val SELECTED_IMAGE_SAVED_STATE = "selectedSavedImageState"
        private const val IMAGES_SAVED_STATE = "imagesSavedState"
        private const val FAVORITE_SAVED_STATE = "favoriteSavedState"
        private const val NOTES_SAVED_STATE = "notesSavedState"
        private const val TAGS_SAVED_STATE = "tagsSavedState"
    }

    var idSavedState: String?
        get() = savedStateHandle.get(ID_SAVED_STATE)
        set(value) = savedStateHandle.set(ID_SAVED_STATE, value)

    var imagedLoadingSavedState: Boolean
        get() = savedStateHandle.get(IMAGES_LOADING_SAVED_STATE) ?: true
        set(value) = savedStateHandle.set(IMAGES_LOADING_SAVED_STATE, value)

    var urlSavedState: String?
        get() = savedStateHandle.get(URL_SAVED_STATE)
        set(value) = savedStateHandle.set(URL_SAVED_STATE, value)

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