package com.inelasticcollision.recipelink.data.remote

import com.inelasticcollision.recipelink.data.models.Recipe

interface RecipeRemoteDataSource {

    suspend fun getRecipe(url: String): Recipe

}