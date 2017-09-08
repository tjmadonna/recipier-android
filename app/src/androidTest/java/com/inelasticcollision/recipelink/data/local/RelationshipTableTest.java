/*
 * RelationshipTableTest.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 6/12/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.inelasticcollision.recipelink.data.models.Collection;
import com.inelasticcollision.recipelink.data.models.Recipe;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RelationshipTableTest {

    @Before
    public void setup() {

        Context context = InstrumentationRegistry.getTargetContext();

        DatabaseHelper helper = new DatabaseHelper(context);

    }

    @After
    public void tearDown() {

    }

}
