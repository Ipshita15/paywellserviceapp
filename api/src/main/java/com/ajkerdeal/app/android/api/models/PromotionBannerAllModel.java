package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamim on 12/29/2016.
 */

public class PromotionBannerAllModel {

    @SerializedName("DealId")
    private int dealId;

    @SerializedName("DealTitle")
    private String dealTitle;

    @SerializedName("AccountsTitle")
    private String accountsTitle;

    @SerializedName("FolderName")
    private String folderName;

    @SerializedName("ImageLink")
    private String imageLink;

    @SerializedName("CustomiseMsg")
    private String customiseMsg;

    @SerializedName("DealPrice")
    private int dealPrice;

    @SerializedName("DealDiscount")
    private int dealDiscount;

    @SerializedName("RegularPrice")
    private int regularPrice;

    @SerializedName("IsSoldOut")
    private boolean isSoldOut;

    @SerializedName("DealPriority")
    private int dealPriority;

    @SerializedName("ProfileId")
    private int profileId;

    @SerializedName("CompanyName")
    private String companyName;

    @SerializedName("CategoryId")
    private int categoryId;

    @SerializedName("Category")
    private String category;

    @SerializedName("CategoryEng")
    private String categoryEng;

    @SerializedName("SubCategoryId")
    private int subCategoryId;

    @SerializedName("SubCategory")
    private String subCategory;

    @SerializedName("SubCategoryEng")
    private String subCategoryEng;

    @SerializedName("SubSubCategoryId")
    private int subSubCategoryId;

    @SerializedName("SubSubCategory")
    private String subSubCategory;

    @SerializedName("SubSubCategoryEng")
    private String subSubCategoryEng;

    @SerializedName("Total")
    private int total;

    @SerializedName("ShowingPriority")
    private int showingPriority;

    public PromotionBannerAllModel(int dealId, String dealTitle, String accountsTitle, String folderName,
                                   String imageLink, String customiseMsg, int dealPrice, int dealDiscount,
                                   int regularPrice, boolean isSoldOut, int dealPriority, int profileId,
                                   String companyName, int categoryId, String category, String categoryEng,
                                   int subCategoryId, String subCategory, String subCategoryEng, int subSubCategoryId,
                                   String subSubCategory, String subSubCategoryEng, int total, int showingPriority) {
        this.dealId = dealId;
        this.dealTitle = dealTitle;
        this.accountsTitle = accountsTitle;
        this.folderName = folderName;
        this.imageLink = imageLink;
        this.customiseMsg = customiseMsg;
        this.dealPrice = dealPrice;
        this.dealDiscount = dealDiscount;
        this.regularPrice = regularPrice;
        this.isSoldOut = isSoldOut;
        this.dealPriority = dealPriority;
        this.profileId = profileId;
        this.companyName = companyName;
        this.categoryId = categoryId;
        this.category = category;
        this.categoryEng = categoryEng;
        this.subCategoryId = subCategoryId;
        this.subCategory = subCategory;
        this.subCategoryEng = subCategoryEng;
        this.subSubCategoryId = subSubCategoryId;
        this.subSubCategory = subSubCategory;
        this.subSubCategoryEng = subSubCategoryEng;
        this.total = total;
        this.showingPriority = showingPriority;
    }

    public int getDealId() {
        return dealId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public String getAccountsTitle() {
        return accountsTitle;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getCustomiseMsg() {
        return customiseMsg;
    }

    public int getDealPrice() {
        return dealPrice;
    }

    public int getDealDiscount() {
        return dealDiscount;
    }

    public int getRegularPrice() {
        return regularPrice;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public int getDealPriority() {
        return dealPriority;
    }

    public int getProfileId() {
        return profileId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryEng() {
        return categoryEng;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getSubCategoryEng() {
        return subCategoryEng;
    }

    public int getSubSubCategoryId() {
        return subSubCategoryId;
    }

    public String getSubSubCategory() {
        return subSubCategory;
    }

    public String getSubSubCategoryEng() {
        return subSubCategoryEng;
    }

    public int getTotal() {
        return total;
    }

    public int getShowingPriority() {
        return showingPriority;
    }
}
