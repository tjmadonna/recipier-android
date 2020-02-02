package com.inelasticcollision.recipelink.data.local.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.models.Recipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class RecipeDao implements LocalDataProvider {

    @Query("SELECT * FROM recipes ORDER BY title COLLATE NOCASE")
    public abstract io.reactivex.Observable<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE favorite = 1 ORDER BY title COLLATE NOCASE")
    public abstract io.reactivex.Observable<List<Recipe>> getFavoriteRecipes();

    public io.reactivex.Observable<List<Recipe>> getRecipesBySearchTerm(@Nullable String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return getAllRecipes();
        } else {
            return searchRecipes("%" + searchTerm + "%");
        }
    }

    @Query("SELECT * FROM recipes WHERE title LIKE :searchTerm OR tags LIKE :searchTerm ORDER BY title COLLATE NOCASE")
    abstract io.reactivex.Observable<List<Recipe>> searchRecipes(@NonNull String searchTerm);

    @Query("SELECT * FROM recipes WHERE id = :id")
    public abstract Single<Recipe> getRecipeById(@NonNull String id);

    @Insert
    public abstract Completable saveRecipe(@NonNull Recipe recipe);

    @Update
    public abstract Completable updateRecipe(@NonNull Recipe recipe);

    @Query("DELETE from recipes WHERE id = :id")
    public abstract Completable deleteRecipe(@NonNull String id);

}
