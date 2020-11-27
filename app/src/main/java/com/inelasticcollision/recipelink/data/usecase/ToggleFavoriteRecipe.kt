package com.inelasticcollision.recipelink.data.usecase

import com.inelasticcollision.recipelink.data.cache.dao.RecipeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class ToggleFavoriteRecipe @Inject constructor(
    private val recipeDao: RecipeDao,
    dispatchers: UseCaseDispatchers
) : FlowUseCase<Unit, String>(dispatchers) {

    override fun buildUseCaseFlow(params: String?): Flow<Unit> {
        if (params == null) {
            throw IllegalArgumentException("Id cannot be null when attempting to toggle recipe favorite")
        }
        return flow {
            recipeDao.toggleFavoriteRecipeWithId(params)
            emit(Unit)
        }.take(1)
    }
}