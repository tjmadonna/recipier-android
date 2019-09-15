/*
 * ImagePickerAdapter.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.dialogs.imagepicker;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inelasticcollision.recipelink.R;

import java.util.List;

class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ImageViewHolder>{

    private final Context mContext;

    private final List<String> mImages;

    private final ImagePickerAdapter.OnImagePickerAdapterItemClickListener mListener;

    ImagePickerAdapter(Context context, List<String> images, OnImagePickerAdapterItemClickListener listener) {

        mContext = context;

        mImages = images;

        mListener = listener;

    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImagePickerAdapter.ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image_picker, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        String image = mImages.get(position);

        // Load recipe image
        Glide.with(mContext)
                .load(image)
                .centerCrop()
                .dontAnimate()
                .dontTransform()
                .crossFade()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mImages != null ? mImages.size() : 0;
    }

    interface OnImagePickerAdapterItemClickListener {

        void onImagePickerAdapterItemClick(String image);

    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;

        ImageViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            String imageUrl = mImages.get(getAdapterPosition());

            mListener.onImagePickerAdapterItemClick(imageUrl);

        }

    }

}
