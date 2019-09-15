/*
 * SQLiteDataProviderTest.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/29/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.content.ContentValues;
import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inelasticcollision.recipelink.data.local.helper.DatabaseHelper;
import com.inelasticcollision.recipelink.data.local.table.CollectionTable;
import com.inelasticcollision.recipelink.data.local.table.RecipeTable;
import com.inelasticcollision.recipelink.data.local.table.RelationshipTable;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

@RunWith(AndroidJUnit4.class)
public class RecipeDataProviderTest {

    private List<Recipe> RECIPES = Arrays.asList(
            new Recipe("Test recipe 1", "www.1.com", "", 0, null, ""),
            new Recipe("Test recipe 2", "www.2.com", "", 0, null, ""),
            new Recipe("Test recipe 3", "www.3.com", "", 0, null, ""),
            new Recipe("Test recipe 4", "www.4.com", "", 1, null, ""),
            new Recipe("Test recipe 5", "www.5.com", "", 1, null, "")
    );

    private RecipeLocalDataProvider mDataProvider;

    private BriteDatabase mBriteDatabase;

    @Before
    public void setup() {

        mBriteDatabase.delete(RecipeTable.TABLE_NAME, null);
        mBriteDatabase.delete(CollectionTable.TABLE_NAME, null);
        mBriteDatabase.delete(RelationshipTable.TABLE_NAME, null);

        Context context = InstrumentationRegistry.getTargetContext();

        DatabaseHelper helper = new DatabaseHelper(context);

        mBriteDatabase = new SqlBrite.Builder()
                .build()
                .wrapDatabaseHelper(helper, Schedulers.immediate());

        mDataProvider = new RecipeLocalDataProvider(mBriteDatabase);

        populationDatabase();

    }

    @After
    public void tearDown() {
        mBriteDatabase.delete(RecipeTable.TABLE_NAME, null);
        mBriteDatabase.delete(CollectionTable.TABLE_NAME, null);
        mBriteDatabase.delete(RelationshipTable.TABLE_NAME, null);
    }

    @Test
    public void loadAllRecipes() {

        TestSubscriber<List<Recipe>> testSubscriber = new TestSubscriber<> ();

        mDataProvider.loadAllRecipes()
                .subscribe(testSubscriber);

        testSubscriber.assertValue(Arrays.asList(
                new Recipe("Test recipe 1", "www.1.com", "", 0, null, ""),
                new Recipe("Test recipe 2", "www.2.com", "", 0, null, ""),
                new Recipe("Test recipe 3", "www.3.com", "", 0, null, ""),
                new Recipe("Test recipe 4", "www.4.com", "", 1, null, ""),
                new Recipe("Test recipe 5", "www.5.com", "", 1, null, "")
        ));

        testSubscriber.assertNoErrors();

    }

    @Test
    public void loadFavoriteRecipes() {

        TestSubscriber<List<Recipe>> testSubscriber = new TestSubscriber<>();

        mDataProvider.loadFavoriteRecipes()
                .subscribe(testSubscriber);

        testSubscriber.assertValue(Arrays.asList(
                new Recipe("Test recipe 4", "www.4.com", "", 1, null, ""),
                new Recipe("Test recipe 5", "www.5.com", "", 1, null, "")
        ));

        testSubscriber.assertNoErrors();

    }

    private void populationDatabase() {

        // Insert Recipes
        ContentValues[] recipeValues = DatabaseTestHelper.getContentValuesFromRecipeList(RECIPES);

        for (ContentValues cv: recipeValues) {
            mBriteDatabase.insert(RecipeTable.TABLE_NAME, cv);
        }

    }

}
