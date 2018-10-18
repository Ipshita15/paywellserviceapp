package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 4/10/16.
 */
public class SearchDealsModel {
    @SerializedName("DealId")
    private int DealId;
    @SerializedName("DealPrice")
    private double DealPrice;
    @SerializedName("DealTitle")
    private String DealTitle;
    @SerializedName("FolderName")
    private  String FolderName;
    @SerializedName("AccountsTitle")
    private  String AccountsTitle;

    //Don't add Serializer to this variable
    private String mImage;



    public SearchDealsModel(int dealId, double dealPrice , String dealTitle, String folderName, String accountsTitle) {
        DealId = dealId;
        DealTitle = dealTitle;
        FolderName = folderName;
        AccountsTitle = accountsTitle;
        DealPrice = dealPrice;

        mImage = "http://www.ajkerdeal.com/Images/Deals/"+FolderName+"/smallImage1.jpg";
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

    public String getAccountsTitle() {
        return AccountsTitle;
    }
    public String getmImage(){return  mImage;}
    public double getDealPrice() { return DealPrice; }

    @Override
    public String toString() {
        return "SearchDealsModel{" +
                "DealId=" + DealId +
                ", DealTitle='" + DealTitle + '\'' +
                ", FolderName='" + FolderName + '\'' +
                ", AccountsTitle='" + AccountsTitle + '\'' +
                '}';
    }
}
