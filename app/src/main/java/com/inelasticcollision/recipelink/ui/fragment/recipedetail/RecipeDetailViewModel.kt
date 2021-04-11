package com.inelasticcollision.recipelink.ui.fragment.recipedetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.data.usecase.DeleteRecipe
import com.inelasticcollision.recipelink.data.usecase.GetRecipe
import com.inelasticcollision.recipelink.data.usecase.ToggleFavoriteRecipe
import com.inelasticcollision.recipelink.data.usecase.UseCaseObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipe: GetRecipe,
    private val toggleFavoriteRecipe: ToggleFavoriteRecipe,
    private val deleteRecipe: DeleteRecipe,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val RECIPE_ID_SAVED_STATE = "recipeId"
    }

    private val _state = MutableLiveData<RecipeDetailState>()

    val state: LiveData<RecipeDetailState>
        get() = _state

    val recipe: Recipe?
        get() = (_state.value as? RecipeDetailState.Data)?.recipe

    private val recipeId: String?
        get() = savedStateHandle.get(RECIPE_ID_SAVED_STATE) ?: run {
            _state.value = RecipeDetailState.Error
            return@run null
        }

    init {
        recipeId?.let { id ->
            getRecipe.execute(GetRecipeObserver(), id)
        }
    }

    fun setIntent(intent: RecipeDetailIntent) {
        when (intent) {
            is RecipeDetailIntent.ToggleFavorite -> toggleFavorite()
            is RecipeDetailIntent.DeleteRecipe -> deleteRecipe()
        }
    }

    private fun toggleFavorite() {
        // Cancel older toggles just in case
        toggleFavoriteRecipe.cancel()
        recipeId?.let { id ->
            toggleFavoriteRecipe.execute(ToggleFavoriteObserver(), id)
        }
    }

    private fun deleteRecipe() {
        recipeId?.let { id ->
            // Cancel get recipe because it will observe recipe being deleted
            getRecipe.cancel()
            deleteRecipe.execute(DeleteRecipeObserver(), id)
        }
    }

    override fun onCleared() {
        // Clean up
        getRecipe.dispose()
        toggleFavoriteRecipe.dispose()
        deleteRecipe.dispose()
        super.onCleared()
    }

    // Use case observers

    private inner class GetRecipeObserver : UseCaseObserver<Recipe> {

        override fun onSuccess(value: Recipe) {
            _state.value = RecipeDetailState.Data(value)
        }

        override fun onError(error: Throwable) {
            _state.value = RecipeDetailState.Error
        }
    }

    private inner class ToggleFavoriteObserver : UseCaseObserver<Unit> {

        override fun onSuccess(value: Unit) {

        }

        override fun onError(error: Throwable) {
            Log.e("RecipeDetail", error.localizedMessage, error)
        }
    }

    private inner class DeleteRecipeObserver : UseCaseObserver<Unit> {

        override fun onSuccess(value: Unit) {
            _state.value = RecipeDetailState.Deleted
        }

        override fun onError(error: Throwable) {
            Log.e("RecipeDetail", error.localizedMessage, error)
        }
    }
}