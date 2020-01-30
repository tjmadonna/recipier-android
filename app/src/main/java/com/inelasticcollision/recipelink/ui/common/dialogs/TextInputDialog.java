/*
 * TextInputDialog.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.common.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.inelasticcollision.recipelink.R;
import com.inelasticcollision.recipelink.utils.StringHelper;

public class TextInputDialog extends DialogFragment {

    public static final String TAG = TextInputDialog.class.getSimpleName();

    private static final String ARGS_TITLE = "argument_title";

    private static final String ARGS_HINT = "argument_hint";

    private static final String ARGS_TEXT = "argument_text";

    private OnTextInputDialogListener mListener;

    public void setListener(OnTextInputDialogListener mListener) {
        this.mListener = mListener;
    }

    public static TextInputDialog newInstance(@NonNull String title, @NonNull String textHint, @Nullable String text) {
        TextInputDialog dialog = new TextInputDialog();
        Bundle arguments = new Bundle(3);
        arguments.putString(ARGS_TITLE, title);
        arguments.putString(ARGS_HINT, textHint);
        arguments.putString(ARGS_TEXT, text);
        dialog.setArguments(arguments);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle arguments = getArguments();

        final String originalText = arguments.getString(ARGS_TEXT);

        FrameLayout frame = new FrameLayout(getActivity());

        final EditText inputView = (EditText) getActivity().getLayoutInflater().inflate(R.layout.dialog_text_input, frame, false);

        inputView.setHint(R.string.hint_enter_collection_name);

        inputView.setText(originalText);

        frame.addView(inputView);


        return new MaterialAlertDialogBuilder(requireContext(), R.style.AppTheme_DialogStyle)
                .setTitle(arguments.getString(ARGS_TITLE, ""))
                .setView(frame)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String inputText = inputView.getText().toString();

                        if (!StringHelper.removeWhitespace(inputText).isEmpty()) {
                            mListener.onOKButtonClicked(inputText, originalText);
                            dismiss();
                        }

                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTextInputDialogListener {

        void onOKButtonClicked(@NonNull String inputText, @Nullable String originalText);

    }

}
