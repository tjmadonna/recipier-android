package com.inelasticcollision.recipelink.test.factory

import com.inelasticcollision.recipelink.data.model.Recipe

object RecipeDataFactory {

    fun makeRecipe(): Recipe {
        return Recipe(
            title = DataFactory.randomString(),
            url = "https://inelasticcollision.com/${DataFactory.randomUuid()}",
            imageUrl = if (DataFactory.randomBoolean()) DataFactory.randomString() else null,
            favorite = DataFactory.randomBoolean(),
            notes = if (DataFactory.randomBoolean()) DataFactory.randomString() else null,
            tags = if (DataFactory.randomBoolean()) DataFactory.makeStringList(5) else null
        )
    }

    fun makeRecipeList(size: Int = 10): List<Recipe> {
        return mutableListOf<Recipe>().apply {
            repeat(size) {
                add(makeRecipe())
            }
        }
    }
}