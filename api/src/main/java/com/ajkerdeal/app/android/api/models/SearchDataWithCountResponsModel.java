package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/20/16.
 */
public class SearchDataWithCountResponsModel {
    @SerializedName("DealId")
    private int DealId;

    @SerializedName("DealTitle")
    private String DealTitle;

    @SerializedName("DealPrice")
    private  int DealPrice;

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

    public SearchDataWithCountResponsModel(int dealId, String dealTitle, int dealPrice, int dealDiscount,
                                           String bulletDescription, String folderName,
                                           String accountsTitle, int regularPrice, int dealPriority,
                                           String signUpStartingDate) {
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
}
