
package com.cloudwell.paywell.services.app.model;

import com.google.gson.annotations.SerializedName;


public class APIResposeGenerateToken {

    @SerializedName("ack_timestamp")
    private String mAckTimestamp;
    @SerializedName("security_token")
    private String mSecurityToken;
    @SerializedName("token_exp_time")
    private String mTokenExpTime;
    @SerializedName("token_type")
    private String mTokenType;

    public String getAckTimestamp() {
        return mAckTimestamp;
    }

    public void setAckTimestamp(String ackTimestamp) {
        mAckTimestamp = ackTimestamp;
    }

    public String getSecurityToken() {
        return mSecurityToken;
    }

    public void setSecurityToken(String securityToken) {
        mSecurityToken = securityToken;
    }

    public String getTokenExpTime() {
        return mTokenExpTime;
    }

    public void setTokenExpTime(String tokenExpTime) {
        mTokenExpTime = tokenExpTime;
    }

    public String getTokenType() {
        return mTokenType;
    }

    public void setTokenType(String tokenType) {
        mTokenType = tokenType;
    }


}
