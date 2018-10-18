package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 6/18/16.
 */
public class
WishlistModelAll {


    private int DealId;
    private String DealTitle;
    private String AccountsTitle;
    private String FolderName;
    private int DealPrice;
    private int DealDiscount;
    private int RegularPrice;
    private boolean IsSoldOut;
    private int ProfileId;
    private int CategoryId;
    private int SubCategoryId;
    private int SubSubCategoryId;
    private String CName;
    private int CustomerId;
    private int ImageLocation;

    public WishlistModelAll(int dealId, String dealTitle, String accountsTitle, String folderName, int dealPrice, int dealDiscount, int regularPrice, boolean isSoldOut, int profileId, int categoryId, int subCategoryId, int subSubCategoryId, String CName, int customerId) {
        DealId = dealId;
        DealTitle = dealTitle;
        AccountsTitle = accountsTitle;
        FolderName = folderName;
        DealPrice = dealPrice;
        DealDiscount = dealDiscount;
        RegularPrice = regularPrice;
        IsSoldOut = isSoldOut;
        ProfileId = profileId;
        CategoryId = categoryId;
        SubCategoryId = subCategoryId;
        SubSubCategoryId = subSubCategoryId;
        this.CName = CName;
        CustomerId = customerId;
    }

    public int getDealId() {
        return DealId;
    }

    public String getDealTitle() {
        return DealTitle;
    }

    public String getAccountsTitle() {
        return AccountsTitle;
    }

    public String getFolderName() {
        return FolderName;
    }

    public int getDealPrice() {
        return DealPrice;
    }

    public int getDealDiscount() {
        return DealDiscount;
    }

    public int getRegularPrice() {
        return RegularPrice;
    }

    public boolean isSoldOut() {
        return IsSoldOut;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public int getSubSubCategoryId() {
        return SubSubCategoryId;
    }

    public String getCName() {
        return CName;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setImageLocation(int imageLocation) {
        ImageLocation = imageLocation;
    }

    public int getImageLocation() {
        return ImageLocation;
    }

    @Override
    public String toString() {
        return "WishlistModelAll{" +
                "DealId=" + DealId +
                ", DealTitle='" + DealTitle + '\'' +
                ", AccountsTitle='" + AccountsTitle + '\'' +
                ", FolderName='" + FolderName + '\'' +
                ", DealPrice=" + DealPrice +
                ", DealDiscount=" + DealDiscount +
                ", RegularPrice=" + RegularPrice +
                ", IsSoldOut=" + IsSoldOut +
                ", ProfileId=" + ProfileId +
                ", CategoryId=" + CategoryId +
                ", SubCategoryId=" + SubCategoryId +
                ", SubSubCategoryId=" + SubSubCategoryId +
                ", CName='" + CName + '\'' +
                ", CustomerId=" + CustomerId +
                '}';
    }
}
