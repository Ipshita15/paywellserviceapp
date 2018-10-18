package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 8/14/16.
 */
public class NinetyNineModelBody {

    private int MinPrice;
    private int MaxPrice;
    private int Index;
    private int Count;
    private String OrderBy;
    private String SortBy;

    public NinetyNineModelBody(int minPrice, int maxPrice, int index, int count, String orderBy, String sortBy) {
        MinPrice = minPrice;
        MaxPrice = maxPrice;
        Index = index;
        Count = count;
        OrderBy = orderBy;
        SortBy = sortBy;
    }

    public int getMinPrice() {
        return MinPrice;
    }

    public int getMaxPrice() {
        return MaxPrice;
    }

    public int getIndex() {
        return Index;
    }

    public int getCount() {
        return Count;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public String getSortBy() {
        return SortBy;
    }

    @Override
    public String toString() {
        return "NinetyNineModelBody{" +
                "MinPrice=" + MinPrice +
                ", MaxPrice=" + MaxPrice +
                ", Index=" + Index +
                ", Count=" + Count +
                ", OrderBy='" + OrderBy + '\'' +
                ", SortBy='" + SortBy + '\'' +
                '}';
    }
}
