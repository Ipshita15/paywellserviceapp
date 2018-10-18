package com.ajkerdeal.app.android.api.models;

/**
 * Created by MhRaju on 1/12/2017.
 */

public class PostEventDealsCount {


    private String MaxDiscountPercantage;
    private String MinDiscountPercantage;
    private String LowestPrice;
    private String HighestPrice;
    private String EventId;

    public PostEventDealsCount(String maxDiscountPercantage, String minDiscountPercantage, String lowestPrice, String highestPrice, String eventId) {
        MaxDiscountPercantage = maxDiscountPercantage;
        MinDiscountPercantage = minDiscountPercantage;
        LowestPrice = lowestPrice;
        HighestPrice = highestPrice;
        EventId = eventId;
    }

    public String getMaxDiscountPercantage() {
        return MaxDiscountPercantage;
    }

    public String getMinDiscountPercantage() {
        return MinDiscountPercantage;
    }

    public String getLowestPrice() {
        return LowestPrice;
    }

    public String getHighestPrice() {
        return HighestPrice;
    }

    public String getEventId() {
        return EventId;
    }
}
