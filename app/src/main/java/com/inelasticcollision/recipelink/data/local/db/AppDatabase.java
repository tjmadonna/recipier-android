package com.inelasticcollision.recipelink.data.local.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.inelasticcollision.recipelink.data.local.dao.RecipeDao;
import com.inelasticcollision.recipelink.data.models.Recipe;

@Database(entities = {Recipe.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "recipecollection.db";
    @NonNull
    private static final Object lock = new Object();
    @Nullable
    private static volatile AppDatabase instance = null;

    private AppDatabase() {
    }

    public static AppDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    ).build();
                }
            }
        }
        return instance;
    }

    public abstract RecipeDao recipeDao();
}
