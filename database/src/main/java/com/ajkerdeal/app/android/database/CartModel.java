package com.ajkerdeal.app.android.database;

import java.util.Date;


/**
 * Created by mitu on 4/6/17.
 */

public class CartModel {

    private int id;
    private int mDealID;
    private int mDealPrice;
    private String mDealSize;
    private double mDeliveryCharge;
    private double mDeliveryChargeOutSide;
    private Date date;
    private int Qtn;


    public CartModel() {
    }

    public CartModel(int id, int mDealID) {
        this.id = id;
        this.mDealID = mDealID;
    }

    public CartModel(int id, int mDealID, String mDealSize) {
        this.id = id;
        this.mDealID = mDealID;
        this.mDealSize = mDealSize;
    }

    public CartModel(int id, int mDealID, String mDealSize, double mDeliveryCharge) {
        this.id = id;
        this.mDealID = mDealID;
        this.mDealSize = mDealSize;
        this.mDeliveryCharge = mDeliveryCharge;
    }

    public CartModel(int id, int mDealID, int mDealPrice, String mDealSize, double mDeliveryCharge, double mDeliveryChargeOutSide, Date date, int qtn) {
        this.id = id;
        this.mDealID = mDealID;
        this.mDealPrice = mDealPrice;
        this.mDealSize = mDealSize;
        this.mDeliveryCharge = mDeliveryCharge;
        this.mDeliveryChargeOutSide = mDeliveryChargeOutSide;
        this.date = date;
        Qtn = qtn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getmDealID() {
        return mDealID;
    }

    public void setmDealID(int mDealID) {
        this.mDealID = mDealID;
    }

    public int getmDealPrice() {
        return mDealPrice;
    }

    public void setmDealPrice(int mDealPrice) {
        this.mDealPrice = mDealPrice;
    }

    public String getmDealSize() {
        return mDealSize;
    }

    public void setmDealSize(String mDealSize) {
        this.mDealSize = mDealSize;
    }

    public double getmDeliveryCharge() {
        return mDeliveryCharge;
    }

    public void setmDeliveryCharge(double mDeliveryCharge) {
        this.mDeliveryCharge = mDeliveryCharge;
    }

    public double getmDeliveryChargeOutSide() {
        return mDeliveryChargeOutSide;
    }

    public void setmDeliveryChargeOutSide(double mDeliveryChargeOutSide) {
        this.mDeliveryChargeOutSide = mDeliveryChargeOutSide;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQtn() {
        return Qtn;
    }

    public void setQtn(int qtn) {
        Qtn = qtn;
    }
}
