
package com.cloudwell.paywell.services.activity.refill.model;

import com.cloudwell.paywell.services.activity.refill.banktransfer.model.Branch;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class BranchData {

    @SerializedName("Branch")
    private List<Branch> mBranch;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;

    public List<Branch> getBranch() {
        return mBranch;
    }

    public void setBranch(List<Branch> branch) {
        mBranch = branch;
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
