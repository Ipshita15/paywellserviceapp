package com.ajkerdeal.app.android.database;

import java.util.Date;



/**
 * Created by mitu on 6/10/17.
 */

public class CartItemModel {
    public int id;
    public int DealID;
    public int DealPrice;
    public String DealSize;
    public double DeliveryCharge;
    public double DeliveryChargeOutSide;
    public int Qtn;

    public CartItemModel() {
    }

    public CartItemModel(int id, int dealID, int dealPrice, String dealSize, double deliveryCharge, double deliveryChargeOutSide, int qtn) {
        this.id = id;
        DealID = dealID;
        DealPrice = dealPrice;
        DealSize = dealSize;
        DeliveryCharge = deliveryCharge;
        DeliveryChargeOutSide = deliveryChargeOutSide;
        Qtn = qtn;
    }

    public CartItemModel(int dealID, int dealPrice, String dealSize, double deliveryCharge, double deliveryChargeOutSide, int qtn) {
        DealID = dealID;
        DealPrice = dealPrice;
        DealSize = dealSize;
        DeliveryCharge = deliveryCharge;
        DeliveryChargeOutSide = deliveryChargeOutSide;
        Qtn = qtn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDealID() {
        return DealID;
    }

    public void setDealID(int dealID) {
        DealID = dealID;
    }

    public int getDealPrice() {
        return DealPrice;
    }

    public void setDealPrice(int dealPrice) {
        DealPrice = dealPrice;
    }

    public String getDealSize() {
        return DealSize;
    }

    public void setDealSize(String dealSize) {
        DealSize = dealSize;
    }

    public double getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    public double getDeliveryChargeOutSide() {
        return DeliveryChargeOutSide;
    }

    public void setDeliveryChargeOutSide(double deliveryChargeOutSide) {
        DeliveryChargeOutSide = deliveryChargeOutSide;
    }

    public int getQtn() {
        return Qtn;
    }

    public void setQtn(int qtn) {
        Qtn = qtn;
    }
}
