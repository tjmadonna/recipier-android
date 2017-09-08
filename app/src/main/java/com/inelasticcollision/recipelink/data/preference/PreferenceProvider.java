/*
 * PreferenceProvider.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 8/12/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.preference;

public interface PreferenceProvider {

    boolean shouldMigrateToVersion12Database();

    boolean setMigrateToVersion12Database();

}
