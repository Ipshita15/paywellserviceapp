
package com.cloudwell.paywell.services.activity.topup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class TopupReposeData {

    @SerializedName("hotlineNumber")
    private String mHotlineNumber;
    @SerializedName("topupData")
    private List<TopupDatum> mTopupData;

    public String getHotlineNumber() {
        return mHotlineNumber;
    }

    public void setHotlineNumber(String hotlineNumber) {
        mHotlineNumber = hotlineNumber;
    }

    public List<TopupDatum> getTopupData() {
        return mTopupData;
    }

    public void setTopupData(List<TopupDatum> topupData) {
        mTopupData = topupData;
    }

}
