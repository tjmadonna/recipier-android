/*
 * AspectRatioCollapsingToolbarView.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 9/21/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.util.AttributeSet;

import com.inelasticcollision.recipelink.R;

public class AspectRatioCollapsingToolbarLayout extends CollapsingToolbarLayout {

    private float aspectRatio = 0f;

    public AspectRatioCollapsingToolbarLayout(Context context) {
        this(context, null, 0);
    }

    public AspectRatioCollapsingToolbarLayout(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public AspectRatioCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioView, defStyle, 0);

        aspectRatio = a.getFloat(R.styleable.AspectRatioView_aspectRatio, 0);

        if (aspectRatio == 0f) {
            throw new IllegalArgumentException("You must specify an aspect ratio when using the AspectRatioView.");
        }

        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width, height;
        if (aspectRatio != 0) {
            width = widthSize;
            height = (int) (width / aspectRatio);
            int exactWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            int exactHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            super.onMeasure(exactWidthSpec, exactHeightSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
