/*
 * ResourceHelper.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.inelasticcollision.recipelink.R;

public class ResourceHelper {

    public static float getImageAspectRatio(Context context) {
        TypedValue outValue = new TypedValue();
        context.getResources().getValue(R.dimen.aspect_16_9, outValue, true);
        return outValue.getFloat();
    }

    public static int getSmallWindowDimension(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

}

