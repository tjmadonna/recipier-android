package com.inelasticcollision.recipelink.ui.fragment.searchrecipe

import com.inelasticcollision.recipelink.data.model.Recipe

sealed class SearchRecipeState {
    data class Data(val recipes: List<Recipe>, val keyboardShowing: Boolean) : SearchRecipeState()
    data class NoData(val keyboardShowing: Boolean) : SearchRecipeState()
    object Error : SearchRecipeState()
}

sealed class SearchRecipeIntent {
    data class Search(val text: String?) : SearchRecipeIntent()
    data class ChangeKeyboardShowing(val isShowing: Boolean) : SearchRecipeIntent()
}