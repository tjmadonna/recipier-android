/*
 * RelationshipLocalDataProvider.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/17/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import com.inelasticcollision.recipelink.data.models.Relationship;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class RelationshipLocalDataProvider {

    private BriteDatabase mDatabase;

    public RelationshipLocalDataProvider(BriteDatabase database) {
        mDatabase = database;
    }

    public Observable<List<Relationship>> loadRelationships() {
        return mDatabase
                .createQuery(RelationshipTable.TABLE_NAME, RelationshipTable.getAllRelationshipQuery())
                .mapToOneOrDefault(RelationshipTable.RELATIONSHIP_MAPPER, new ArrayList<Relationship>(0));
    }

    public Observable<Integer> deleteAllRelationships() {
        return Observable.just(mDatabase
                .delete(CollectionTable.TABLE_NAME, null));

    }

}
