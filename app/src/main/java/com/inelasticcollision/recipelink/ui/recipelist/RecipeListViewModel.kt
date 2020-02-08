package com.inelasticcollision.recipelink.ui.recipelist

import androidx.lifecycle.*
import com.inelasticcollision.recipelink.data.local.RecipeLocalDataSource
import com.inelasticcollision.recipelink.ui.common.filtertype.RecipeFilterType
import com.inelasticcollision.recipelink.data.models.Recipe
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


class RecipeListViewModel(
        private val state: SavedStateHandle,
        private val recipeLocalDataSource: RecipeLocalDataSource
) : ViewModel() {

    companion object {
        private const val FILTER_TYPE_STATE = "filter_type_state"
    }

    private var job: Job? = null

    private val _filterType = MutableLiveData<RecipeFilterType>(
            state.get(FILTER_TYPE_STATE) ?: RecipeFilterType.All
    )

    val filterType: LiveData<RecipeFilterType>
        get() = _filterType

    private val _recipes = MutableLiveData<List<Recipe>>()

    val recipes: LiveData<List<Recipe>>
        get() = _recipes

    fun setFilterType(filterType: RecipeFilterType) {
        _filterType.value = filterType
        state.set(FILTER_TYPE_STATE, filterType)
        getRecipesForFilterType(filterType)
    }

    private fun getRecipesForFilterType(filterType: RecipeFilterType) {
        job?.cancel()
        job = viewModelScope.launch {

            when (filterType) {
                is RecipeFilterType.All -> recipeLocalDataSource.getAllRecipes()
                is RecipeFilterType.Favorite -> recipeLocalDataSource.getFavoriteRecipes()
                is RecipeFilterType.Collection -> recipeLocalDataSource.getRecipesWithCollection(filterType.collectionName)
            }.collect {
                _recipes.value = it
            }
        }
    }

    override fun onCleared() {
        job?.cancel() // Probably not necessary
        super.onCleared()
    }

}