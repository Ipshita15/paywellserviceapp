
package com.cloudwell.paywell.services.activity.refill.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class DistrictData {

    @SerializedName("bankInfo")
    private BankInfo mBankInfo;
    @SerializedName("branch")
    private List<Branch> mBranch;
    @SerializedName("currentDistrict")
    private CurrentDistrict mCurrentDistrict;
    @SerializedName("districtData")
    private List<DistrictDatum> mDistrictData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;

    public BankInfo getBankInfo() {
        return mBankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        mBankInfo = bankInfo;
    }

    public List<Branch> getBranch() {
        return mBranch;
    }

    public void setBranch(List<Branch> branch) {
        mBranch = branch;
    }

    public CurrentDistrict getCurrentDistrict() {
        return mCurrentDistrict;
    }

    public void setCurrentDistrict(CurrentDistrict currentDistrict) {
        mCurrentDistrict = currentDistrict;
    }

    public List<DistrictDatum> getDistrictData() {
        return mDistrictData;
    }

    public void setDistrictData(List<DistrictDatum> districtData) {
        mDistrictData = districtData;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
