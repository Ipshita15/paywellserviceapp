package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 7/23/16.
 */
public class MerchantwiseProductBody {



            private int ProfileID;
            private int SubCategoryId;
            private int SubSubCategoryId;
            private int MinPrice;
            private int MaxPrice;
            private String  DealList;
            private int Index;
            private int Count;
            private String  OrderBy;
            private String  SortBy;

    public MerchantwiseProductBody(int profileID, int subCategoryId, int subSubCategoryId, int minPrice, int maxPrice, String dealList, int index, int count, String orderBy, String sortBy) {
        ProfileID = profileID;
        SubCategoryId = subCategoryId;
        SubSubCategoryId = subSubCategoryId;
        MinPrice = minPrice;
        MaxPrice = maxPrice;
        DealList = dealList;
        Index = index;
        Count = count;
        OrderBy = orderBy;
        SortBy = sortBy;
    }

    @Override
    public String toString() {
        return "MerchantwiseProductBody{" +
                "ProfileID=" + ProfileID +
                ", SubCategoryId=" + SubCategoryId +
                ", SubSubCategoryId=" + SubSubCategoryId +
                ", MinPrice=" + MinPrice +
                ", MaxPrice=" + MaxPrice +
                ", DealList='" + DealList + '\'' +
                ", LowerLimit=" + Index +
                ", UpperLimit=" + Count +
                ", OrderBy='" + OrderBy + '\'' +
                ", SortBy='" + SortBy + '\'' +
                '}';
    }
}
