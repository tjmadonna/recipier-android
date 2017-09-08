/*
 * NewRecipeSavedState.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/15/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.editrecipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

class EditRecipeSavedState implements Parcelable {

    int id;

    long dateAdded;

    String url;

    boolean favorite;

    List<String> images;

    String selectedImage;

    boolean needsRecipeQuery;

    boolean needsImageExtracting;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.dateAdded);
        dest.writeString(this.url);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.images);
        dest.writeString(this.selectedImage);
        dest.writeByte(this.needsRecipeQuery ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needsImageExtracting ? (byte) 1 : (byte) 0);
    }

    EditRecipeSavedState(int id, long dateAdded, String url, boolean favorite, List<String> images, String selectedImage, boolean needsRecipeQuery, boolean needsImageExtracting) {
        this.id = id;
        this.dateAdded = dateAdded;
        this.url = url;
        this.favorite = favorite;
        this.images = images;
        this.selectedImage = selectedImage;
        this.needsRecipeQuery = needsRecipeQuery;
        this.needsImageExtracting = needsImageExtracting;
    }

    protected EditRecipeSavedState(Parcel in) {
        this.id = in.readInt();
        this.dateAdded = in.readLong();
        this.url = in.readString();
        this.favorite = in.readByte() != 0;
        this.images = in.createStringArrayList();
        this.selectedImage = in.readString();
        this.needsRecipeQuery = in.readByte() != 0;
        this.needsImageExtracting = in.readByte() != 0;
    }

    public static final Creator<EditRecipeSavedState> CREATOR = new Creator<EditRecipeSavedState>() {
        @Override
        public EditRecipeSavedState createFromParcel(Parcel source) {
            return new EditRecipeSavedState(source);
        }

        @Override
        public EditRecipeSavedState[] newArray(int size) {
            return new EditRecipeSavedState[size];
        }
    };

}
