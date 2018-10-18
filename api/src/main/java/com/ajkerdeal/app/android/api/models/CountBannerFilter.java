package com.ajkerdeal.app.android.api.models;

/**
 * Created by Rasel on 1/3/2017.
 */

public class CountBannerFilter {

    private int Count;

    public CountBannerFilter(int count) {
        Count = count;
    }

    public CountBannerFilter() {
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
