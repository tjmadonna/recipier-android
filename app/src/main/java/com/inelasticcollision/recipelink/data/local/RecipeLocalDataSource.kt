package com.inelasticcollision.recipelink.data.local

import com.inelasticcollision.recipelink.common.Result
import com.inelasticcollision.recipelink.data.models.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeLocalDataSource {

    fun getAllRecipes(): Flow<List<Recipe>>

    fun getFavoriteRecipes(): Flow<List<Recipe>>

    fun getRecipesWithCollection(collection: String): Flow<List<Recipe>>

    fun getRecipesBySearchTerm(searchTerm: String?): Flow<List<Recipe>>

    suspend fun getRecipeById(id: String): Recipe

    suspend fun saveRecipe(recipe: Recipe): Result<Unit>

    suspend fun updateRecipe(recipe: Recipe): Result<Unit>

    suspend fun deleteRecipe(id: String): Result<Unit>

}