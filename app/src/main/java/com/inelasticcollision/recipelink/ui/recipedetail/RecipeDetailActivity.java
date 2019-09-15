/*
 * RecipeDetailActivity.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/26/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.inelasticcollision.recipelink.R;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    public static Intent createIntent(Context context, int recipeId) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {

            int recipeId = getIntent().getExtras().getInt(EXTRA_RECIPE_ID, -1);

            getFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_activity_container, RecipeDetailFragment.newInstance(recipeId), RecipeDetailFragment.TAG)
                    .commit();

        }

    }

}
