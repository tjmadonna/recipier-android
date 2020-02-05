package com.inelasticcollision.recipelink.data.local

import androidx.lifecycle.LiveData
import com.inelasticcollision.recipelink.data.models.Recipe

interface RecipeLocalDataSource {

    fun getAllRecipes(): LiveData<List<Recipe>>

    fun getFavoriteRecipes(): LiveData<List<Recipe>>

    fun getRecipesWithCollection(collection: String): LiveData<List<Recipe>>

    fun getRecipesBySearchTerm(searchTerm: String?): LiveData<List<Recipe>>

    fun getRecipeById(id: String, callback: (Result<Recipe>) -> Unit)

    fun saveRecipe(recipe: Recipe, callback: (Throwable?) -> Unit)

    fun updateRecipe(recipe: Recipe, callback: (Throwable?) -> Unit)

    fun deleteRecipe(id: String, callback: (Throwable?) -> Unit)

}