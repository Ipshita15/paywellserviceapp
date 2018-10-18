package com.ajkerdeal.app.android.api.models;

/**
 * Created by MhRaju on 1/12/2017.
 */

public class PostEventDealsModel {


    private String EventId;
    private String MinDiscountPercantage;
    private String MaxDiscountPercantage;
    private String LowerLimit;
    private String Upperlimit;
    private String LowestPrice;
    private String HighestPrice;
    private String CatId;
    private String SubCatId;
    private String SubsubCatId;
    private String OrderBy;
    private String SortBy;

    public PostEventDealsModel(String eventId, String minDiscountPercantage, String maxDiscountPercantage, String lowerLimit, String upperlimit, String lowestPrice, String highestPrice, String catId, String subCatId, String subsubCatId, String orderBy, String sortBy) {
        EventId = eventId;
        MinDiscountPercantage = minDiscountPercantage;
        MaxDiscountPercantage = maxDiscountPercantage;
        LowerLimit = lowerLimit;
        Upperlimit = upperlimit;
        LowestPrice = lowestPrice;
        HighestPrice = highestPrice;
        CatId = catId;
        SubCatId = subCatId;
        SubsubCatId = subsubCatId;
        OrderBy = orderBy;
        SortBy = sortBy;
    }

    public String getEventId() {
        return EventId;
    }

    public String getMinDiscountPercantage() {
        return MinDiscountPercantage;
    }

    public String getMaxDiscountPercantage() {
        return MaxDiscountPercantage;
    }

    public String getLowerLimit() {
        return LowerLimit;
    }

    public String getUpperlimit() {
        return Upperlimit;
    }

    public String getLowestPrice() {
        return LowestPrice;
    }

    public String getHighestPrice() {
        return HighestPrice;
    }

    public String getCatId() {
        return CatId;
    }

    public String getSubCatId() {
        return SubCatId;
    }

    public String getSubsubCatId() {
        return SubsubCatId;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public String getSortBy() {
        return SortBy;
    }
}
