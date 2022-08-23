package com.inelasticcollision.recipelink.data.repository

import com.inelasticcollision.recipelink.data.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllRecipes(): Flow<List<Recipe>>

    fun getFavoriteRecipes(): Flow<List<Recipe>>

}