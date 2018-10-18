package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitu on 7/10/17.
 */

public class ElasticSearachProduct {

    @SerializedName("Total")

    public int total;
    @SerializedName("Products")

    public List<Product> products = null;

    public ElasticSearachProduct(int total, List<Product> products) {
        this.total = total;
        this.products = products;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ElasticSearachProduct{" +
                "total=" + total +
                ", products=" + products +
                '}';
    }
}
