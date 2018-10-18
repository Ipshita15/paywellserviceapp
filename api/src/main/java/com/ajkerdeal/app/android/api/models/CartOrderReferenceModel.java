package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitu on 5/17/17.
 */

public class CartOrderReferenceModel {
 @SerializedName("ReferenceId")
    private List<Integer> mCartReferenceID;

    public CartOrderReferenceModel(List<Integer> mCartReferenceID) {
        this.mCartReferenceID = mCartReferenceID;
    }

    public List<Integer> getmCartReferenceID() {
        return mCartReferenceID;
    }

    @Override
    public String toString() {
        return "CartOrderReferenceModel{" +
                "mCartReferenceID=" + mCartReferenceID +
                '}';
    }
}
