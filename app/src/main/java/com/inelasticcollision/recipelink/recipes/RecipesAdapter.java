/*
 * RecipesAdapter.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.recipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.utils.StringHelper;

import java.util.List;

import com.inelasticcollision.recipelink.R;

class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private List<Recipe> mRecipes;

    private final Context mContext;

    private final OnRecipesAdapterItemClickListener mListener;

    RecipesAdapter(Context context, OnRecipesAdapterItemClickListener listener) {

        mContext = context;

        mListener = listener;

    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recipes, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int position) {

        Recipe recipe = mRecipes.get(position);

        // Load recipe image
        Glide.with(mContext)
                .load(recipe.getImageUrl())
                .centerCrop()
                .dontAnimate()
                .dontTransform()
                .crossFade()
                .into(holder.image);

        // Set the recipe images content description
        holder.image.setContentDescription(recipe.getTitle());

        // Load recipe title
        holder.title.setText(recipe.getTitle());

        // Load recipe url
        holder.hostUrl.setText(StringHelper.mapUrlToUserPresentableHostUrl(recipe.getUrl()));

    }

    @Override
    public int getItemCount() {
        return mRecipes != null ? mRecipes.size() : 0;
    }

    void setRecipes(List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
        notifyDataSetChanged();
    }

    interface OnRecipesAdapterItemClickListener {

        void onRecipesAdapterItemClick(Recipe recipe);

    }

    class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;

        TextView title;

        TextView hostUrl;

        RecipesViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.recipes_list_item_image);
            title = (TextView) itemView.findViewById(R.id.recipes_list_item_title);
            hostUrl = (TextView) itemView.findViewById(R.id.recipes_list_item_host_url);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Recipe recipe = mRecipes.get(getAdapterPosition());

            mListener.onRecipesAdapterItemClick(recipe);

        }

    }

}
