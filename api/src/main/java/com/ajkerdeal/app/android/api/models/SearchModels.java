package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by mitu on 4/10/16.
 */
public class SearchModels {
    @SerializedName("Deals")
    private SearchDealsModel[] Deals;
    @SerializedName("Categories")
    private SearchCategoriesModel[] Categories;
    @SerializedName("Merchants")
    private SearchMerchantsModel[] Merchants;

    public SearchModels(SearchDealsModel[] deals, SearchCategoriesModel[] categories, SearchMerchantsModel[] merchants) {
        Deals = deals;
        Categories = categories;
        Merchants = merchants;
    }

    public SearchDealsModel[] getDeals() {
        return Deals;
    }

    public SearchCategoriesModel[] getCategories() {
        return Categories;
    }

    public SearchMerchantsModel[] getMerchants() {
        return Merchants;
    }

    @Override
    public String toString() {
        return "SearchModels{" +
                "Deals=" + Arrays.toString(Deals) +
                ", Categories=" + Arrays.toString(Categories) +
                ", Merchants=" + Arrays.toString(Merchants) +
                '}';
    }
}
