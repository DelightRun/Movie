package com.movie.movie.recommend;

import org.apache.mahout.cf.taste.impl.model.GenericPreference;

public class GenericBoolPreference extends GenericPreference {
    public GenericBoolPreference(long userID, long itemID) {
        super(userID, itemID, 1.0f);
    }
}
