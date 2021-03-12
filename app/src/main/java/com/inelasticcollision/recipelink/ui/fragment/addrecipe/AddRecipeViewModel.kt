package com.inelasticcollision.recipelink.ui.fragment.addrecipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.data.model.RecipeInfo
import com.inelasticcollision.recipelink.data.usecase.GetRecipeInfo
import com.inelasticcollision.recipelink.data.usecase.SaveRecipe
import com.inelasticcollision.recipelink.data.usecase.UseCaseObserver
import com.inelasticcollision.recipelink.util.capitalizeWords
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val getRecipeInfo: GetRecipeInfo,
    private val saveRecipe: SaveRecipe,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableLiveData<AddRecipeState>()

    val state: LiveData<AddRecipeState>
        get() = _state

    private val savedStateManager = AddRecipeSavedStateManager(savedStateHandle)

    init {
        if (savedStateManager.loadingSavedState) {
            // Load recipe info
            loadRecipeInfo()
        } else {
            // Send view data
            sendViewData()
        }
    }

    private fun loadRecipeInfo() {
        savedStateManager.urlSavedState?.let { url ->
            _state.value = AddRecipeState.Loading
            getRecipeInfo.execute(GetRecipeInfoObserver(), url)
        } ?: run {
            _state.value = AddRecipeState.Error
        }
    }

    private fun sendViewData() {
        val viewData = AddRecipeViewData(
            title = savedStateManager.titleSavedState,
            selectedImageUrl = savedStateManager.selectedImageSavedState,
            imageUrls = savedStateManager.imagesSavedState,
            favorite = savedStateManager.favoriteSavedState,
            notes = savedStateManager.notesSavedState,
            tags = savedStateManager.tagsSavedState
        )
        _state.value = AddRecipeState.Data(viewData)
    }

    fun setIntent(intent: AddRecipeIntent) {
        when (intent) {
            is AddRecipeIntent.ChangeTitle -> setTitle(intent.title)
            is AddRecipeIntent.ChangeNotes -> setNotes(intent.notes)
            is AddRecipeIntent.ChangeTags -> setTags(intent.tags)
            is AddRecipeIntent.ToggleFavorite -> toggleFavorite()
            is AddRecipeIntent.SaveRecipe -> saveRecipe()
            is AddRecipeIntent.ChangeImage -> setSelectedImage(intent.imageUrl)
        }
    }

    private fun setTitle(title: String?) {
        savedStateManager.titleSavedState = title
        sendViewData()
    }

    private fun setNotes(notes: String?) {
        savedStateManager.notesSavedState = notes
        sendViewData()
    }

    private fun toggleFavorite() {
        savedStateManager.favoriteSavedState = !savedStateManager.favoriteSavedState
        sendViewData()
    }

    private fun setTags(tags: List<String>?) {
        savedStateManager.tagsSavedState = tags
        sendViewData()
    }

    private fun setSelectedImage(imageUrl: String) {
        savedStateManager.selectedImageSavedState = imageUrl
        sendViewData()
    }

    private fun saveRecipe() {
        val title = savedStateManager.titleSavedState
        val url = savedStateManager.urlSavedState
        if (title != null && url != null) {
            val recipe = Recipe(
                title = title,
                url = url,
                imageUrl = savedStateManager.selectedImageSavedState,
                favorite = savedStateManager.favoriteSavedState,
                notes = savedStateManager.notesSavedState,
                tags = savedStateManager.tagsSavedState
            )
            saveRecipe.execute(SaveRecipeObserver(), recipe)
        }
    }

    // Use case observers

    private inner class GetRecipeInfoObserver : UseCaseObserver<RecipeInfo> {

        override fun onSuccess(value: RecipeInfo) {
            savedStateManager.titleSavedState = value.title?.capitalizeWords()
            savedStateManager.selectedImageSavedState = value.mainImage
            savedStateManager.imagesSavedState = value.images?.toMutableList()?.apply {
                if (value.title != null) {
                    // Add main image to beginning of image list
                    this.add(0, value.title)
                }
            }

            value.url?.let { url ->
                savedStateManager.urlSavedState = url
            }
            savedStateManager.loadingSavedState = false

            sendViewData()
        }

        override fun onError(error: Throwable) {
            Log.e("AddRecipe", error.localizedMessage, error)
            savedStateManager.loadingSavedState = false
            _state.value = AddRecipeState.Error
        }
    }

    private inner class SaveRecipeObserver : UseCaseObserver<Unit> {

        override fun onSuccess(value: Unit) {
            _state.value = AddRecipeState.Saved
        }

        override fun onError(error: Throwable) {
            Log.e("AddRecipe", error.localizedMessage, error)
            _state.value = AddRecipeState.Error
        }
    }
}