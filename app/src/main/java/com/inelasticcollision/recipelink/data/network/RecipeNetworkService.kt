package com.inelasticcollision.recipelink.data.network

import com.inelasticcollision.recipelink.BuildConfig
import com.inelasticcollision.recipelink.data.model.RecipeInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeNetworkService {

    @GET(BuildConfig.NETWORK_SERVICE_RECIPE_INFO_ROUTE)
    suspend fun getRecipeInfo(@Query("url") url: String): RecipeInfo

}