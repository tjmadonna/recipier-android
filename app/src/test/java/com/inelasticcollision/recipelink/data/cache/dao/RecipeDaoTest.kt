package com.inelasticcollision.recipelink.data.cache.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inelasticcollision.recipelink.data.cache.database.AppDatabase
import com.inelasticcollision.recipelink.data.model.Recipe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class RecipeDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var SUT: RecipeDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        SUT = database.recipeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllRecipes_withRecipes_returnsFlowOfAllRecipes() = runBlocking {
        // Arrange
        val recipes = arrayListOf(
            Recipe(title = "My Recipe 1", url = "https://example.com", favorite = true),
            Recipe(title = "Sample Recipe 2", url = "https://example.com", favorite = false),
            Recipe(title = "A New Recipe", url = "https://example.com", favorite = true)
        )
        SUT.insertRecipes(recipes)
        val expectedRecipes =
            arrayListOf(recipes[2], recipes[0], recipes[1]) // sorted by title

        // Act
        val actualRecipes = SUT.getAllRecipes()
            .first()

        // Assert
        assertEquals(expectedRecipes, actualRecipes)
    }

    @Test
    fun getAllRecipes_withNoRecipes_returnsEmptyFlow() = runBlocking {
        // Arrange
        val expectedRecipes = emptyList<Recipe>()

        // Act
        val actualRecipes = SUT.getAllRecipes()
            .first()

        // Assert
        assertEquals(expectedRecipes, actualRecipes)
    }

    @Test
    fun getFavoriteRecipes_withFavoriteRecipes_returnsFlowOfFavoriteRecipes() = runBlocking {
        // Arrange
        val recipes = arrayListOf(
            Recipe(title = "My Recipe 1", url = "https://example.com", favorite = true),
            Recipe(title = "Sample Recipe 2", url = "https://example.com", favorite = false),
            Recipe(title = "A New Recipe", url = "https://example.com", favorite = true)
        )
        SUT.insertRecipes(recipes)
        val expectedRecipes =
            arrayListOf(recipes[2], recipes[0]) // sorted by title

        // Act
        val actualRecipes = SUT.getFavoriteRecipes()
            .first()

        // Assert
        assertEquals(expectedRecipes, actualRecipes)
    }

    @Test
    fun getFavoriteRecipes_withNoFavoriteRecipes_returnsEmptyFlow() = runBlocking {
        // Arrange
        val recipes = arrayListOf(
            Recipe(title = "My Recipe 1", url = "https://example.com", favorite = false),
            Recipe(title = "Sample Recipe 2", url = "https://example.com", favorite = false),
            Recipe(title = "A New Recipe", url = "https://example.com", favorite = false)
        )
        SUT.insertRecipes(recipes)
        val expectedRecipes = emptyList<Recipe>()

        // Act
        val actualRecipes = SUT.getFavoriteRecipes()
            .first()

        // Assert
        assertEquals(expectedRecipes, actualRecipes)
    }

    @Test
    fun getRecipesBySearchTerm_withSearchTerm_returnsFlowOfRecipesWithTermInTitle() = runBlocking {
        // Arrange
        val recipes = arrayListOf(
            Recipe(title = "Baked Corn", url = "https://example.com"),
            Recipe(title = "Chocolate Chip Cookies", url = "https://example.com"),
            Recipe(title = "Cookie Parfait", url = "https://example.com"),
        )
        SUT.insertRecipes(recipes)
        val expectedRecipes =
            arrayListOf(recipes[1], recipes[2]) // sorted by title

        // Act
        val actualRecipes = SUT.getRecipesBySearchTerm("%Cookie%")
            .first()

        // Assert
        assertEquals(expectedRecipes, actualRecipes)
    }

    @Test
    fun getRecipesBySearchTerm_withSearchTerm_returnsFlowOfRecipesWithTermInUrl() = runBlocking {
        // Arrange
        val recipes = arrayListOf(
            Recipe(title = "Baked Cookies", url = "https://cookierecipes.com"),
            Recipe(title = "Fried Cookies", url = "https://mycookies.com"),
            Recipe(title = UUID.randomUUID().toString(), url = "https://example.com"),
        )
        SUT.insertRecipes(recipes)
        val expectedRecipes =
            arrayListOf(recipes[0], recipes[1]) // sorted by title

        // Act
        val actualRecipes = SUT.getRecipesBySearchTerm("%Cookie%")
            .first()

        // Assert
        assertEquals(expectedRecipes, actualRecipes)
    }

    @Test
    fun getRecipesBySearchTerm_withSearchTerm_returnsFlowOfRecipesWithTermInTags() = runBlocking {
        // Arrange
        val recipes = arrayListOf(
            Recipe(
                title = "Example Recipe",
                url = "https://example.com",
                tags = arrayListOf("Cookie cake")
            ),
            Recipe(
                title = "A New Recipe",
                url = "https://example.com",
                tags = arrayListOf("cookies")
            ),
            Recipe(title = UUID.randomUUID().toString(), url = "https://example.com"),
        )
        SUT.insertRecipes(recipes)
        val expectedRecipes =
            arrayListOf(recipes[1], recipes[0]) // sorted by title

        // Act
        val actualRecipes = SUT.getRecipesBySearchTerm("%Cookie%")
            .first()

        // Assert
        assertEquals(expectedRecipes, actualRecipes)
    }

    @Test
    fun getRecipeById_withRecipe_returnsFlowWithRecipe() = runBlocking {
        // Arrange
        val id = UUID.randomUUID().toString()
        val expectedRecipe = Recipe(
            id = id,
            title = UUID.randomUUID().toString(),
            url = UUID.randomUUID().toString()
        )
        SUT.insertRecipe(expectedRecipe)

        // Act
        val actualRecipe = SUT.getRecipeById(id)
            .first()

        // Assert
        assertEquals(expectedRecipe, actualRecipe)
    }

    @Test
    fun getRecipeById_withNoRecipe_returnsFlowWithNull() = runBlocking {
        // Arrange
        val id = UUID.randomUUID().toString()

        // Act
        val actualRecipe = SUT.getRecipeById(id)
            .first()

        // Assert
        assertNull(actualRecipe)
    }

    @Test
    fun toggleFavoriteRecipeWithId_withRecipe_togglesFavoriteFieldOfRecipe() = runBlocking {
        // Arrange
        val id = UUID.randomUUID().toString()
        val recipe = Recipe(
            id = id,
            title = UUID.randomUUID().toString(),
            url = UUID.randomUUID().toString(),
            favorite = true
        )
        SUT.insertRecipe(recipe)

        // Act
        SUT.toggleFavoriteRecipeWithId(id)

        // Assert
        val queriedRecipe = SUT.getRecipeById(id).first()
        assertFalse(queriedRecipe!!.favorite)
    }

    @Test
    fun toggleFavoriteRecipeWithId_withRecipe_updatesLastModifiedField() = runBlocking {
        // Arrange
        val id = UUID.randomUUID().toString()
        val expectedLastModified = Date().time
        val recipe = Recipe(
            id = id,
            title = UUID.randomUUID().toString(),
            url = UUID.randomUUID().toString(),
            favorite = true
        )
        SUT.insertRecipe(recipe)

        // Act
        SUT.toggleFavoriteRecipeWithId(id, lastModified = expectedLastModified)

        // Assert
        val queriedRecipe = SUT.getRecipeById(id).first()
        assertEquals(expectedLastModified, queriedRecipe!!.lastModified.time)
    }

    @Test
    fun deleteRecipeById_withRecipe_deletesRecipe() = runBlocking {
        // Arrange
        val id = UUID.randomUUID().toString()
        val recipe = Recipe(
            id = id,
            title = UUID.randomUUID().toString(),
            url = UUID.randomUUID().toString()
        )
        SUT.insertRecipe(recipe)

        // Act
        SUT.deleteRecipeById(id)

        // Assert
        val queriedRecipe = SUT.getRecipeById(id).first()
        assertNull(queriedRecipe)
    }

}