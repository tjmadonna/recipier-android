package com.inelasticcollision.recipelink.ui.fragment.editrecipe

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.data.model.RecipeInfo
import com.inelasticcollision.recipelink.data.usecase.GetRecipe
import com.inelasticcollision.recipelink.data.usecase.GetRecipeInfo
import com.inelasticcollision.recipelink.data.usecase.UpdateRecipe
import com.inelasticcollision.recipelink.data.usecase.UseCaseObserver

class EditRecipeViewModel @ViewModelInject constructor(
    private val getRecipe: GetRecipe,
    private val getRecipeInfo: GetRecipeInfo,
    private val updateRecipe: UpdateRecipe,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableLiveData<EditRecipeState>()

    val state: LiveData<EditRecipeState>
        get() = _state

    private val savedStateManager = EditRecipeSavedStateManager(savedStateHandle)

    init {
        savedStateManager.idSavedState?.let { id ->
            getRecipe.execute(GetRecipeObserver(), id)
        } ?: run {
            _state.value = EditRecipeState.Error
        }
    }

    private fun loadRecipeInfo(url: String) {
        getRecipeInfo.execute(GetRecipeInfoObserver(), url)
    }

    private fun sendViewData() {
        val viewData = EditRecipeViewData(
            title = savedStateManager.titleSavedState,
            selectedImageUrl = savedStateManager.selectedImageSavedState,
            imageUrls = savedStateManager.imagesSavedState,
            favorite = savedStateManager.favoriteSavedState,
            notes = savedStateManager.notesSavedState,
            tags = savedStateManager.tagsSavedState
        )
        _state.value = EditRecipeState.Data(viewData)
    }

    fun setIntent(intent: EditRecipeIntent) {
        when (intent) {
            is EditRecipeIntent.ChangeTitle -> setTitle(intent.title)
            is EditRecipeIntent.ChangeNotes -> setNotes(intent.notes)
            is EditRecipeIntent.ChangeTags -> setTags(intent.tags)
            is EditRecipeIntent.ToggleFavorite -> toggleFavorite()
            is EditRecipeIntent.SaveRecipe -> saveRecipe()
            is EditRecipeIntent.ChangeImage -> setSelectedImage(intent.imageUrl)
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
        val id = savedStateManager.idSavedState
        val title = savedStateManager.titleSavedState
        val url = savedStateManager.urlSavedState
        if (id != null && title != null && url != null) {
            val recipe = Recipe(
                id = id,
                title = title,
                url = url,
                imageUrl = savedStateManager.selectedImageSavedState,
                favorite = savedStateManager.favoriteSavedState,
                notes = savedStateManager.notesSavedState,
                tags = savedStateManager.tagsSavedState
            )
            updateRecipe.execute(UpdateRecipeObserver(), recipe)
        }
    }

    private inner class GetRecipeObserver : UseCaseObserver<Recipe> {

        override fun onSuccess(value: Recipe) {
            savedStateManager.titleSavedState = value.title
            savedStateManager.urlSavedState = value.url
            savedStateManager.selectedImageSavedState = value.imageUrl
            savedStateManager.favoriteSavedState = value.favorite
            savedStateManager.notesSavedState = value.notes
            savedStateManager.tagsSavedState = value.tags
            sendViewData()

            if (savedStateManager.imagedLoadingSavedState) {
                // Load recipe images
                loadRecipeInfo(value.url)
            }
        }

        override fun onError(error: Throwable) {
            Log.e("EditRecipe", error.localizedMessage, error)
            _state.value = EditRecipeState.Error
        }
    }

    // Use case observers

    private inner class GetRecipeInfoObserver : UseCaseObserver<RecipeInfo> {

        override fun onSuccess(value: RecipeInfo) {
            savedStateManager.imagedLoadingSavedState = false
            savedStateManager.imagesSavedState = value.images?.toMutableList()?.apply {
                if (value.title != null) {
                    // Add main image to beginning of image list
                    this.add(0, value.title)
                }
            }
            sendViewData()
        }

        override fun onError(error: Throwable) {
            savedStateManager.imagedLoadingSavedState = false
        }
    }

    private inner class UpdateRecipeObserver : UseCaseObserver<Unit> {

        override fun onSuccess(value: Unit) {
            _state.value = EditRecipeState.Saved
        }

        override fun onError(error: Throwable) {
            Log.e("UpdateRecipe", error.localizedMessage, error)
            _state.value = EditRecipeState.Error
        }
    }
}