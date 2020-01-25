package com.inelasticcollision.recipelink.data.local.dao;

import android.database.Observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.inelasticcollision.recipelink.data.models.Recipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY title COLLATE NOCASE")
    public abstract Observable<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE favorite = 1 ORDER BY title COLLATE NOCASE")
    public abstract Observable<List<Recipe>> getFavoriteRecipes();

    @Transaction
    public Observable<List<Recipe>> getRecipesBySearchTerm(@Nullable String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return getAllRecipes();
        } else {
            return searchRecipes(searchTerm);
        }
    }

    @Query("SELECT * FROM recipes WHERE title LIKE :searchTerm OR tags LIKE :searchTerm ORDER BY title COLLATE NOCASE")
    abstract Observable<List<Recipe>> searchRecipes(@NonNull String searchTerm);

    @Query("SELECT * FROM recipes WHERE id = :id")
    public abstract Single<Recipe> getRecipeById(@NonNull String id);

    @Insert
    public abstract Completable saveRecipe(@NonNull Recipe recipe);

    @Update
    public abstract Completable updateRecipe(@NonNull Recipe recipe);

    @Query("DELETE from recipes WHERE id = :id")
    public abstract Completable deleteRecipeById(@NonNull String id);

}
