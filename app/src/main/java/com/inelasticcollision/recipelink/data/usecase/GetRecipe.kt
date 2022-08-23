package com.inelasticcollision.recipelink.data.usecase

import com.inelasticcollision.recipelink.data.cache.dao.RecipeDao
import com.inelasticcollision.recipelink.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipe @Inject constructor(
    private val recipeDao: RecipeDao,
    dispatchers: UseCaseDispatchers
) : FlowUseCase<Recipe?, String>(dispatchers) {

    override fun buildUseCaseFlow(params: String?): Flow<Recipe?> {
        if (params == null) {
            throw IllegalArgumentException("Id cannot be null when attempting to get recipe")
        }
        return recipeDao.getRecipeById(params)
    }
}