package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 6/16/16.
 */
public class CustomerInfoOrderListModel {
    @SerializedName("CouponId")
    public int CouponId;
    @SerializedName("CouponPrice")
    public int CouponPrice;
    @SerializedName("CouponQtn")
    public int CouponQtn;
    @SerializedName("IsDone")
    public int IsDone;
    @SerializedName("BookingCode")
    public String BookingCode;
    @SerializedName("DealId")
    public int DealId;
    @SerializedName("DealTitle")
    public String DealTitle;
    @SerializedName("CouponExpiryDate")
    public String CouponExpiryDate;
    @SerializedName("SignupClosingDate")
    public String SignupClosingDate;
    @SerializedName("FolderName")
    public String FolderName;
    @SerializedName("DealPrice")
    public int DealPrice;
    @SerializedName("PostedOn")
    public String PostedOn;
    @SerializedName("DeliveryDate")
    public String DeliveryDate;
    @SerializedName("CompanyName")
    public String CompanyName;
    @SerializedName("CustomerBillingAddress")
    public String CustomerBillingAddress;
    @SerializedName("IsSoldOut")
    public boolean IsSoldOut;
    @SerializedName("ShopCartId")
    private int mShopcartID;

    public CustomerInfoOrderListModel(int couponId, int couponPrice, int couponQtn, int isDone, String bookingCode, int dealId, String dealTitle, String couponExpiryDate, String signupClosingDate, String folderName, int dealPrice, String postedOn, String deliveryDate, String companyName, String customerBillingAddress, boolean isSoldOut, int mShopcartID) {
        CouponId = couponId;
        CouponPrice = couponPrice;
        CouponQtn = couponQtn;
        IsDone = isDone;
        BookingCode = bookingCode;
        DealId = dealId;
        DealTitle = dealTitle;
        CouponExpiryDate = couponExpiryDate;
        SignupClosingDate = signupClosingDate;
        FolderName = folderName;
        DealPrice = dealPrice;
        PostedOn = postedOn;
        DeliveryDate = deliveryDate;
        CompanyName = companyName;
        CustomerBillingAddress = customerBillingAddress;
        IsSoldOut = isSoldOut;
        this.mShopcartID = mShopcartID;
    }

    public CustomerInfoOrderListModel(int couponId, int couponPrice, int couponQtn, int isDone, String bookingCode, int dealId,
                                      String dealTitle, String couponExpiryDate, String signupClosingDate, String folderName,
                                      int dealPrice, String postedOn, String deliveryDate, String companyName, String customerBillingAddress,
                                      boolean isSoldOut) {
        CouponId = couponId;
        CouponPrice = couponPrice;
        CouponQtn = couponQtn;
        IsDone = isDone;
        BookingCode = bookingCode;
        DealId = dealId;
        DealTitle = dealTitle;
        CouponExpiryDate = couponExpiryDate;
        SignupClosingDate = signupClosingDate;
        FolderName = folderName;
        DealPrice = dealPrice;
        PostedOn = postedOn;
        DeliveryDate = deliveryDate;
        CompanyName = companyName;
        CustomerBillingAddress = customerBillingAddress;
        IsSoldOut = isSoldOut;
    }

    public int getCouponId() {
        return CouponId;
    }

    public int getCouponPrice() {
        return CouponPrice;
    }

    public int getCouponQtn() {
        return CouponQtn;
    }

    public int getIsDone() {
        return IsDone;
    }

    public String getBookingCode() {
        return BookingCode;
    }

    public int getDealId() {
        return DealId;
    }

    public String getDealTitle() {
        return DealTitle;
    }

    public String getCouponExpiryDate() {
        return CouponExpiryDate;
    }

    public String getSignupClosingDate() {
        return SignupClosingDate;
    }

    public String getFolderName() {
        return FolderName;
    }

    public int getDealPrice() {
        return DealPrice;
    }

    public String getPostedOn() {
        return PostedOn;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getCustomerBillingAddress() {
        return CustomerBillingAddress;
    }

    public boolean getIsSoldOut() {
        return IsSoldOut;
    }

    public boolean isSoldOut() {
        return IsSoldOut;
    }

    public int getmShopcartID() {
        return mShopcartID;
    }

    @Override
    public String toString() {
        return "CustomerInfoOrderListModel{" +
                "CouponId=" + CouponId +
                ", CouponPrice=" + CouponPrice +
                ", CouponQtn=" + CouponQtn +
                ", IsDone=" + IsDone +
                ", BookingCode='" + BookingCode + '\'' +
                ", DealId=" + DealId +
                ", DealTitle='" + DealTitle + '\'' +
                ", CouponExpiryDate='" + CouponExpiryDate + '\'' +
                ", SignupClosingDate='" + SignupClosingDate + '\'' +
                ", FolderName='" + FolderName + '\'' +
                ", DealPrice=" + DealPrice +
                ", PostedOn='" + PostedOn + '\'' +
                ", DeliveryDate='" + DeliveryDate + '\'' +
                ", CompanyName='" + CompanyName + '\'' +
                ", CustomerBillingAddress='" + CustomerBillingAddress + '\'' +
                ", IsSoldOut=" + IsSoldOut +
                ", mShopcartID=" + mShopcartID +
                '}';
    }
}
