package com.ajkerdeal.app.android.api.models;

/**
 * Created by has9 on 4/2/16.
 */
public class LimitModel {
    private int Index;
    private int Count;
    public LimitModel(int index, int count) {
        Index = index;
        Count = count;
    }

    public int getIndex() {
        return Index;
    }

    public int getCount() {
        return Count;
    }
}
