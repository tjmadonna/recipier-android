/*
 * RecipeTableTest.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 6/12/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.content.ContentValues;
import android.support.test.runner.AndroidJUnit4;

import com.inelasticcollision.recipelink.data.models.Recipe;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RecipeTableTest {

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void getAllRecipeQuery() {

        String query = RecipeTable.getAllRecipesQuery();

        Assert.assertEquals("SELECT recipe._id, date_added, title, link, photo_url, favorite, keywords, notes FROM recipe ORDER BY title COLLATE NOCASE", query);

    }

    @Test
    public void getFavoriteRecipeQuery() {

        String query = RecipeTable.getFavoriteRecipesQuery();

        Assert.assertEquals("SELECT recipe._id, date_added, title, link, photo_url, favorite, keywords, notes FROM recipe WHERE favorite = 1 ORDER BY title COLLATE NOCASE", query);

    }

    @Test
    public void getRecipesSearchQuery() {

        String query = RecipeTable.getRecipesSearchQuery("Example Search");

        Assert.assertEquals("SELECT recipe._id, date_added, title, link, photo_url, favorite, keywords, notes FROM recipe WHERE title LIKE '%Example Search%' OR keywords LIKE '%Example Search%' ORDER BY title COLLATE NOCASE", query);

    }

    @Test
    public void getRecipeIdQuery() {

        String query = RecipeTable.getRecipeWhereClause(65);

        Assert.assertEquals("recipe._id = 65", query);

    }

    @Test
    public void getNewRecipeAsContentValues() {

        Recipe recipe = new Recipe("Test recipe 1", "www.1.com", "", 0, null, "keywords");

        ContentValues recipeVC = RecipeTable.getRecipeAsContentValues(recipe);

        Assert.assertNotEquals(Long.valueOf(recipe.getDateAdded()), recipeVC.getAsLong(RecipeTable.COLUMN_DATE_ADDED));
        Assert.assertEquals(recipe.getTitle(), recipeVC.getAsString(RecipeTable.COLUMN_TITLE));
        Assert.assertEquals(recipe.getUrl(), recipeVC.getAsString(RecipeTable.COLUMN_URL));
        Assert.assertEquals(recipe.getImageUrl(), recipeVC.getAsString(RecipeTable.COLUMN_IMAGE_URL));
        Assert.assertEquals(Integer.valueOf(recipe.getFavoriteAsInt()), recipeVC.getAsInteger(RecipeTable.COLUMN_FAVORITE));
        Assert.assertEquals(recipe.getNotes(), recipeVC.getAsString(RecipeTable.COLUMN_NOTES));
        Assert.assertEquals("", recipeVC.getAsString(RecipeTable.COLUMN_KEYWORDS));

    }

    @Test
    public void getUpdateRecipeAsContentValues() {

        Recipe recipe = new Recipe(15, 1000, "Test recipe 1", "www.1.com", "", 0, null, "keywords");

        ContentValues recipeVC = RecipeTable.getRecipeAsContentValues(recipe);

        Assert.assertEquals(Long.valueOf(recipe.getDateAdded()), recipeVC.getAsLong(RecipeTable.COLUMN_DATE_ADDED));
        Assert.assertEquals(recipe.getTitle(), recipeVC.getAsString(RecipeTable.COLUMN_TITLE));
        Assert.assertEquals(recipe.getUrl(), recipeVC.getAsString(RecipeTable.COLUMN_URL));
        Assert.assertEquals(recipe.getImageUrl(), recipeVC.getAsString(RecipeTable.COLUMN_IMAGE_URL));
        Assert.assertEquals(Integer.valueOf(recipe.getFavoriteAsInt()), recipeVC.getAsInteger(RecipeTable.COLUMN_FAVORITE));
        Assert.assertEquals(recipe.getNotes(), recipeVC.getAsString(RecipeTable.COLUMN_NOTES));
        Assert.assertEquals("", recipeVC.getAsString(RecipeTable.COLUMN_KEYWORDS));

    }

    @Test
    public void keywordsListToString() {

        List<String> list = Arrays.asList("Keyword 1", "Keyword 5", "Keyword 3", "Keyword 4", "Keyword 2");

        String listString = RecipeTable.keywordsListToString(list);

        Assert.assertEquals("Keyword 1|Keyword 5|Keyword 3|Keyword 4|Keyword 2", listString);

    }

    @Test
    public void keywordsStringToList() {

        String listString = "Keyword 1|Keyword 5|Keyword 3|Keyword 4|Keyword 2";

        List<String> list = RecipeTable.keywordsStringToList(listString);

        Assert.assertEquals(Arrays.asList("Keyword 1", "Keyword 5", "Keyword 3", "Keyword 4", "Keyword 2"), list);

    }

}
