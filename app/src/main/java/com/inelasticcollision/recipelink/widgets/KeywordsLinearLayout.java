/*
 * KeywordsLinearLayout.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inelasticcollision.recipelink.R;

import java.util.ArrayList;
import java.util.List;

public class KeywordsLinearLayout extends LinearLayout implements View.OnClickListener {

    private final boolean mEditable;

    private final int mKeywordsTextColor;

    private final int mAddKeywordTextColor;

    private KeywordsLinearLayoutListener mListener;

    private List<String> mKeywords = new ArrayList<>();

    public KeywordsLinearLayout(Context context) {
        this(context, null, 0);
    }

    public KeywordsLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeywordsLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOrientation(VERTICAL);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeywordsLinearLayout, defStyle, 0);

        mEditable = a.getBoolean(R.styleable.KeywordsLinearLayout_editable, false);

        a.recycle();

        mKeywordsTextColor = ContextCompat.getColor(context, R.color.primaryTextColor);

        mAddKeywordTextColor = ContextCompat.getColor(context, R.color.secondaryTextColor);

        updateLayout();

    }

    public void setListener(KeywordsLinearLayoutListener listener) {
        mListener = listener;
    }

    public boolean addKeyword(String keyword) {

        if (!mKeywords.contains(keyword)) {

            mKeywords.add(keyword);

            updateLayout();

            return true;

        }

        return false;

    }

    public List<String> getKeywords() {
        return mKeywords;
    }

    public boolean updateKeyword(String oldKeyword, String newKeyword) {

        if (!mKeywords.contains(newKeyword)) {

            int index = mKeywords.indexOf(oldKeyword);

            mKeywords.set(index, newKeyword);

            updateLayout();

            return true;

        }

        return false;

    }

    public void removeKeyword(String keyword) {

        mKeywords.remove(keyword);

        updateLayout();

    }

    public void setKeywords(List<String> keywords) {

        if (keywords == null || keywords.isEmpty()) {
            keywords = new ArrayList<>();
        }

        mKeywords = keywords;

        updateLayout();

    }

    public void setNullMessage(String message) {

        View view = getView(0);

        populateNullView(view, message);

        removeExcessViews(1);

    }

    private void updateLayout() {

        int index = 0;

        for (String keyword : mKeywords) {

            View view = getView(index);

            populateView(view, keyword, index);

            index++;

        }

        if (mEditable) {

            View view = getView(index);

            populateAddView(view, index);

            index++;

        }

        removeExcessViews(index);

    }

    private void populateView(View view, String keyword, int index) {

        ImageView image = (ImageView) view.findViewById(R.id.recipe_detail_list_item_image);

        TextView text = (TextView) view.findViewById(R.id.recipe_detail_list_item_text);

        text.setTextColor(mKeywordsTextColor);

        if (index == 0) {
            image.setVisibility(VISIBLE);
        } else {
            image.setVisibility(GONE);
        }

        text.setText(keyword);

    }

    private void populateAddView(View view, int index) {

        ImageView image = (ImageView) view.findViewById(R.id.recipe_detail_list_item_image);

        TextView text = (TextView) view.findViewById(R.id.recipe_detail_list_item_text);

        text.setTextColor(mAddKeywordTextColor);

        if (index == 0) {

            image.setVisibility(VISIBLE);

            text.setText(R.string.hint_add_keyword);

        } else {

            image.setVisibility(GONE);

            text.setText(R.string.hint_add_another_keyword);

        }

    }

    private void populateNullView(View view, String message) {

        ImageView image = (ImageView) view.findViewById(R.id.recipe_detail_list_item_image);

        image.setVisibility(VISIBLE);

        TextView text = (TextView) view.findViewById(R.id.recipe_detail_list_item_text);

        text.setTextColor(mAddKeywordTextColor);

        text.setText(message);

    }

    private View getView(int index) {

        View view = getChildAt(index);

        if (view == null) {

            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_recipe_detail, this, false);

            view.setClickable(mEditable);

            if (mEditable) {
                view.setOnClickListener(this);
            }

            addView(view);

        }

        return view;

    }

    private void removeExcessViews(int numberOfViews) {

        while (getChildCount() > numberOfViews) {
            removeViewAt(getChildCount() - 1);
        }

    }

    @Override
    public void onClick(View v) {

        if (mListener == null) {
            return;
        }

        int index = indexOfChild(v);

        if (index >= mKeywords.size()) {
            mListener.onAddKeywordItemClicked();
            return;
        }

        String item = mKeywords.get(index);

        mListener.onKeywordItemClicked(item);

    }

    public interface KeywordsLinearLayoutListener {

        void onKeywordItemClicked(String item);

        void onAddKeywordItemClicked();

    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.mKeywordsState = this.mKeywords;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());

        setKeywords(ss.mKeywordsState);

    }

    private static class SavedState extends BaseSavedState {

        List<String> mKeywordsState;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            in.readStringList(mKeywordsState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeStringList(mKeywordsState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
