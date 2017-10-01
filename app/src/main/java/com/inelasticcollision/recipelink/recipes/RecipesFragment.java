/*
 * RecipesFragment.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.recipes;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.inelasticcollision.recipelink.R;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.main.MainActivityActionListener;

import java.util.List;

public class RecipesFragment extends Fragment implements RecipesContract.View, MainActivityActionListener {

    private static final String SAVED_SEARCH_STRING = "saved_search_string";

    private static final String SAVED_SEARCH_FOCUS = "saved_search_focus";

    private static final String SAVED_SORT_TYPE = "saved_sort_type";

    private static final String LEARN_HOW_URL = "https://madonnaapps.com/recipelink/learn-android.html";

    private RecipesContract.Presenter mPresenter;

    private View mEmptyContentView;

    private RecipesAdapter mAdapter;

    private SearchView mSearchView;

    private String mSearchQuery;

    private boolean mSearchFocus = false;

    private int mSortType;

    private RecipesFragmentInteractionListener mListener;

    public RecipesFragment() { }

    // Lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        RecipesInjection.inject(getActivity(), this);

        if (savedInstanceState != null) {

            mSearchQuery = savedInstanceState.getString(SAVED_SEARCH_STRING);

            mSortType = savedInstanceState.getInt(SAVED_SORT_TYPE, RecipesSortType.ALL);

            mSearchFocus = savedInstanceState.getBoolean(SAVED_SEARCH_FOCUS, false);

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);

        configureViews(rootView);

        return rootView;

    }

    private void configureViews(View rootView) {

        mAdapter = new RecipesAdapter(getActivity(), new RecipesAdapter.OnRecipesAdapterItemClickListener() {
            @Override
            public void onRecipesAdapterItemClick(Recipe recipe) {
                mListener.onRecipeItemClicked(recipe);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = rootView.findViewById(R.id.recipes_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        mEmptyContentView = rootView.findViewById(R.id.recipes_empty_content);

        rootView.findViewById(R.id.recipes_empty_learn_how_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.handleLearnHowButtonClicked();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onSubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onUnsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SAVED_SORT_TYPE, mSortType);

        if (!mSearchView.isIconified()) {

            String query = mSearchView.getQuery().toString();
            outState.putString(SAVED_SEARCH_STRING, query);

            boolean focus = mSearchView.hasFocus();
            outState.putBoolean(SAVED_SEARCH_FOCUS, focus);

        }

    }

    // Menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_recipes, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mSearchQuery = null;
                mPresenter.handleLoadRecipes(mSortType, null);
                return true;
            }
        });

        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryForSearchTerm(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (mSearchQuery != null) {

            searchItem.expandActionView();
            mSearchView.setQuery(mSearchQuery, false);
            queryForSearchTerm(mSearchQuery, false);

            if (!mSearchFocus) {
                mSearchView.clearFocus();
            }

            if (mSearchQuery.isEmpty()) {
                mPresenter.handleLoadRecipes(mSortType, null);
            }

        }

    }

    private void queryForSearchTerm(String searchTerm, boolean clearFocus) {
        mPresenter.handleLoadRecipes(RecipesSortType.SEARCH, searchTerm);
        if (clearFocus) {
            mSearchView.clearFocus();
        }
    }

    public void setListener(RecipesFragmentInteractionListener listener) {
        mListener = listener;
    }


    // RecipesContract.View interface

    @Override
    public void setPresenter(RecipesContract.Presenter presenter) {

        mPresenter = presenter;

    }

    @Override
    public void showRecipes(@NonNull List<Recipe> recipes) {

        mAdapter.setRecipes(recipes);

        mEmptyContentView.setVisibility(View.GONE);

    }

    @Override
    public void showEmptyContentMessage() {

        mEmptyContentView.setVisibility(View.VISIBLE);

    }

    @Override
    public void showLearnHowWebPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LEARN_HOW_URL));
        startActivity(intent);
    }

    // MainActivityActionListener

    @Override
    public void changeSortTypeSelection(int viewId) {

        if (mSearchQuery == null) {

            int sortType = RecipesSortType.sortTypeFromViewId(viewId);

            mSortType = sortType;

            mPresenter.handleLoadRecipes(sortType, null);
        }

    }

    @Override
    public void activateSearchView() {

        mSearchQuery = "";

        mSearchFocus = true;

    }

    public interface RecipesFragmentInteractionListener {

        void onRecipeItemClicked(Recipe recipe);

    }

}
