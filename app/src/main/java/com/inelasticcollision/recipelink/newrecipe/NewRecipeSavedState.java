/*
 * NewRecipeSavedState.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.newrecipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

class NewRecipeSavedState implements Parcelable {

    List<String> images;

    String selectedImage;

    boolean favorite;

    boolean needsExtracting;

    String title;

    String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.images);
        dest.writeString(this.selectedImage);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needsExtracting ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeString(this.url);
    }

    NewRecipeSavedState(List<String> images, String selectedImage, boolean favorite, boolean needsExtracting, String title, String url) {
        this.images = images;
        this.selectedImage = selectedImage;
        this.favorite = favorite;
        this.needsExtracting = needsExtracting;
        this.title = title;
        this.url = url;
    }

    protected NewRecipeSavedState(Parcel in) {
        this.images = in.createStringArrayList();
        this.selectedImage = in.readString();
        this.favorite = in.readByte() != 0;
        this.needsExtracting = in.readByte() != 0;
        this.title = in.readString();
        this.url = in.readString();
    }

    public static final Creator<NewRecipeSavedState> CREATOR = new Creator<NewRecipeSavedState>() {
        @Override
        public NewRecipeSavedState createFromParcel(Parcel source) {
            return new NewRecipeSavedState(source);
        }

        @Override
        public NewRecipeSavedState[] newArray(int size) {
            return new NewRecipeSavedState[size];
        }
    };

}
