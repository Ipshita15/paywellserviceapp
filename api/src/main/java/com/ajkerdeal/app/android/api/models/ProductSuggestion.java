package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitu on 7/10/17.
 */

public class ProductSuggestion {
    @SerializedName("Id")

    public int id;
    @SerializedName("DealId")

    public int dealId;
    @SerializedName("DealTitle")

    public String dealTitle;
    @SerializedName("DealTitleWithSubText")

    public String dealTitleWithSubText;
    @SerializedName("DealTitleEng")

    public String dealTitleEng;
    @SerializedName("DealTitleEngWithSubText")

    public String dealTitleEngWithSubText;
    @SerializedName("DealPrice")

    public String dealPrice;
    @SerializedName("DealDiscountInPercent")

    public String dealDiscountInPercent;
    @SerializedName("FolderName")

    public String folderName;
    @SerializedName("InsertedOn")

    public String insertedOn;
    @SerializedName("SocialTitle")

    public String socialTitle;
    @SerializedName("SocialDescription")

    public String socialDescription;
    @SerializedName("BulletDescription")

    public String bulletDescription;
    @SerializedName("BulletDescriptionEng")

    public String bulletDescriptionEng;
    @SerializedName("DealKeywords")

    public String dealKeywords;
    @SerializedName("ProfileId")

    public int profileId;
    @SerializedName("CompanyName")

    public String companyName;
    @SerializedName("BrandId")

    public int brandId;
    @SerializedName("BrandName")

    public String brandName;
    @SerializedName("IsActive")

    public boolean isActive;
    @SerializedName("IsHotDeal")

    public boolean isHotDeal;
    @SerializedName("DealPriority")

    public int dealPriority;
    @SerializedName("Category")

    public List<Integer> category = null;
    @SerializedName("SubCategory")

    public List<Integer> subCategory = null;
    @SerializedName("SubSubCategory")

    public List<Integer> subSubCategory = null;

    public ProductSuggestion(int id, int dealId, String dealTitle, String dealTitleWithSubText, String dealTitleEng, String dealTitleEngWithSubText, String dealPrice, String dealDiscountInPercent, String folderName, String insertedOn, String socialTitle, String socialDescription, String bulletDescription, String bulletDescriptionEng, String dealKeywords, int profileId, String companyName, int brandId, String brandName, boolean isActive, boolean isHotDeal, int dealPriority, List<Integer> category, List<Integer> subCategory, List<Integer> subSubCategory) {
        this.id = id;
        this.dealId = dealId;
        this.dealTitle = dealTitle;
        this.dealTitleWithSubText = dealTitleWithSubText;
        this.dealTitleEng = dealTitleEng;
        this.dealTitleEngWithSubText = dealTitleEngWithSubText;
        this.dealPrice = dealPrice;
        this.dealDiscountInPercent = dealDiscountInPercent;
        this.folderName = folderName;
        this.insertedOn = insertedOn;
        this.socialTitle = socialTitle;
        this.socialDescription = socialDescription;
        this.bulletDescription = bulletDescription;
        this.bulletDescriptionEng = bulletDescriptionEng;
        this.dealKeywords = dealKeywords;
        this.profileId = profileId;
        this.companyName = companyName;
        this.brandId = brandId;
        this.brandName = brandName;
        this.isActive = isActive;
        this.isHotDeal = isHotDeal;
        this.dealPriority = dealPriority;
        this.category = category;
        this.subCategory = subCategory;
        this.subSubCategory = subSubCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public String getDealTitleWithSubText() {
        return dealTitleWithSubText;
    }

    public void setDealTitleWithSubText(String dealTitleWithSubText) {
        this.dealTitleWithSubText = dealTitleWithSubText;
    }

    public String getDealTitleEng() {
        return dealTitleEng;
    }

    public void setDealTitleEng(String dealTitleEng) {
        this.dealTitleEng = dealTitleEng;
    }

    public String getDealTitleEngWithSubText() {
        return dealTitleEngWithSubText;
    }

    public void setDealTitleEngWithSubText(String dealTitleEngWithSubText) {
        this.dealTitleEngWithSubText = dealTitleEngWithSubText;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
    }

    public String getDealDiscountInPercent() {
        return dealDiscountInPercent;
    }

    public void setDealDiscountInPercent(String dealDiscountInPercent) {
        this.dealDiscountInPercent = dealDiscountInPercent;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getInsertedOn() {
        return insertedOn;
    }

    public void setInsertedOn(String insertedOn) {
        this.insertedOn = insertedOn;
    }

    public String getSocialTitle() {
        return socialTitle;
    }

    public void setSocialTitle(String socialTitle) {
        this.socialTitle = socialTitle;
    }

    public String getSocialDescription() {
        return socialDescription;
    }

    public void setSocialDescription(String socialDescription) {
        this.socialDescription = socialDescription;
    }

    public String getBulletDescription() {
        return bulletDescription;
    }

    public void setBulletDescription(String bulletDescription) {
        this.bulletDescription = bulletDescription;
    }

    public String getBulletDescriptionEng() {
        return bulletDescriptionEng;
    }

    public void setBulletDescriptionEng(String bulletDescriptionEng) {
        this.bulletDescriptionEng = bulletDescriptionEng;
    }

    public String getDealKeywords() {
        return dealKeywords;
    }

    public void setDealKeywords(String dealKeywords) {
        this.dealKeywords = dealKeywords;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isHotDeal() {
        return isHotDeal;
    }

    public void setHotDeal(boolean hotDeal) {
        isHotDeal = hotDeal;
    }

    public int getDealPriority() {
        return dealPriority;
    }

    public void setDealPriority(int dealPriority) {
        this.dealPriority = dealPriority;
    }

    public List<Integer> getCategory() {
        return category;
    }

    public void setCategory(List<Integer> category) {
        this.category = category;
    }

    public List<Integer> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<Integer> subCategory) {
        this.subCategory = subCategory;
    }

    public List<Integer> getSubSubCategory() {
        return subSubCategory;
    }

    public void setSubSubCategory(List<Integer> subSubCategory) {
        this.subSubCategory = subSubCategory;
    }

    @Override
    public String toString() {
        return "ProductSuggestion{" +
                "id=" + id +
                ", dealId=" + dealId +
                ", dealTitle='" + dealTitle + '\'' +
                ", dealTitleWithSubText='" + dealTitleWithSubText + '\'' +
                ", dealTitleEng='" + dealTitleEng + '\'' +
                ", dealTitleEngWithSubText='" + dealTitleEngWithSubText + '\'' +
                ", dealPrice='" + dealPrice + '\'' +
                ", dealDiscountInPercent=" + dealDiscountInPercent +
                ", folderName='" + folderName + '\'' +
                ", insertedOn='" + insertedOn + '\'' +
                ", socialTitle='" + socialTitle + '\'' +
                ", socialDescription='" + socialDescription + '\'' +
                ", bulletDescription='" + bulletDescription + '\'' +
                ", bulletDescriptionEng='" + bulletDescriptionEng + '\'' +
                ", dealKeywords='" + dealKeywords + '\'' +
                ", profileId=" + profileId +
                ", companyName='" + companyName + '\'' +
                ", brandId=" + brandId +
                ", brandName=" + brandName +
                ", isActive=" + isActive +
                ", isHotDeal=" + isHotDeal +
                ", dealPriority=" + dealPriority +
                ", category=" + category +
                ", subCategory=" + subCategory +
                ", subSubCategory=" + subSubCategory +
                '}';
    }
}
