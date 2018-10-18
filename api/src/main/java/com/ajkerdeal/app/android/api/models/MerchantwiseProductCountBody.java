package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 7/25/16.
 */
public class MerchantwiseProductCountBody {

    private int ProfileID;
    private int SubCategoryId;
    private int SubSubCategoryId;
    private int MinPrice;
    private int MaxPrice;

    public MerchantwiseProductCountBody(int profileID, int subCategoryId, int subSubCategoryId, int minPrice, int maxPrice) {
        ProfileID = profileID;
        SubCategoryId = subCategoryId;
        SubSubCategoryId = subSubCategoryId;
        MinPrice = minPrice;
        MaxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return "MerchantwiseProductCountBody{" +
                "ProfileID=" + ProfileID +
                ", SubCategoryId=" + SubCategoryId +
                ", SubSubCategoryId=" + SubSubCategoryId +
                ", MinPrice=" + MinPrice +
                ", MaxPrice=" + MaxPrice +
                '}';
    }
}
