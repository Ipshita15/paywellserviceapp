package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeCategoryBlockModel {
    @SerializedName("Id")
    private int id;
    @SerializedName("CategoryId")
    private int categoryId;
    @SerializedName("SubcategoryId")
    private int subcategoryId;
    @SerializedName("BanglaCategoryName")
    private String banglaCategoryName;
    @SerializedName("Deals")
    private List<HomeCategoryBlockProductModel> listOfDeals;

    public HomeCategoryBlockModel(int Id, int CategoryId, int SubcategoryId, String BanglaCategoryName, List<HomeCategoryBlockProductModel> Deals) {
        this.id = Id;
        this.categoryId = CategoryId;
        this.subcategoryId = SubcategoryId;
        this.banglaCategoryName = BanglaCategoryName;
        this.listOfDeals = Deals;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public String getBanglaCategoryName() {
        return banglaCategoryName;
    }

    public List<HomeCategoryBlockProductModel> getDeals() {
        return listOfDeals;
    }


    @Override
    public String toString() {
        return "HomeCategoryBlockModel{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", subcategoryId=" + subcategoryId +
                ", banglaCategoryName='" + banglaCategoryName + '\'' +
                ", listOfDeals=" + listOfDeals +
                '}';
    }
}
