package com.inelasticcollision.recipelink.data.cache.dao

import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.inelasticcollision.recipelink.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface RecipeDao {

    // Recipe methods
    @Query("SELECT * FROM recipes ORDER BY title COLLATE NOCASE ASC")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE favorite = 1 ORDER BY title COLLATE NOCASE ASC")
    fun getFavoriteRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE title LIKE :searchTerm OR url LIKE :searchTerm OR tags LIKE :searchTerm ORDER BY title COLLATE NOCASE ASC")
    fun getRecipesBySearchTerm(searchTerm: String): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeById(id: String): Flow<Recipe?>

    @Query("UPDATE recipes SET favorite = ((favorite | 1) - (favorite & 1)), last_modified = :lastModified WHERE id = :id")
    suspend fun toggleFavoriteRecipeWithId(id: String, lastModified: Long = Date().time)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRecipe(recipe: Recipe)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateRecipe(recipe: Recipe)

    @Query("DELETE FROM recipes WHERE id = :id")
    suspend fun deleteRecipeById(id: String)

    @VisibleForTesting
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRecipes(recipes: List<Recipe>)
}