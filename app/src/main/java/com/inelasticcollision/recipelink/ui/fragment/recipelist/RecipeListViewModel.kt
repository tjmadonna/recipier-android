package com.inelasticcollision.recipelink.ui.fragment.recipelist

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.data.usecase.GetAllRecipes
import com.inelasticcollision.recipelink.data.usecase.GetFavoriteRecipes
import com.inelasticcollision.recipelink.data.usecase.UseCaseObserver
import kotlinx.parcelize.Parcelize

sealed class RecipeFilterType : Parcelable {
    @Parcelize
    object All : RecipeFilterType()

    @Parcelize
    object Favorites : RecipeFilterType()
}

class RecipeListViewModel @ViewModelInject constructor(
    private val getAllRecipes: GetAllRecipes,
    private val getFavoriteRecipes: GetFavoriteRecipes,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val FILTER_TYPE_STATE = "filterType"
    }

    private val _state = MutableLiveData<RecipeListState>()

    val state: LiveData<RecipeListState>
        get() = _state

    private val filterType: RecipeFilterType?
        get() = savedStateHandle.get<RecipeFilterType>(FILTER_TYPE_STATE)

    init {
        val filterType = this.filterType ?: RecipeFilterType.All
        setFilterType(filterType)
    }

    fun setIntent(intent: RecipeListIntent) {
        when (intent) {
            is RecipeListIntent.SetFilter -> setFilterType(intent.filterType)
        }
    }

    private fun setFilterType(filterType: RecipeFilterType) {
        savedStateHandle.set(FILTER_TYPE_STATE, filterType)
        cancelActiveObservers()
        when (filterType) {
            is RecipeFilterType.All -> getAllRecipes.execute(GetAllRecipesObserver())
            is RecipeFilterType.Favorites -> getFavoriteRecipes.execute(GetFavoriteRecipesObserver())
        }
    }

    private fun cancelActiveObservers() {
        // Cancel all queries
        getAllRecipes.cancel()
        getFavoriteRecipes.cancel()
    }

    override fun onCleared() {
        // Clean up
        getAllRecipes.dispose()
        getFavoriteRecipes.dispose()
        super.onCleared()
    }

    // Use case observers

    private inner class GetAllRecipesObserver : UseCaseObserver<List<Recipe>> {

        override fun onSuccess(value: List<Recipe>) {
            if (value.isNotEmpty()) {
                _state.value = RecipeListState.AllData(value)
            } else {
                _state.value = RecipeListState.AllNoData
            }
        }

        override fun onError(error: Throwable) {
            _state.value = RecipeListState.Error
        }
    }

    private inner class GetFavoriteRecipesObserver : UseCaseObserver<List<Recipe>> {

        override fun onSuccess(value: List<Recipe>) {
            if (value.isNotEmpty()) {
                _state.value = RecipeListState.FavoritesData(value)
            } else {
                _state.value = RecipeListState.FavoritesNoDate
            }
        }

        override fun onError(error: Throwable) {
            _state.value = RecipeListState.Error
        }
    }
}