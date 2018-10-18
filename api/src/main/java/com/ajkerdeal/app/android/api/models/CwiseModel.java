package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 8/30/16.
 */
public class CwiseModel {

    private int DealId;
    private String DealTitle;
    private String AccountsTitle;
    private int DealPrice;
    private String FolderName;
    private String ImageLink;
    private int FavId;
    private int AwardType;
    private int AwardValue;
    private String AwardName;
    private int TotalPoint;
    private String CompanyName;
    private double CommissionPerCoupon;


    public CwiseModel(int dealId, String dealTitle, String accountsTitle, int dealPrice, String folderName, String imageLink, int favId, int awardType, int awardValue, String awardName, int totalPoint, String companyName, double commissionPerCoupon) {
        DealId = dealId;
        DealTitle = dealTitle;
        AccountsTitle = accountsTitle;
        DealPrice = dealPrice;
        FolderName = folderName;
        ImageLink = imageLink;
        FavId = favId;
        AwardType = awardType;
        AwardValue = awardValue;
        AwardName = awardName;
        TotalPoint = totalPoint;
        CompanyName = companyName;
        CommissionPerCoupon = commissionPerCoupon;
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

    public int getDealPrice() {
        return DealPrice;
    }

    public void setDealPrice(int dealPrice) {
        DealPrice = dealPrice;
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

    public int getFavId() {
        return FavId;
    }

    public void setFavId(int favId) {
        FavId = favId;
    }

    public int getAwardType() {
        return AwardType;
    }

    public void setAwardType(int awardType) {
        AwardType = awardType;
    }

    public int getAwardValue() {
        return AwardValue;
    }

    public void setAwardValue(int awardValue) {
        AwardValue = awardValue;
    }

    public String getAwardName() {
        return AwardName;
    }

    public void setAwardName(String awardName) {
        AwardName = awardName;
    }

    public int getTotalPoint() {
        return TotalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        TotalPoint = totalPoint;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public double getCommissionPerCoupon() {
        return CommissionPerCoupon;
    }

    public void setCommissionPerCoupon(double commissionPerCoupon) {
        CommissionPerCoupon = commissionPerCoupon;
    }


    @Override
    public String toString() {
        return "CwiseModel{" +
                "DealId=" + DealId +
                ", DealTitle='" + DealTitle + '\'' +
                ", AccountsTitle='" + AccountsTitle + '\'' +
                ", DealPrice=" + DealPrice +
                ", FolderName='" + FolderName + '\'' +
                ", ImageLink='" + ImageLink + '\'' +
                ", FavId=" + FavId +
                ", AwardType=" + AwardType +
                ", AwardValue=" + AwardValue +
                ", AwardName='" + AwardName + '\'' +
                ", TotalPoint=" + TotalPoint +
                ", CompanyName='" + CompanyName + '\'' +
                ", CommissionPerCoupon=" + CommissionPerCoupon +
                '}';
    }
}




