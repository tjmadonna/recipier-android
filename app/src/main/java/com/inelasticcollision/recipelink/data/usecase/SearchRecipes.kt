package com.inelasticcollision.recipelink.data.usecase

import com.inelasticcollision.recipelink.data.cache.dao.RecipeDao
import com.inelasticcollision.recipelink.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRecipes @Inject constructor(
    private val recipeDao: RecipeDao,
    dispatchers: UseCaseDispatchers
) : FlowUseCase<List<Recipe>, String>(dispatchers) {

    override fun buildUseCaseFlow(params: String?): Flow<List<Recipe>> {
        return if (params.isNullOrEmpty()) {
            recipeDao.getAllRecipes()
        } else {
            recipeDao.getRecipesBySearchTerm("%$params%")
        }
    }
}