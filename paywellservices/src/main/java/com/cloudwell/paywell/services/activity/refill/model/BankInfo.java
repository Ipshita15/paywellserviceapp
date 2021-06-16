package com.cloudwell.paywell.services.activity.refill.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BankInfo {

    @SerializedName("accountNumber")
    private String mAccountNumber;
    @SerializedName("name")
    private String mName;


    @SerializedName("branchName")
    private String branchName;


    @SerializedName("AccountName")
    private String accountName;

    public String getAccountNumber() {
        return mAccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        mAccountNumber = accountNumber;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
