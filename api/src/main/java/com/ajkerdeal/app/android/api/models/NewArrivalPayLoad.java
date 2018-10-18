package com.ajkerdeal.app.android.api.models;

/**
 * Created by Rasel on 1/22/2017.
 */

public class NewArrivalPayLoad {

    int DealId;
    String DealTitle;
    int CategoryId;
    int SubCategoryId;
    String FolderName;
    String ImageLink;
    int IsSoldOut;
    int DealPriority;
    String AccountsTitle;
    String CustomiseMsg;
    int DealPrice;
    String Subcategory;
    String Category;
    String SubcategoryEng;
    String CategoryEng;

    public NewArrivalPayLoad() {
    }

    public NewArrivalPayLoad(int dealId, String dealTitle, int categoryId, int subCategoryId, String folderName, String imageLink, int isSoldOut, int dealPriority, String accountsTitle, String customiseMsg, int dealPrice, String subcategory, String category, String subcategoryEng, String categoryEng) {
        DealId = dealId;
        DealTitle = dealTitle;
        CategoryId = categoryId;
        SubCategoryId = subCategoryId;
        FolderName = folderName;
        ImageLink = imageLink;
        IsSoldOut = isSoldOut;
        DealPriority = dealPriority;
        AccountsTitle = accountsTitle;
        CustomiseMsg = customiseMsg;
        DealPrice = dealPrice;
        Subcategory = subcategory;
        Category = category;
        SubcategoryEng = subcategoryEng;
        CategoryEng = categoryEng;
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

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        SubCategoryId = subCategoryId;
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

    public int getIsSoldOut() {
        return IsSoldOut;
    }

    public void setIsSoldOut(int isSoldOut) {
        IsSoldOut = isSoldOut;
    }

    public int getDealPriority() {
        return DealPriority;
    }

    public void setDealPriority(int dealPriority) {
        DealPriority = dealPriority;
    }

    public String getAccountsTitle() {
        return AccountsTitle;
    }

    public void setAccountsTitle(String accountsTitle) {
        AccountsTitle = accountsTitle;
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

    public String getSubcategory() {
        return Subcategory;
    }

    public void setSubcategory(String subcategory) {
        Subcategory = subcategory;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubcategoryEng() {
        return SubcategoryEng;
    }

    public void setSubcategoryEng(String subcategoryEng) {
        SubcategoryEng = subcategoryEng;
    }

    public String getCategoryEng() {
        return CategoryEng;
    }

    public void setCategoryEng(String categoryEng) {
        CategoryEng = categoryEng;
    }

    @Override
    public String toString() {
        return "NewArrivalPayLoad{" +
                "DealId=" + DealId +
                ", DealTitle='" + DealTitle + '\'' +
                ", CategoryId=" + CategoryId +
                ", SubCategoryId=" + SubCategoryId +
                ", FolderName='" + FolderName + '\'' +
                ", ImageLink='" + ImageLink + '\'' +
                ", IsSoldOut=" + IsSoldOut +
                ", DealPriority=" + DealPriority +
                ", AccountsTitle='" + AccountsTitle + '\'' +
                ", CustomiseMsg='" + CustomiseMsg + '\'' +
                ", DealPrice=" + DealPrice +
                ", Subcategory='" + Subcategory + '\'' +
                ", Category='" + Category + '\'' +
                ", SubcategoryEng='" + SubcategoryEng + '\'' +
                ", CategoryEng='" + CategoryEng + '\'' +
                '}';
    }
}
