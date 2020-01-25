package com.inelasticcollision.recipelink.data.local.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.inelasticcollision.recipelink.data.local.converter.DateTypeConverter;
import com.inelasticcollision.recipelink.data.local.converter.StringListTypeConverter;
import com.inelasticcollision.recipelink.data.local.dao.RecipeDao;
import com.inelasticcollision.recipelink.data.models.Recipe;

@Database(entities = {Recipe.class}, version = 3)
@TypeConverters({DateTypeConverter.class, StringListTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "recipecollection.db";

    @Nullable
    private static volatile AppDatabase instance = null;

    AppDatabase() { }

    @NonNull
    private static final Object lock = new Object();

    public abstract RecipeDao recipeDao();

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

}
