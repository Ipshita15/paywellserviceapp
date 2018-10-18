package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/21/16.
 */
public class SearchInMerchantResponsModel {
    @SerializedName("DealId")
    private int DealId;

    @SerializedName("DealTitle")
    private String DealTitle;

    @SerializedName("DealPrice")
    private int DealPrice;

    @SerializedName("DealDiscount")
    private int DealDiscount;

    @SerializedName("BulletDescription")
    private String BulletDescription;

    @SerializedName("FolderName")
    private String FolderName;

    @SerializedName("AccountsTitle")
    private String AccountsTitle;

    @SerializedName("RegularPrice")
    private int RegularPrice;

    @SerializedName("DealPriority")
    private int DealPriority;

    @SerializedName("SignUpStartingDate")
    private String SignUpStartingDate;

    @SerializedName("SignUpClosingDate")
    private String SignUpClosingDate;

    @SerializedName("IsSoldOut")
    private boolean IsSoldOut;

    @SerializedName("CategoryId")
    private int CategoryId;

    @SerializedName("SubcategoryId")
    private int SubcategoryId;

    @SerializedName("Category")
    private String Category;

    @SerializedName("Subcategory")
    private String Subcategory;

    public SearchInMerchantResponsModel(int dealId, String dealTitle, int dealPrice, int dealDiscount, String bulletDescription, String folderName, String accountsTitle,
                                        int regularPrice, int dealPriority, String signUpStartingDate, String signUpClosingDate, boolean isSoldOut, int categoryId,
                                        int subcategoryId, String category, String subcategory) {
        DealId = dealId;
        DealTitle = dealTitle;
        DealPrice = dealPrice;
        DealDiscount = dealDiscount;
        BulletDescription = bulletDescription;
        FolderName = folderName;
        AccountsTitle = accountsTitle;
        RegularPrice = regularPrice;
        DealPriority = dealPriority;
        SignUpStartingDate = signUpStartingDate;
        SignUpClosingDate = signUpClosingDate;
        IsSoldOut = isSoldOut;
        CategoryId = categoryId;
        SubcategoryId = subcategoryId;
        Category = category;
        Subcategory = subcategory;
    }

    public int getDealId() {
        return DealId;
    }

    public String getDealTitle() {
        return DealTitle;
    }

    public int getDealPrice() {
        return DealPrice;
    }

    public int getDealDiscount() {
        return DealDiscount;
    }

    public String getBulletDescription() {
        return BulletDescription;
    }

    public String getFolderName() {
        return FolderName;
    }

    public String getAccountsTitle() {
        return AccountsTitle;
    }

    public int getRegularPrice() {
        return RegularPrice;
    }

    public int getDealPriority() {
        return DealPriority;
    }

    public String getSignUpStartingDate() {
        return SignUpStartingDate;
    }

    public String getSignUpClosingDate() {
        return SignUpClosingDate;
    }

    public boolean isSoldOut() {
        return IsSoldOut;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getSubcategoryId() {
        return SubcategoryId;
    }

    public String getCategory() {
        return Category;
    }

    public String getSubcategory() {
        return Subcategory;
    }
}
