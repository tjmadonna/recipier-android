/*
 * RecipeDetailFragment.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipedetail;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.inelasticcollision.recipelink.R;
import com.inelasticcollision.recipelink.ui.editrecipe.EditRecipeActivity;
import com.inelasticcollision.recipelink.ui.common.widgets.KeywordsLinearLayout;

import java.util.List;

public class RecipeDetailFragment extends Fragment implements RecipeDetailContract.View {

    static final String TAG = RecipeDetailFragment.class.getSimpleName();

    private static final String ARGS_RECIPE_ID = "argument_recipe_id";

    private RecipeDetailContract.Presenter mPresenter;

    private RecipeDetailAppBarOffsetListener mAppBarOffsetListener;

    private TextView mTitleView;

    private ImageView mMainImageView;

    private ImageView mFavoriteImageView;

    private TextView mUrlView;

    private TextView mNotesView;

    private KeywordsLinearLayout mKeywordsLayout;

    private ProgressBar mImageProgressBar;

    public static RecipeDetailFragment newInstance(String recipeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();

        Bundle args = new Bundle(1);
        args.putString(ARGS_RECIPE_ID, recipeId);

        fragment.setArguments(args);

        return fragment;
    }

    public RecipeDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        String recipeId = getArguments().getString(ARGS_RECIPE_ID);

        RecipeDetailInjection.inject(getActivity(), this, recipeId);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        configureViews(rootView);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.onSubscribe();

        mPresenter.handleLoadRecipes();

    }

    @Override
    public void onPause() {
        super.onPause();

        mPresenter.onUnsubscribe();

    }

    // Set all the view
    private void configureViews(View rootView) {

        Toolbar toolbar = rootView.findViewById(R.id.recipe_detail_toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar;
        if ((actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar()) != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTitleView = rootView.findViewById(R.id.recipe_detail_title);

        mFavoriteImageView = rootView.findViewById(R.id.recipe_detail_favorite);

        mMainImageView = rootView.findViewById(R.id.recipe_detail_main_image);

        mImageProgressBar = rootView.findViewById(R.id.recipe_detail_image_progress);

        mUrlView = rootView.findViewById(R.id.recipe_detail_url);

        mNotesView = rootView.findViewById(R.id.recipe_detail_notes);

        mKeywordsLayout = rootView.findViewById(R.id.recipe_collections_container);

        rootView.findViewById(R.id.recipe_detail_floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openUrlInBrowser(); }
        });

        rootView.findViewById(R.id.recipe_detail_url_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openUrlInBrowser(); }
        });

        AppBarLayout appBarLayout = rootView.findViewById(R.id.recipe_detail_app_bar);

        if (appBarLayout != null) {

            int toolbarHeight = toolbar.getLayoutParams().height;

            mAppBarOffsetListener = new RecipeDetailAppBarOffsetListener(mFavoriteImageView, toolbarHeight);

            appBarLayout.addOnOffsetChangedListener(mAppBarOffsetListener);

        }

    }

    private void openUrlInBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlView.getText().toString()));
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_recipe_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                ((AppCompatActivity) getActivity()).supportFinishAfterTransition();
                return true;

            case R.id.action_edit:
                mPresenter.handleEditRecipeClick();
                return true;

            case R.id.action_delete:
                mPresenter.handleDeleteRecipeClick();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    // RecipeDetailContract.View

    @Override
    public void setPresenter(RecipeDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEditRecipeActivity(String recipeId) {
        Intent intent = EditRecipeActivity.createIntent(getActivity(), recipeId);
        getActivity().startActivity(intent);
    }

    @Override
    public void showImageLoadingIndicator(boolean show) {
        mImageProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMainImage(String imageUrl) {

        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .error(R.drawable.background_placeholder)
                .listener(new RequestListener<String, GlideDrawable>() {

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        showImageLoadingIndicator(false);
                        return false;
                    }

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        Log.e("NewRecipeActivity", e.getMessage(), e);

                        showImageLoadingIndicator(false);
                        showErrorMessage();
                        return false;
                    }

                })
                .crossFade()
                .into(mMainImageView);

    }

    @Override
    public void showTitle(String title) {
        mTitleView.setText(title);
    }

    @Override
    public void showUrl(String url) {
        mUrlView.setText(url);
    }

    @Override
    public void showNotes(String notes) {
        mNotesView.setText(notes);
        mNotesView.setHint(null);
    }

    @Override
    public void showEmptyNotes() {
        mNotesView.setText(null);
        mNotesView.setHint(getString(R.string.prompt_no_notes));
    }

    @Override
    public void showFavoriteIcon(boolean favorite) {
        mFavoriteImageView.setVisibility(favorite ? View.VISIBLE : View.GONE);

        if (mAppBarOffsetListener != null) {
            mAppBarOffsetListener.setEnabled(favorite);
        }
    }

    @Override
    public void showKeywords(List<String> keywords) { mKeywordsLayout.setKeywords(keywords); }

    @Override
    public void showEmptyKeywords() { mKeywordsLayout.setNullMessage(getString(R.string.prompt_no_keywords)); }

    @Override
    public void showErrorMessage() {
        Toast.makeText(getActivity(), R.string.prompt_error_loading_content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDeleteRecipeDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.prompt_delete_recipe)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.handleDeleteRecipe();
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .create()
                .show();
    }

    @Override
    public void finishView() {
        ((AppCompatActivity) getActivity()).supportFinishAfterTransition();
    }

}
