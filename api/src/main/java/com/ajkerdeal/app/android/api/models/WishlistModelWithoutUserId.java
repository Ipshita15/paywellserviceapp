package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by piash on 6/22/16.
 */
public class WishlistModelWithoutUserId {


   private  int DealId;
   private  String DealTitle;
   private  String FolderName;
   private  boolean IsSoldOut;
   private  int DealPprivate;
   private  String AccountsTitle;
   private  int DealPrice;
    private int ImageLocation;
    @SerializedName("MerchantId")
    private int mMerchantId;
    @SerializedName("QtnLimitPerUser")
    private int mQuantityLimitPerUser;
    @SerializedName("Sizes")
    private String mSizes;
    @SerializedName("Colors")
    private String mColors;

    public WishlistModelWithoutUserId(int dealId, String dealTitle, String folderName, boolean isSoldOut, int dealPprivate, String accountsTitle, int dealPrice) {
        DealId = dealId;
        DealTitle = dealTitle;
        FolderName = folderName;
        IsSoldOut = isSoldOut;
        DealPprivate = dealPprivate;
        AccountsTitle = accountsTitle;
        DealPrice = dealPrice;
    }

    public WishlistModelWithoutUserId(int dealId, String dealTitle, String folderName, boolean isSoldOut, int dealPprivate, String accountsTitle, int dealPrice, int imageLocation, int mMerchantId, int mQuantityLimitPerUser, String mSizes, String mColors) {
        DealId = dealId;
        DealTitle = dealTitle;
        FolderName = folderName;
        IsSoldOut = isSoldOut;
        DealPprivate = dealPprivate;
        AccountsTitle = accountsTitle;
        DealPrice = dealPrice;
        ImageLocation = imageLocation;
        this.mMerchantId = mMerchantId;
        this.mQuantityLimitPerUser = mQuantityLimitPerUser;
        this.mSizes = mSizes;
        this.mColors = mColors;
    }

    public int getDealId() {
        return DealId;
    }

    public String getDealTitle() {
        return DealTitle;
    }

    public String getFolderName() {
        return FolderName;
    }

    public boolean isSoldOut() {
        return IsSoldOut;
    }

    public int getDealPprivate() {
        return DealPprivate;
    }

    public String getAccountsTitle() {
        return AccountsTitle;
    }

    public int getDealPrice() {
        return DealPrice;
    }

    public void setImageLocation(int imageLocation) {
        ImageLocation = imageLocation;
    }

    public int getImageLocation() {
        return ImageLocation;
    }

    public String getmColors() {
        return mColors;
    }

    public void setmColors(String mColors) {
        this.mColors = mColors;
    }

    public String getmSizes() {
        return mSizes;
    }

    public void setmSizes(String mSizes) {
        this.mSizes = mSizes;
    }

    public int getmQuantityLimitPerUser() {
        return mQuantityLimitPerUser;
    }

    public void setmQuantityLimitPerUser(int mQuantityLimitPerUser) {
        this.mQuantityLimitPerUser = mQuantityLimitPerUser;
    }

    public int getmMerchantId() {
        return mMerchantId;
    }

    public void setmMerchantId(int mMerchantId) {
        this.mMerchantId = mMerchantId;
    }

    @Override
    public String toString() {
        return "WishlistModelWithoutUserId{" +
                "DealId=" + DealId +
                ", DealTitle='" + DealTitle + '\'' +
                ", FolderName='" + FolderName + '\'' +
                ", IsSoldOut=" + IsSoldOut +
                ", DealPprivate=" + DealPprivate +
                ", AccountsTitle='" + AccountsTitle + '\'' +
                ", DealPrice=" + DealPrice +
                '}';
    }
}
