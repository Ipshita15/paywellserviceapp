package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitu on 7/10/17.
 */

public class ElasticSearchModel {

    @SerializedName("CategorySuggestion")
    public List<CategorySuggestion> categorySuggestion = null;
    @SerializedName("ProductSuggestion")
    public List<ProductSuggestion> productSuggestion = null;
    @SerializedName("MerchantSuggestion")
    public List<MerchantSuggestion> merchantSuggestion = null;

    public ElasticSearchModel(List<CategorySuggestion> categorySuggestion, List<ProductSuggestion> productSuggestion, List<MerchantSuggestion> merchantSuggestion) {
        this.categorySuggestion = categorySuggestion;
        this.productSuggestion = productSuggestion;
        this.merchantSuggestion = merchantSuggestion;
    }

    public List<CategorySuggestion> getCategorySuggestion() {
        return categorySuggestion;
    }

    public void setCategorySuggestion(List<CategorySuggestion> categorySuggestion) {
        this.categorySuggestion = categorySuggestion;
    }

    public List<ProductSuggestion> getProductSuggestion() {
        return productSuggestion;
    }

    public void setProductSuggestion(List<ProductSuggestion> productSuggestion) {
        this.productSuggestion = productSuggestion;
    }

    public List<MerchantSuggestion> getMerchantSuggestion() {
        return merchantSuggestion;
    }

    public void setMerchantSuggestion(List<MerchantSuggestion> merchantSuggestion) {
        this.merchantSuggestion = merchantSuggestion;
    }

    @Override
    public String toString() {
        return "ElasticSearchModel{" +
                "categorySuggestion=" + categorySuggestion +
                ", productSuggestion=" + productSuggestion +
                ", merchantSuggestion=" + merchantSuggestion +
                '}';
    }
}
