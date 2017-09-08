/*
 * Relationship.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/17/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.models;

public class Relationship {

    private int recipeId;

    private int collectionId;

    public Relationship(int recipeId, int collectionId) {
        this.recipeId = recipeId;
        this.collectionId = collectionId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getCollectionId() {
        return collectionId;
    }

}
