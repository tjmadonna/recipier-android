package com.inelasticcollision.recipelink.ui.fragment.editrecipe

import android.util.Log
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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    private val getRecipe: GetRecipe,
    private val getRecipeInfo: GetRecipeInfo,
    private val updateRecipe: UpdateRecipe,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val savedStateManager = EditRecipeSavedStateManager(savedStateHandle)

    val titleState = savedStateManager.titleState
    val favoriteState = savedStateManager.favoriteState
    val selectedImageState = savedStateManager.selectedImageState
    val imagesState = savedStateManager.imagesState
    val notesState = savedStateManager.notesState
    val tagsState = savedStateManager.tagsState

    private val _savedStatusState = MutableLiveData<Unit>()
    val savedStatusState: LiveData<Unit> = _savedStatusState

    private val _errorState = MutableLiveData<EditRecipeErrorState?>()
    val errorState: LiveData<EditRecipeErrorState?> = _errorState

    init {
        savedStateManager.idState?.let { id ->
            getRecipe.execute(GetRecipeObserver(), id)
        } ?: run {
            _errorState.value = EditRecipeErrorState.RecipeNotFound
        }
    }

    private fun loadRecipeInfo(url: String) {
        getRecipeInfo.execute(GetRecipeInfoObserver(), url)
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
    }

    fun saveRecipe() {
        val id = savedStateManager.idState
        val url = savedStateManager.urlState
        val title = savedStateManager.titleState.value
        if (id != null && url != null && title != null) {
            val recipe = Recipe(
                id = id,
                title = title,
                url = url,
                imageUrl = selectedImageState.value,
                favorite = favoriteState.value ?: false,
                notes = notesState.value,
                tags = if (tagsState.value.isNullOrEmpty()) null else tagsState.value
            )
            updateRecipe.execute(UpdateRecipeObserver(), recipe)
        }
    }

    private inner class GetRecipeObserver : UseCaseObserver<Recipe?> {

        override fun onSuccess(value: Recipe?) {
            if (value == null) {
                _errorState.value = EditRecipeErrorState.ErrorGettingRecipe
                return
            }

            savedStateManager.setTitleState(value.title)
            savedStateManager.setUrlState(value.url)
            savedStateManager.setSelectedImageState(value.imageUrl)
            savedStateManager.setFavoriteState(value.favorite)
            savedStateManager.setNotesState(value.notes)
            savedStateManager.setTagsState(value.tags ?: emptyList())

            if (savedStateManager.imagesLoadingState.value != false) {
                // Load recipe images
                loadRecipeInfo(value.url)
            }
        }

        override fun onError(error: Throwable) {
            Log.e("EditRecipe", error.localizedMessage, error)
            _errorState.value = EditRecipeErrorState.ErrorGettingRecipe
        }
    }

    // Use case observers

    private inner class GetRecipeInfoObserver : UseCaseObserver<RecipeInfo> {

        override fun onSuccess(value: RecipeInfo) {
            savedStateManager.setImagesLoadingState(false)
            savedStateManager.setImagesState(value.images?.toMutableList()?.apply {
                // Add main image to list of images
                value.mainImage?.let { image -> this.add(0, image) }
            })
        }

        override fun onError(error: Throwable) {
            Log.e("EditRecipe", error.localizedMessage, error)
            savedStateManager.setImagesLoadingState(true)
            _errorState.value = EditRecipeErrorState.ErrorFetchRecipeInfo
        }
    }

    private inner class UpdateRecipeObserver : UseCaseObserver<Unit> {

        override fun onSuccess(value: Unit) {
            _savedStatusState.value = Unit
        }

        override fun onError(error: Throwable) {
            Log.e("EditRecipe", error.localizedMessage, error)
            _errorState.value = EditRecipeErrorState.ErrorSavingRecipe
        }
    }
}