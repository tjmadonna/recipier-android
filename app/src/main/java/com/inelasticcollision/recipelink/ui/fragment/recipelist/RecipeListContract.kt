package com.inelasticcollision.recipelink.ui.fragment.recipelist

import com.inelasticcollision.recipelink.data.model.Recipe

sealed class RecipeListState {
    data class AllData(val recipes: List<Recipe>) : RecipeListState()
    data class FavoritesData(val recipes: List<Recipe>) : RecipeListState()
    object AllNoData : RecipeListState()
    object FavoritesNoDate : RecipeListState()
    object Error : RecipeListState()
}

sealed class RecipeListIntent {
    data class SetFilter(val filterType: RecipeFilterType) : RecipeListIntent()
}