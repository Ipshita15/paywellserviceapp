package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 7/18/16.
 */
public class WishlistIndexCount {

    private int Index;
    private int Count;

    public WishlistIndexCount(int index, int count) {
        Index = index;
        Count = count;
    }

    public int getIndex() {
        return Index;
    }

    public int getCount() {
        return Count;
    }

    @Override
    public String toString() {
        return "WishlistIndexCount{" +
                "Index=" + Index +
                ", Count=" + Count +
                '}';
    }
}
