/*
 * OptionsBottomSheetDialog.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.inelasticcollision.recipelink.R;

public class OptionsBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = OptionsBottomSheetDialog.class.getSimpleName();

    private static final String ARGS_ITEM = "argument_item";

    // The bottom sheet behavior object
    private BottomSheetBehavior mBehavior;

    private OptionsBottomSheetDialog.OptionsBottomSheetDialogListener mListener;

    public void setListener(OptionsBottomSheetDialogListener mListener) {
        this.mListener = mListener;
    }

    public static OptionsBottomSheetDialog newInstance(@NonNull String item) {
        OptionsBottomSheetDialog dialog = new OptionsBottomSheetDialog();
        Bundle arguments = new Bundle(1);
        arguments.putString(ARGS_ITEM, item);
        dialog.setArguments(arguments);
        return dialog;
    }

    public OptionsBottomSheetDialog() { }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String item = getArguments().getString(ARGS_ITEM, "");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_options_bottom_sheet, new FrameLayout(getActivity()), false);

        view.findViewById(R.id.options_bottom_sheet_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditItemClicked(item);
                dismiss();
            }
        });

        view.findViewById(R.id.options_bottom_sheet_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteItemClicked(item);
                dismiss();
            }
        });

        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        dialog.setContentView(view);

        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        return dialog;

    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OptionsBottomSheetDialogListener {

        void onEditItemClicked(String item);

        void onDeleteItemClicked(String item);

    }

}
