package com.inelasticcollision.recipelink.data.cache.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inelasticcollision.recipelink.data.cache.database.AppDatabase
import com.inelasticcollision.recipelink.data.cache.database.AppDatabaseMigration
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.test.factory.DataFactory
import com.inelasticcollision.recipelink.test.factory.RecipeDataFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class RecipeDaoTest {

    private val recipes = RecipeDataFactory.makeRecipeList(10)

    private lateinit var database: AppDatabase

    private val recipeDao: RecipeDao
        get() = database.recipeDao()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .addMigrations(AppDatabaseMigration.MIGRATION_2_3)
            .build()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun getAllRecipesReturnsAllRecipesSortedByTitle() = runBlocking {
        populateDatabaseWithRecipes(recipes)

        val expectedValues = recipes.sortedBy { recipe -> recipe.title }

        val actualValues = recipeDao.getAllRecipes().first()

        Assert.assertEquals(expectedValues, actualValues)
    }

    @Test
    fun getFavoriteRecipesReturnsFavoriteRecipesSortedByTitle() = runBlocking {
        populateDatabaseWithRecipes(recipes)

        val expectedValues = recipes
            .filter { recipe -> recipe.favorite }
            .sortedBy { recipe -> recipe.title }

        val actualValues = recipeDao.getFavoriteRecipes().first()

        Assert.assertEquals(expectedValues, actualValues)
    }

    @Test
    fun getRecipeByIdReturnsRecipe() = runBlocking {
        populateDatabaseWithRecipes(recipes)

        val expectedValue = recipes[DataFactory.randomInt(recipes.lastIndex)]
        val id = expectedValue.id

        val actualValue = recipeDao.getRecipeById(id).first()

        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun toggleFavoriteRecipeWithIdTogglesFavoriteUpdatesLastModified() = runBlocking {
        populateDatabaseWithRecipes(recipes)

        val recipeToChange = recipes[DataFactory.randomInt(recipes.lastIndex)]
        val expectedFavorite = !recipeToChange.favorite // Toggle favorite
        val expectedLastModified =
            Date(recipeToChange.lastModified.time + 5 * 1000) // add 5 seconds

        // Toggle favorite
        recipeDao.toggleFavoriteRecipeWithId(recipeToChange.id, expectedLastModified.time)

        // Check if value was toggled
        val retrievedRecipe = recipeDao.getRecipeById(recipeToChange.id).first()
        Assert.assertEquals(expectedFavorite, retrievedRecipe.favorite)
        Assert.assertEquals(expectedLastModified, retrievedRecipe.lastModified)
    }

    @Test
    fun insertRecipeInsertsRecipe() = runBlocking {
        val expectedRecipe = recipes[DataFactory.randomInt(recipes.lastIndex)]

        recipeDao.insertRecipe(expectedRecipe)

        val actualRecipe = recipeDao.getRecipeById(expectedRecipe.id).first()

        Assert.assertEquals(expectedRecipe, actualRecipe)
    }

    @Test
    fun updateRecipeUpdatesRecipe() = runBlocking {
        populateDatabaseWithRecipes(recipes)
        val recipeIdToUpdate = recipes[DataFactory.randomInt(recipes.lastIndex)].id
        val expectedRecipe = Recipe(
            id = recipeIdToUpdate,
            title = DataFactory.randomString(),
            url = "https://inelasticcollision.com/${DataFactory.randomUuid()}",
            imageUrl = if (DataFactory.randomBoolean()) DataFactory.randomString() else null,
            favorite = DataFactory.randomBoolean(),
            notes = if (DataFactory.randomBoolean()) DataFactory.randomString() else null,
            tags = if (DataFactory.randomBoolean()) DataFactory.makeStringList(5) else null
        )

        recipeDao.updateRecipe(expectedRecipe)

        val actualRecipe = recipeDao.getRecipeById(expectedRecipe.id).first()

        Assert.assertEquals(expectedRecipe, actualRecipe)
    }

    @Test
    fun deleteRecipeDeletesRecipe() = runBlocking {
        populateDatabaseWithRecipes(recipes)
        val recipeToRemove = recipes[DataFactory.randomInt(recipes.lastIndex)]

        recipeDao.deleteRecipeById(recipeToRemove.id)

        val retrievedRecipes = recipeDao.getAllRecipes().first()

        Assert.assertFalse(retrievedRecipes.contains(recipeToRemove))
    }

    private fun populateDatabaseWithRecipes(recipes: List<Recipe>) = runBlocking {
        recipes.forEach { recipe ->
            recipeDao.insertRecipe(recipe)
        }
    }
}