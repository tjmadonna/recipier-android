/*
 * MainActivity.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.inelasticcollision.recipelink.R;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.data.preference.PreferenceProvider;
import com.inelasticcollision.recipelink.data.preference.SharedPreferenceProvider;
import com.inelasticcollision.recipelink.recipedetail.RecipeDetailActivity;
import com.inelasticcollision.recipelink.recipes.RecipesFragment;
import com.inelasticcollision.recipelink.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SAVED_SELECTED_NAVIGATION_ITEM = "saved_selected_navigation_item";

    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    private static final String EXTRA_SHORTCUT_ID = "extra_shortcut_id";

    private static final String SHORTCUT_ID_FAVORITE = "shortcut_favorite";

    private static final String SHORTCUT_ID_SEARCH = "shortcut_search";

    private NavigationView mNavigationView;

    private DrawerLayout mDrawerLayout;

    private int mNavigationViewSelectedItem = R.id.nav_all_recipes;

    private MainActivityActionListener mListener;

    private boolean mActivateSearchView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {

            String shortcutId = getIntent().getExtras().getString(EXTRA_SHORTCUT_ID, "");

            if (shortcutId.equals(SHORTCUT_ID_FAVORITE)) {

                mNavigationViewSelectedItem = R.id.nav_favorite_recipes;

            } else if (shortcutId.equals(SHORTCUT_ID_SEARCH)) {

                mActivateSearchView = true;

            }

        }

        if (savedInstanceState != null) {

            mNavigationViewSelectedItem = savedInstanceState.getInt(SAVED_SELECTED_NAVIGATION_ITEM, R.id.nav_all_recipes);

        }

        setContentView(R.layout.activity_main);

        configureViews();

    }

    private void configureViews() {

        // Set up the recipes fragment
        mListener = (MainActivityActionListener) getFragmentManager().findFragmentById(R.id.main_fragment_recipes);

        // Set the toolbar as the actionbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Set up the drawer layout
        mDrawerLayout = findViewById(R.id.main_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.prompt_navigation_drawer_open, R.string.prompt_navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigation view
        mNavigationView = findViewById(R.id.main_nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    private void migrateDatabase() {

        PreferenceProvider preferenceProvider = new SharedPreferenceProvider(PreferenceManager.getDefaultSharedPreferences(this));

        if (preferenceProvider.shouldMigrateToVersion12Database()) {
            Intent intent = MigrationActivity.createIntent(this);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        selectSortTypeInNavigationDrawer(mNavigationViewSelectedItem);

        if (mActivateSearchView) {
            mListener.activateSearchView();
            mActivateSearchView = false;
        }

        migrateDatabase();

    }

    @Override
    protected void onStart() {
        super.onStart();

        RecipesFragment recipesFragment = (RecipesFragment) getFragmentManager().findFragmentById(R.id.main_fragment_recipes);
        recipesFragment.setListener(new RecipesFragment.RecipesFragmentInteractionListener() {

            @Override
            public void onRecipeItemClicked(Recipe recipe) {

                Intent intent = RecipeDetailActivity.createIntent(MainActivity.this, recipe.getId());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityCompat.startActivity(MainActivity.this, intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                } else {
                    startActivity(intent);
                }

            }

        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        RecipesFragment recipesFragment = (RecipesFragment) getFragmentManager().findFragmentById(R.id.main_fragment_recipes);
        recipesFragment.setListener(null);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SAVED_SELECTED_NAVIGATION_ITEM, mNavigationViewSelectedItem);

    }

    // NavigationView.OnNavigationItemSelectedListener

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.nav_settings:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = SettingsActivity.createIntent(MainActivity.this);

                        startActivity(intent);

                        mNavigationView.setCheckedItem(mNavigationViewSelectedItem);

                    }
                }, NAVDRAWER_LAUNCH_DELAY);

                break;

            default:

                selectSortTypeInNavigationDrawer(itemId);

                break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void selectSortTypeInNavigationDrawer(int itemId) {

        mNavigationViewSelectedItem = itemId;

        mNavigationView.setCheckedItem(itemId);

        setTitleForItemId(itemId);

        mListener.changeSortTypeSelection(itemId);

    }

    private void setTitleForItemId(int itemId) {

        switch (itemId) {
            case R.id.nav_all_recipes:
                setTitle(R.string.title_recipes_all);
                break;

            case R.id.nav_favorite_recipes:
                setTitle(R.string.title_recipes_favorite);
                break;
        }

    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        super.onBackPressed();

    }

}
