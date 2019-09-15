/*
 * CollectionTableTest.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 6/12/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inelasticcollision.recipelink.data.local.table.CollectionTable;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CollectionTableTest {

    @Before
    public void setup() {

        Context context = InstrumentationRegistry.getTargetContext();

        DatabaseHelper helper = new DatabaseHelper(context);

    }

    @After
    public void tearDown() {

    }


    @Test
    public void getAllRecipeQuery() {

        String query = CollectionTable.getAllCollectionsQuery();

        Assert.assertEquals("SELECT collection._id, title FROM collection ORDER BY title ASC", query);

    }

}
