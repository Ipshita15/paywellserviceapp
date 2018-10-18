package com.ajkerdeal.app.android.api.models;

import java.util.List;

/**
 * Created by piash on 9/4/16.
 */
public class CwisewithCountModel {

    private int Count;
    private List<CwiseModel> CatgoryProductModel;

    public CwisewithCountModel(int count, List<CwiseModel> catgoryProductModel) {
        Count = count;
        CatgoryProductModel = catgoryProductModel;
    }

    public int getCount() {
        return Count;
    }

    public List<CwiseModel> getCatgoryProductModel() {
        return CatgoryProductModel;
    }

    @Override
    public String toString() {
        return "CwisewithCountModel{" +
                "Count=" + Count +
                ", CatgoryProductModel=" + CatgoryProductModel +
                '}';
    }
}
