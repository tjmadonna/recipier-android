package com.inelasticcollision.recipelink.ui.recipelist.state

import com.inelasticcollision.recipelink.data.models.Recipe
import com.inelasticcollision.recipelink.ui.common.filtertype.RecipeFilterType

sealed class RecipeListState {
    data class EmptyData(val filterType: RecipeFilterType) : RecipeListState()
    data class Data(val filterType: RecipeFilterType, val recipes: List<Recipe>) : RecipeListState()
}