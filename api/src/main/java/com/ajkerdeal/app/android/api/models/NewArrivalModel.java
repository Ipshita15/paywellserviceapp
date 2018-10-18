package com.ajkerdeal.app.android.api.models;

/**
 * Created by Rasel on 1/25/2017.
 */

public class NewArrivalModel {

    int TopCount;

    public NewArrivalModel() {
    }

    public NewArrivalModel(int topCount) {
        TopCount = topCount;
    }

    public int getTopCount() {
        return TopCount;
    }

    public void setTopCount(int topCount) {
        TopCount = topCount;
    }
}
