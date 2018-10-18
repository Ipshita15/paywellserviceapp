package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 5/31/16.
 */
public class SingleOrderModel {
    @SerializedName("DealId")
    private int DealId;
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("CouponQtn")
    private int CouponQtn;
    @SerializedName("PaymentType")
    private String PaymentType;
    @SerializedName("CustomerMobile")
    private String CustomerMobile;
    @SerializedName("CustomerAlternateMobile")
    private String CustomerAlternateMobile;
    @SerializedName("CustomerBillingAddress")
    private String CustomerBillingAddress;
    @SerializedName("Sizes")
    private String Sizes;
    @SerializedName("Colors")
    private String Colors;
    @SerializedName("DeliveryDist")
    private int DeliveryDist;
    @SerializedName("CardType")
    private String CardType;
    @SerializedName("OrderFrom")
    private String OrderFrom;


    public SingleOrderModel(int dealId, int customerId, int couponQtn, String paymentType, String customerMobile, String customerAlternateMobile,
                            String customerBillingAddress, String sizes,
                            String colors, int deliveryDist, String cardType, String orderFrom) {
        DealId = dealId;
        CustomerId = customerId;
        CouponQtn = couponQtn;
        PaymentType = paymentType;
        CustomerMobile = customerMobile;
        CustomerAlternateMobile = customerAlternateMobile;
        CustomerBillingAddress = customerBillingAddress;
        Sizes = sizes;
        Colors = colors;
        DeliveryDist = deliveryDist;
        CardType = cardType;
        OrderFrom = orderFrom;
    }

    public int getDealId() {
        return DealId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public int getCouponQtn() {
        return CouponQtn;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public String getCustomerMobile() {
        return CustomerMobile;
    }

    public String getCustomerAlternateMobile() {
        return CustomerAlternateMobile;
    }

    public String getCustomerBillingAddress() {
        return CustomerBillingAddress;
    }

    public String getSizes() {
        return Sizes;
    }

    public String getColors() {
        return Colors;
    }

    public int getDeliveryDist() {
        return DeliveryDist;
    }

    public String getCardType() {
        return CardType;
    }

    public String getOrderFrom() {
        return OrderFrom;
    }

    @Override
    public String toString() {
        return "SingleOrderModel{" +
                "DealId=" + DealId +
                ", CustomerId=" + CustomerId +
                ", CouponQtn=" + CouponQtn +
                ", PaymentType='" + PaymentType + '\'' +
                ", CustomerMobile='" + CustomerMobile + '\'' +
                ", CustomerAlternateMobile='" + CustomerAlternateMobile + '\'' +
                ", CustomerBillingAddress='" + CustomerBillingAddress + '\'' +
                ", Sizes='" + Sizes + '\'' +
                ", Colors='" + Colors + '\'' +
                ", DeliveryDist=" + DeliveryDist +
                ", CardType='" + CardType + '\'' +
                ", OrderFrom='" + OrderFrom + '\'' +
                '}';
    }
}
