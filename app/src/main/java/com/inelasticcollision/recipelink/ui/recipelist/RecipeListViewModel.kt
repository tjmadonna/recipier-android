package com.inelasticcollision.recipelink.ui.recipelist

import androidx.lifecycle.*
import com.inelasticcollision.recipelink.data.local.RecipeLocalDataSource
import com.inelasticcollision.recipelink.ui.common.filtertype.RecipeFilterType
import com.inelasticcollision.recipelink.ui.recipelist.state.RecipeListState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


class RecipeListViewModel(
        private val savedState: SavedStateHandle,
        private val recipeLocalDataSource: RecipeLocalDataSource
) : ViewModel() {

    companion object {
        private const val FILTER_TYPE_STATE = "filter_type_state"
    }

    private var job: Job? = null

    private val _state = MutableLiveData<RecipeListState>()

    val state: LiveData<RecipeListState>
        get() = _state

    init {
        val savedFilterType = savedState.get<RecipeFilterType>(FILTER_TYPE_STATE)
                ?: RecipeFilterType.All
        setFilterType(savedFilterType)
    }

    fun setFilterType(filterType: RecipeFilterType) {
        savedState.set(FILTER_TYPE_STATE, filterType)
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
                if (it.isNotEmpty()) {
                    _state.value = RecipeListState.Data(filterType, it)
                } else {
                    _state.value = RecipeListState.EmptyData(filterType)
                }
            }
        }
    }

    override fun onCleared() {
        job?.cancel() // Probably not necessary
        super.onCleared()
    }

}