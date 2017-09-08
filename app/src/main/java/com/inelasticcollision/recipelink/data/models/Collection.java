/*
 * Collection.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/28/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.models;

public class Collection {

    public static final int ID_NONE = -1;

    private int id;

    private String title;

    public Collection(String title) {
        this.id = ID_NONE;
        this.title = title;
    }

    public Collection(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object object) {

        return (object == this) || (object instanceof Collection) && title.equals(((Collection) object).getTitle());

    }

}
