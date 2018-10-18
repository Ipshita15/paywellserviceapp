package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 4/16/17.
 */

public class CartOrderRequestBody {
    @SerializedName("DealId")
    private int  mDealId;
    @SerializedName("CustomerId")
    private int mCustomerId;
    @SerializedName("CouponQtn")
    private int mCouponQtn;
    @SerializedName("PaymentType")
    private String mPaymentType;
    @SerializedName("CustomerMobile")
    private String mCustomerMobile;
    @SerializedName("CustomerAlternateMobile")
    private String mAlterMobile;
    @SerializedName("CustomerBillingAddress")
    private String mBillingAddress;

    @SerializedName("Sizes")
    private String mSizes;
    @SerializedName("Colors")
    private String mColor;

    @SerializedName("DeliveryDist")
    private int mDistrictId;
    @SerializedName("CardType")
    private String mCardType;
    @SerializedName("OrderFrom")
    private String mOrderFrom;
    @SerializedName("MerchantId")
    private int mMerchantId;
    @SerializedName("OrderSource")
    private String mOrderSource;
    @SerializedName("AdvPaymentAmount")
    private int mAdvPaymentType;
    @SerializedName("AdvPaymentPhoneId")
    private int mAdvPayPhoneId;

    public CartOrderRequestBody() {
    }

    public CartOrderRequestBody(int mDealId, int mCustomerId, int mCouponQtn, String mPaymentType, String mCustomerMobile, String mAlterMobile, String mBillingAddress, String mSizes, String mColor, int mDistrictId, String mCardType, String mOrderFrom, int mMerchantId, String mOrderSource, int mAdvPaymentType, int mAdvPayPhoneId) {
        this.mDealId = mDealId;
        this.mCustomerId = mCustomerId;
        this.mCouponQtn = mCouponQtn;
        this.mPaymentType = mPaymentType;
        this.mCustomerMobile = mCustomerMobile;
        this.mAlterMobile = mAlterMobile;
        this.mBillingAddress = mBillingAddress;
        this.mSizes = mSizes;
        this.mColor = mColor;
        this.mDistrictId = mDistrictId;
        this.mCardType = mCardType;
        this.mOrderFrom = mOrderFrom;
        this.mMerchantId = mMerchantId;
        this.mOrderSource = mOrderSource;
        this.mAdvPaymentType = mAdvPaymentType;
        this.mAdvPayPhoneId = mAdvPayPhoneId;
    }

    public int getmDealId() {
        return mDealId;
    }

    public void setmDealId(int mDealId) {
        this.mDealId = mDealId;
    }

    public int getmCustomerId() {
        return mCustomerId;
    }

    public void setmCustomerId(int mCustomerId) {
        this.mCustomerId = mCustomerId;
    }

    public int getmCouponQtn() {
        return mCouponQtn;
    }

    public void setmCouponQtn(int mCouponQtn) {
        this.mCouponQtn = mCouponQtn;
    }

    public String getmPaymentType() {
        return mPaymentType;
    }

    public void setmPaymentType(String mPaymentType) {
        this.mPaymentType = mPaymentType;
    }

    public String getmCustomerMobile() {
        return mCustomerMobile;
    }

    public void setmCustomerMobile(String mCustomerMobile) {
        this.mCustomerMobile = mCustomerMobile;
    }

    public String getmAlterMobile() {
        return mAlterMobile;
    }

    public void setmAlterMobile(String mAlterMobile) {
        this.mAlterMobile = mAlterMobile;
    }

    public String getmBillingAddress() {
        return mBillingAddress;
    }

    public void setmBillingAddress(String mBillingAddress) {
        this.mBillingAddress = mBillingAddress;
    }

    public String getmSizes() {
        return mSizes;
    }

    public void setmSizes(String mSizes) {
        this.mSizes = mSizes;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public int getmDistrictId() {
        return mDistrictId;
    }

    public void setmDistrictId(int mDistrictId) {
        this.mDistrictId = mDistrictId;
    }

    public String getmCardType() {
        return mCardType;
    }

    public void setmCardType(String mCardType) {
        this.mCardType = mCardType;
    }

    public String getmOrderFrom() {
        return mOrderFrom;
    }

    public void setmOrderFrom(String mOrderFrom) {
        this.mOrderFrom = mOrderFrom;
    }

    public int getmMerchantId() {
        return mMerchantId;
    }

    public void setmMerchantId(int mMerchantId) {
        this.mMerchantId = mMerchantId;
    }

    public String getmOrderSource() {
        return mOrderSource;
    }

    public void setmOrderSource(String mOrderSource) {
        this.mOrderSource = mOrderSource;
    }

    public int getmAdvPaymentType() {
        return mAdvPaymentType;
    }

    public void setmAdvPaymentType(int mAdvPaymentType) {
        this.mAdvPaymentType = mAdvPaymentType;
    }

    public int getmAdvPayPhoneId() {
        return mAdvPayPhoneId;
    }

    public void setmAdvPayPhoneId(int mAdvPayPhoneId) {
        this.mAdvPayPhoneId = mAdvPayPhoneId;
    }

    @Override
    public String toString() {
        return "CartOrderRequestBody{" +
                "mDealId=" + mDealId +
                ", mCustomerId=" + mCustomerId +
                ", mCouponQtn=" + mCouponQtn +
                ", mPaymentType='" + mPaymentType + '\'' +
                ", mCustomerMobile='" + mCustomerMobile + '\'' +
                ", mAlterMobile=" + mAlterMobile +
                ", mBillingAddress='" + mBillingAddress + '\'' +
                ", mSizes='" + mSizes + '\'' +
                ", mColor='" + mColor + '\'' +
                ", mDistrictId=" + mDistrictId +
                ", mCardType='" + mCardType + '\'' +
                ", mOrderFrom='" + mOrderFrom + '\'' +
                ", mMerchantId=" + mMerchantId +
                ", mOrderSource='" + mOrderSource + '\'' +
                ", mAdvPaymentType=" + mAdvPaymentType +
                ", mAdvPayPhoneId=" + mAdvPayPhoneId +
                '}';
    }
}
