package com.inelasticcollision.recipelink.ui.common.filtertype

sealed class RecipeFilterType {
    object All : RecipeFilterType()
    object Favorite : RecipeFilterType()
    data class Collection(val collectionName: String) : RecipeFilterType()
}