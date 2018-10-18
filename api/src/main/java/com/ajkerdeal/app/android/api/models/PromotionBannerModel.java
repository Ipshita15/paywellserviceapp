package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamim on 10/26/2016.
 */

public class PromotionBannerModel {

    @SerializedName("CategoryId")
    private int categoryId;

    @SerializedName("SubCategoryId")
    private int subCategoryId;


    @SerializedName("SubSubCategoryId")
    private int subSubCategoryId;


    @SerializedName("PageName")
    private String pageName;

    @SerializedName("Position")
    private String position;

    @SerializedName("Action_url")
    private String actionUrl;

    @SerializedName("Image_Url")
    private String imageUrl;

    @SerializedName("PostedDate")
    private String postedDate;

    @SerializedName("MerchantId")
    private int merchantId;

    @SerializedName("DealId")
    private int dealId;

    public PromotionBannerModel() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public int getSubSubCategoryId() {
        return subSubCategoryId;
    }

    public void setSubSubCategoryId(int subSubCategoryId) {
        this.subSubCategoryId = subSubCategoryId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }


    @Override
    public String toString() {
        return "PromotionBannerModel{" +
                "categoryId=" + categoryId +
                ", subCategoryId=" + subCategoryId +
                ", subSubCategoryId=" + subSubCategoryId +
                ", pageName='" + pageName + '\'' +
                ", position='" + position + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", postedDate='" + postedDate + '\'' +
                ", merchantId=" + merchantId +
                ", dealId=" + dealId +
                '}';
    }
}
