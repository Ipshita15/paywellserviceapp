package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 1/25/17.
 */

public class NewArrivalPayLoadRequestBody {
    @SerializedName("Index")
    public int index;

    @SerializedName("Count")
    public int count;

    public NewArrivalPayLoadRequestBody() {
    }

    public NewArrivalPayLoadRequestBody(int index, int count) {
        this.index = index;
        this.count = count;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "NewArrivalPayLoadRequestBody{" +
                "index=" + index +
                ", count=" + count +
                '}';
    }
}
