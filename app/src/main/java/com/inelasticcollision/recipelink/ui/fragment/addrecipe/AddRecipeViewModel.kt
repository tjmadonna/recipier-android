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

    private val savedStateManager = AddRecipeSavedStateManager(savedStateHandle)

    val loadingState = savedStateManager.loadingState
    val titleState = savedStateManager.titleState
    val favoriteState = savedStateManager.favoriteState
    val selectedImageState = savedStateManager.selectedImageState
    val imagesState = savedStateManager.imagesState
    val notesState = savedStateManager.notesState
    val tagsState = savedStateManager.tagsState

    private val _savedStatusState = MutableLiveData<Unit>()
    val savedStatusState: LiveData<Unit> = _savedStatusState

    private val _errorState = MutableLiveData<AddRecipeErrorState?>()
    val errorState: LiveData<AddRecipeErrorState?> = _errorState

    init {
        if (loadingState.value != false) {
            loadRecipeInfo()
        }
    }

    private fun loadRecipeInfo() {
        savedStateManager.urlState?.let { url ->
            savedStateManager.setLoadingState(true)
            getRecipeInfo.execute(GetRecipeInfoObserver(), url)
        } ?: run {
            _errorState.value = AddRecipeErrorState.NoRecipeUrlProvided
            _errorState.value = null
        }
    }

    fun setTitle(title: String?) {
        savedStateManager.setTitleState(title)
    }

    fun setNotes(notes: String?) {
        savedStateManager.setNotesState(notes)
    }

    fun toggleFavorite() {
        val newValue = !(favoriteState.value ?: false)
        savedStateManager.setFavoriteState(newValue)
    }

    fun setTags(tags: List<String>) {
        savedStateManager.setTagsState(tags)
    }

    fun setSelectedImage(imageUrl: String?) {
        savedStateManager.setSelectedImageState(imageUrl)
        selectedImageState.value
    }

    fun saveRecipe() {
        val title = titleState.value
        val url = savedStateManager.urlState
        if (title != null && url != null) {
            val recipe = Recipe(
                title = title,
                url = url,
                imageUrl = selectedImageState.value,
                favorite = favoriteState.value ?: false,
                notes = notesState.value,
                tags = if (tagsState.value.isNullOrEmpty()) null else tagsState.value
            )
            saveRecipe.execute(SaveRecipeObserver(), recipe)
        }
    }

    // Use case observers

    private inner class GetRecipeInfoObserver : UseCaseObserver<RecipeInfo> {

        override fun onSuccess(value: RecipeInfo) {
            savedStateManager.setTitleState(value.title?.capitalizeWords())
            savedStateManager.setSelectedImageState(value.mainImage)
            savedStateManager.setImagesState(value.images?.toMutableList()?.apply {
                // Add main image to list of images
                value.mainImage?.let { image -> this.add(0, image) }
            })

            value.url?.let { url -> savedStateManager.setUrlState(url) }

            savedStateManager.setLoadingState(false)
        }

        override fun onError(error: Throwable) {
            Log.e("AddRecipe", error.localizedMessage, error)
            savedStateManager.setLoadingState(false)
            _errorState.value = AddRecipeErrorState.ErrorFetchRecipeInfo
        }
    }

    private inner class SaveRecipeObserver : UseCaseObserver<Unit> {

        override fun onSuccess(value: Unit) {
            _savedStatusState.value = Unit
        }

        override fun onError(error: Throwable) {
            Log.e("AddRecipe", error.localizedMessage, error)
            _errorState.value = AddRecipeErrorState.ErrorSavingRecipe
        }
    }
}