/*
 * NewRecipeActivity.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.newrecipe;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.inelasticcollision.recipelink.R;
import com.inelasticcollision.recipelink.dialogs.OptionsBottomSheetDialog;
import com.inelasticcollision.recipelink.dialogs.TextInputDialog;
import com.inelasticcollision.recipelink.dialogs.imagepicker.ImagePickerDialog;
import com.inelasticcollision.recipelink.widgets.KeywordsLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class NewRecipeActivity extends AppCompatActivity implements NewRecipeContract.View {

    private static final String SAVED_PRESENTER_STATE = "saved_presenter_state";

    private View mMainContainer;

    private FloatingActionButton mFab;

    private ImageView mMainImageView;

    private ProgressBar mMainProgressBar;

    private ProgressBar mImageProgressBar;

    private Button mChangeImageButton;

    private TextView mTitleView;

    private TextView mUrlView;

    private TextView mNotesView;

    private KeywordsLinearLayout mKeywordsLayout;

    private NewRecipeContract.Presenter mPresenter;

    public static Intent createIntent(Context context) {
        return new Intent(context, NewRecipeActivity.class);
    }

    // Lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_recipe);

        configureViews();

        NewRecipeSavedState state;

        if (savedInstanceState != null) {

            state = savedInstanceState.getParcelable(SAVED_PRESENTER_STATE);

            NewRecipeInjection.inject(this, this, state);

        } else {

            Bundle extras = getIntent().getExtras();

            String title = extras.getString(Intent.EXTRA_SUBJECT, "");
            String url = extras.getString(Intent.EXTRA_TEXT, "");

            NewRecipeInjection.inject(this, this, title, url);

        }

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        NewRecipeSavedState state = mPresenter.handleSavedState();

        outState.putParcelable(SAVED_PRESENTER_STATE, state);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_recipe, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_save).setVisible(mMainContainer.getVisibility() == View.VISIBLE);
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                mPresenter.handleCloseClick();
                return true;

            case R.id.action_save:
                mPresenter.handleSaveRecipe(mTitleView.getText().toString(), mUrlView.getText().toString(), mNotesView.getText().toString(), mKeywordsLayout.getKeywords());
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void configureViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.new_recipe_toolbar);

        setSupportActionBar(toolbar);

        mMainContainer = findViewById(R.id.new_recipe_main_container);

        mFab = (FloatingActionButton) findViewById(R.id.new_recipe_floating_action_button);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.handleFabClick();

            }
        });

        mMainImageView = (ImageView) findViewById(R.id.new_recipe_main_image);

        mMainProgressBar = (ProgressBar) findViewById(R.id.new_recipe_main_progress);

        mImageProgressBar = (ProgressBar) findViewById(R.id.new_recipe_image_progress);

        mChangeImageButton = (Button) findViewById(R.id.new_recipe_change_image);

        mChangeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.handleChangeImage();

            }
        });

        mTitleView = (TextView) findViewById(R.id.new_recipe_title);

        mUrlView = (TextView) findViewById(R.id.new_recipe_url);

        mNotesView = (TextView) findViewById(R.id.new_recipe_notes);

        mKeywordsLayout = (KeywordsLinearLayout) findViewById(R.id.new_recipe_collections_container);

        mKeywordsLayout.setListener(new KeywordsLinearLayout.KeywordsLinearLayoutListener() {
            @Override
            public void onKeywordItemClicked(String item) {
                showOptionsBottomSheetDialog(item);
            }

            @Override
            public void onAddKeywordItemClicked() {
                showAddCollectionDialog(null);
            }
        });

    }

    private void showAddCollectionDialog(@Nullable String text) {

        String title = getString(R.string.title_add_collection);

        String hint = getString(R.string.hint_enter_collection_name);

        TextInputDialog dialog = TextInputDialog.newInstance(title, hint, text);

        dialog.setListener(new TextInputDialog.OnTextInputDialogListener() {
            @Override
            public void onOKButtonClicked(@NonNull String inputText, @Nullable String originalText) {

                if (originalText == null) {
                    mKeywordsLayout.addKeyword(inputText);
                    return;
                }

                mKeywordsLayout.updateKeyword(originalText, inputText);

            }
        });

        dialog.show(getSupportFragmentManager(), TextInputDialog.TAG);
    }

    private void showOptionsBottomSheetDialog(@NonNull String item) {

        OptionsBottomSheetDialog dialog = OptionsBottomSheetDialog.newInstance(item);

        dialog.setListener(new OptionsBottomSheetDialog.OptionsBottomSheetDialogListener() {
            @Override
            public void onEditItemClicked(String item) { showAddCollectionDialog(item); }

            @Override
            public void onDeleteItemClicked(String item) { mKeywordsLayout.removeKeyword(item); }
        });

        dialog.show(getSupportFragmentManager(), OptionsBottomSheetDialog.TAG);

    }

    // NewRecipeContract.View

    @Override
    public void setPresenter(NewRecipeContract.Presenter presenter) { mPresenter = presenter; }

    @Override
    public void showMainImage(String imageUrl) {

        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .error(R.drawable.background_placeholder)
                .listener(new RequestListener<String, GlideDrawable>() {

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        showMainLoadingIndicator(false);
                        showImageLoadingIndicator(false);
                        return false;
                    }

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        Log.e("NewRecipeActivity", e.getMessage(), e);

                        showMainLoadingIndicator(false);
                        showImageLoadingIndicator(false);
                        showExtractionErrorMessage();
                        return false;
                    }

                })
                .dontAnimate()
                .into(mMainImageView);

    }

    @Override
    public void showTitle(String title) { mTitleView.setText(title); }

    @Override
    public void showUrl(String url) { mUrlView.setText(url); }

    @Override
    public void showMainLoadingIndicator(boolean show) {

        boolean isShowing = mMainProgressBar.getVisibility() == View.VISIBLE;

        if (isShowing != show) {

            mMainProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);

            mMainContainer.setVisibility(show ? View.INVISIBLE : View.VISIBLE);

            supportInvalidateOptionsMenu();

            mFab.setVisibility(View.INVISIBLE);

            if (!show) {

                mMainContainer.setAlpha(0.0f);

                mMainContainer.animate()
                        .alpha(1.0f)
                        .setDuration(500)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mFab.show();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

            }

        }

    }

    @Override
    public void showImageLoadingIndicator(boolean show) { mImageProgressBar.setVisibility(show ? View.VISIBLE : View.GONE); }

    @Override
    public void showExtractionErrorMessage() { Toast.makeText(NewRecipeActivity.this, R.string.prompt_error_loading_content, Toast.LENGTH_SHORT).show(); }

    @Override
    public void showDatabaseErrorMessage() { Toast.makeText(NewRecipeActivity.this, R.string.prompt_error_saving_content, Toast.LENGTH_SHORT).show(); }

    @Override
    public void showImagePicker(List<String> images) {

        if (images instanceof ArrayList) {
            ImagePickerDialog dialog = ImagePickerDialog.newInstance((ArrayList<String>) images);
            dialog.setListener(new ImagePickerDialog.OnImagePickerDialogListener() {
                @Override
                public void onImageClicked(String imageUrl) {
                    mPresenter.handleImagePickerClick(imageUrl);
                }
            });
            dialog.show(getSupportFragmentManager(), ImagePickerDialog.TAG);
        }

    }

    @Override
    public void showFab(boolean favorite) {

        mFab.setImageDrawable(ContextCompat.getDrawable(this, favorite ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp));

    }

    @Override
    public void showCloseDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.prompt_discard_recipe)
                .setPositiveButton(R.string.action_discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.handleCloseView();
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .create()
                .show();
    }

    @Override
    public void finishView() {
        supportFinishAfterTransition();
    }

}
