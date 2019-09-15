/*
 * RecipesSortType.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipes;

import com.inelasticcollision.recipelink.R;

final class RecipesSortType {

    static int sortTypeFromViewId(int viewId) {

        switch (viewId) {
            case R.id.nav_all_recipes:
                return ALL;
            case R.id.nav_favorite_recipes:
                return FAVORITE;
            default:
                return NONE;
        }

    }

    static final int NONE = -1;

    static final int ALL = 1000;

    static final int FAVORITE = 2000;

    static final int SEARCH = 3000;

}
