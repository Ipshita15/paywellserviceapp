
package com.cloudwell.paywell.services.activity.refill.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DistrictDatum {

    @SerializedName("district_name")
    private String mDistrictName;
    @SerializedName("id")
    private String mId;

    public String getDistrictName() {
        return mDistrictName;
    }

    public void setDistrictName(String districtName) {
        mDistrictName = districtName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

}
