package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 8/14/16.
 */
public class NinetyNineShopCountBody {

   private int MinPrice;
   private int MaxPrice;

    public NinetyNineShopCountBody(int minPrice, int maxPrice) {
        MinPrice = minPrice;
        MaxPrice = maxPrice;
    }

    public int getMinPrice() {
        return MinPrice;
    }

    public int getMaxPrice() {
        return MaxPrice;
    }

    @Override
    public String toString() {
        return "NinetyNineShopCountBody{" +
                "MinPrice=" + MinPrice +
                ", MaxPrice=" + MaxPrice +
                '}';
    }
}
