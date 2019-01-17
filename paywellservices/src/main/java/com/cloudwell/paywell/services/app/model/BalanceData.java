
package com.cloudwell.paywell.services.app.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class BalanceData {

    @SerializedName("balance")
    private String mBalance;
    @SerializedName("frozenBalance")
    private String mFrozenBalance;
    @SerializedName("otfBalance")
    private String mOtfBalance;

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        mBalance = balance;
    }

    public String getFrozenBalance() {
        return mFrozenBalance;
    }

    public void setFrozenBalance(String frozenBalance) {
        mFrozenBalance = frozenBalance;
    }

    public String getOtfBalance() {
        return mOtfBalance;
    }

    public void setOtfBalance(String otfBalance) {
        mOtfBalance = otfBalance;
    }

}
