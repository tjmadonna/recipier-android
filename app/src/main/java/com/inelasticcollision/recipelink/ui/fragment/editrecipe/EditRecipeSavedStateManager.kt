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

    // Recipe id

    val idState: String?
        get() = savedStateHandle.get<String>(ID_SAVED_STATE)

    // Url

    val urlState: String?
        get() = savedStateHandle.get<String>(URL_SAVED_STATE)

    fun setUrlState(url: String) {
        if (savedStateHandle.get<String>(URL_SAVED_STATE) != url) {
            savedStateHandle.set(URL_SAVED_STATE, url)
        }
    }

    // Images Loading

    val imagesLoadingState = savedStateHandle.getLiveData(IMAGES_LOADING_SAVED_STATE, true)

    fun setImagesLoadingState(loading: Boolean) {
        if (savedStateHandle.get<Boolean>(IMAGES_LOADING_SAVED_STATE) != loading) {
            savedStateHandle.set(IMAGES_LOADING_SAVED_STATE, loading)
        }
    }

    // Title

    val titleState = savedStateHandle.getLiveData<String?>(TITLE_SAVED_STATE)

    fun setTitleState(title: String?) {
        if (savedStateHandle.get<String>(TITLE_SAVED_STATE) != title) {
            savedStateHandle.set(TITLE_SAVED_STATE, title)
        }
    }

    // Favorite

    val favoriteState = savedStateHandle.getLiveData(FAVORITE_SAVED_STATE, false)

    fun setFavoriteState(favorite: Boolean) {
        if (savedStateHandle.get<Boolean>(FAVORITE_SAVED_STATE) != favorite) {
            savedStateHandle.set(FAVORITE_SAVED_STATE, favorite)
        }
    }

    // Selected Image

    val selectedImageState = savedStateHandle.getLiveData<String?>(SELECTED_IMAGE_SAVED_STATE)

    fun setSelectedImageState(selectedImage: String?) {
        if (savedStateHandle.get<String>(SELECTED_IMAGE_SAVED_STATE) != selectedImage) {
            savedStateHandle.set(SELECTED_IMAGE_SAVED_STATE, selectedImage)
        }
    }

    // Images

    val imagesState = savedStateHandle.getLiveData<List<String>?>(IMAGES_SAVED_STATE, emptyList())

    fun setImagesState(images: List<String>?) {
        if (savedStateHandle.get<List<String>>(IMAGES_SAVED_STATE) != images) {
            savedStateHandle.set(IMAGES_SAVED_STATE, images)
        }
    }

    // Notes

    val notesState = savedStateHandle.getLiveData<String?>(NOTES_SAVED_STATE)

    fun setNotesState(notes: String?) {
        if (savedStateHandle.get<String>(NOTES_SAVED_STATE) != notes) {
            savedStateHandle.set(NOTES_SAVED_STATE, notes)
        }
    }

    // Tags

    val tagsState = savedStateHandle.getLiveData<List<String>>(TAGS_SAVED_STATE)

    fun setTagsState(tags: List<String>) {
        if (savedStateHandle.get<List<String>>(TAGS_SAVED_STATE) != tags) {
            savedStateHandle.set(TAGS_SAVED_STATE, tags)
        }
    }
}