package com.ajkerdeal.app.android.api.models;

import java.util.List;

/**
 * Created by piash on 6/18/16.
 */
public class WishlistBody {

    private int CustomerId;
    private List<Integer> DealList;
    private String OrderFrom;


    public WishlistBody(int customerId, List<Integer> dealList, String orderFrom) {
        CustomerId = customerId;
        DealList = dealList;
        OrderFrom = orderFrom;
    }

    @Override
    public String toString() {
        return "WishlistBody{" +
                "CustomerId=" + CustomerId +
                ", DealList=" + DealList +
                ", OrderFrom='" + OrderFrom + '\'' +
                '}';
    }
}
