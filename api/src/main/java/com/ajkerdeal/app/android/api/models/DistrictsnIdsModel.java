package com.ajkerdeal.app.android.api.models;

/**
 * Created by mitu on 5/31/16.
 */
public class DistrictsnIdsModel {

    private int DistrictId;
    private String District;
    private String DistrictBng;
    private int DeliveryCharge;
    private byte IsAdvPaymentActive;

    public DistrictsnIdsModel(int districtId, String district, String districtBng,
                              int deliveryCharge, Byte isAdvPaymentActive) {
        DistrictId = districtId;
        District = district;
        DistrictBng = districtBng;
        DeliveryCharge = deliveryCharge;
        IsAdvPaymentActive = isAdvPaymentActive;
    }

    public DistrictsnIdsModel(int districtId,String districtBng)
    {
        DistrictId = districtId;
        DistrictBng = districtBng;

    }
    public int getDistrictId() {
        return DistrictId;
    }

    public String getDistrict() {
        return District;
    }

    public String getDistrictBng() {
        return DistrictBng;
    }

    public int getDeliveryCharge() {
        return DeliveryCharge;
    }

    public byte getIsAdvPaymentActive() {
        return IsAdvPaymentActive;
    }

    @Override
    public String toString() {
        return "DistrictsnIdsModel{" +
                "DistrictId=" + DistrictId +
                ", District='" + District + '\'' +
                ", DistrictBng='" + DistrictBng + '\'' +
                ", DeliveryCharge=" + DeliveryCharge +
                ", IsAdvPaymentActive=" + IsAdvPaymentActive +
                '}';
    }
}
