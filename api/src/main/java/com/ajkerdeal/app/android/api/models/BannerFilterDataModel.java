package com.ajkerdeal.app.android.api.models;

/**
 * Created by Rasel on 1/3/2017.
 */

public class BannerFilterDataModel {

    int DealId;
    String DealTitle;
    String AccountsTitle;
    String FolderName;
    String ImageLink;
    String CustomiseMsg;
    int DealPrice;
    int DealDiscount;
    int RegularPrice;
    Boolean IsSoldOut;
    int DealPriority;
    int ProfileId;
    String CompanyName;
    int CategoryId;
    String Category;
    String CategoryEng;
    int SubCategoryId;
    String SubCategory;
    String SubCategoryEng;
    int SubSubCategoryId;
    String SubSubCategory;
    String SubSubCategoryEng;
    int Total;
    int ShowingPriority;

    public BannerFilterDataModel() {
    }

    public BannerFilterDataModel(int dealId, String dealTitle, String accountsTitle, String folderName, String imageLink, String customiseMsg, int dealPrice, int dealDiscount, int regularPrice, Boolean isSoldOut, int dealPriority, int profileId, String companyName, int categoryId, String category, String categoryEng, int subCategoryId, String subCategory, String subCategoryEng, int subSubCategoryId, String subSubCategory, String subSubCategoryEng, int total, int showingPriority) {
        DealId = dealId;
        DealTitle = dealTitle;
        AccountsTitle = accountsTitle;
        FolderName = folderName;
        ImageLink = imageLink;
        CustomiseMsg = customiseMsg;
        DealPrice = dealPrice;
        DealDiscount = dealDiscount;
        RegularPrice = regularPrice;
        IsSoldOut = isSoldOut;
        DealPriority = dealPriority;
        ProfileId = profileId;
        CompanyName = companyName;
        CategoryId = categoryId;
        Category = category;
        CategoryEng = categoryEng;
        SubCategoryId = subCategoryId;
        SubCategory = subCategory;
        SubCategoryEng = subCategoryEng;
        SubSubCategoryId = subSubCategoryId;
        SubSubCategory = subSubCategory;
        SubSubCategoryEng = subSubCategoryEng;
        Total = total;
        ShowingPriority = showingPriority;
    }

    public int getDealId() {
        return DealId;
    }

    public void setDealId(int dealId) {
        DealId = dealId;
    }

    public String getDealTitle() {
        return DealTitle;
    }

    public void setDealTitle(String dealTitle) {
        DealTitle = dealTitle;
    }

    public String getAccountsTitle() {
        return AccountsTitle;
    }

    public void setAccountsTitle(String accountsTitle) {
        AccountsTitle = accountsTitle;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public String getCustomiseMsg() {
        return CustomiseMsg;
    }

    public void setCustomiseMsg(String customiseMsg) {
        CustomiseMsg = customiseMsg;
    }

    public int getDealPrice() {
        return DealPrice;
    }

    public void setDealPrice(int dealPrice) {
        DealPrice = dealPrice;
    }

    public int getDealDiscount() {
        return DealDiscount;
    }

    public void setDealDiscount(int dealDiscount) {
        DealDiscount = dealDiscount;
    }

    public int getRegularPrice() {
        return RegularPrice;
    }

    public void setRegularPrice(int regularPrice) {
        RegularPrice = regularPrice;
    }

    public Boolean getSoldOut() {
        return IsSoldOut;
    }

    public void setSoldOut(Boolean soldOut) {
        IsSoldOut = soldOut;
    }

    public int getDealPriority() {
        return DealPriority;
    }

    public void setDealPriority(int dealPriority) {
        DealPriority = dealPriority;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCategoryEng() {
        return CategoryEng;
    }

    public void setCategoryEng(String categoryEng) {
        CategoryEng = categoryEng;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getSubCategoryEng() {
        return SubCategoryEng;
    }

    public void setSubCategoryEng(String subCategoryEng) {
        SubCategoryEng = subCategoryEng;
    }

    public int getSubSubCategoryId() {
        return SubSubCategoryId;
    }

    public void setSubSubCategoryId(int subSubCategoryId) {
        SubSubCategoryId = subSubCategoryId;
    }

    public String getSubSubCategory() {
        return SubSubCategory;
    }

    public void setSubSubCategory(String subSubCategory) {
        SubSubCategory = subSubCategory;
    }

    public String getSubSubCategoryEng() {
        return SubSubCategoryEng;
    }

    public void setSubSubCategoryEng(String subSubCategoryEng) {
        SubSubCategoryEng = subSubCategoryEng;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public int getShowingPriority() {
        return ShowingPriority;
    }

    public void setShowingPriority(int showingPriority) {
        ShowingPriority = showingPriority;
    }

    @Override
    public String toString() {
        return "BannerFilterDataModel{" +
                "DealId=" + DealId +
                ", DealTitle='" + DealTitle + '\'' +
                ", AccountsTitle='" + AccountsTitle + '\'' +
                ", FolderName='" + FolderName + '\'' +
                ", ImageLink='" + ImageLink + '\'' +
                ", CustomiseMsg='" + CustomiseMsg + '\'' +
                ", DealPrice=" + DealPrice +
                ", DealDiscount=" + DealDiscount +
                ", RegularPrice=" + RegularPrice +
                ", IsSoldOut=" + IsSoldOut +
                ", DealPriority=" + DealPriority +
                ", ProfileId=" + ProfileId +
                ", CompanyName='" + CompanyName + '\'' +
                ", CategoryId=" + CategoryId +
                ", Category='" + Category + '\'' +
                ", CategoryEng='" + CategoryEng + '\'' +
                ", SubCategoryId=" + SubCategoryId +
                ", SubCategory='" + SubCategory + '\'' +
                ", SubCategoryEng='" + SubCategoryEng + '\'' +
                ", SubSubCategoryId=" + SubSubCategoryId +
                ", SubSubCategory='" + SubSubCategory + '\'' +
                ", SubSubCategoryEng='" + SubSubCategoryEng + '\'' +
                ", Total=" + Total +
                ", ShowingPriority=" + ShowingPriority +
                '}';
    }
}
