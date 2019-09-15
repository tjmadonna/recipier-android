/*
 * ImagePickerDialog.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.dialogs.imagepicker;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.FrameLayout;

import com.inelasticcollision.recipelink.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePickerDialog extends DialogFragment {

    public static final String TAG = ImagePickerDialog.class.getSimpleName();

    private static final String ARGS_IMAGES = "argument_images";

    public static ImagePickerDialog newInstance(ArrayList<String> imageList) {
        ImagePickerDialog dialog = new ImagePickerDialog();
        Bundle arguments = new Bundle(1);
        arguments.putStringArrayList(ARGS_IMAGES, imageList);
        dialog.setArguments(arguments);
        return dialog;
    }

    private OnImagePickerDialogListener mListener;

    public void setListener(OnImagePickerDialogListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        List<String> images = getArguments().getStringArrayList(ARGS_IMAGES);

        FrameLayout frame = new FrameLayout(getActivity());

        RecyclerView recyclerView = (RecyclerView) getActivity().getLayoutInflater().inflate(R.layout.dialog_image_picker, frame, false);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recyclerView.setAdapter(new ImagePickerAdapter(getActivity(), images, new ImagePickerAdapter.OnImagePickerAdapterItemClickListener() {

            @Override
            public void onImagePickerAdapterItemClick(String image) {
                mListener.onImageClicked(image);
                dismiss();
            }

        }));

        frame.addView(recyclerView);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_change_image)
                .setView(frame)
                .setNegativeButton(R.string.action_cancel, null)
                .create();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnImagePickerDialogListener {

        void onImageClicked(String imageUrl);

    }

}
