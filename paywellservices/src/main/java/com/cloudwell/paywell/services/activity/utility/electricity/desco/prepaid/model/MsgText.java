
package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MsgText implements Serializable {

    @SerializedName("bill_customer_phone")
    private String billCustomerPhone;
    @SerializedName("bill_number")
    private String billNumber;
    @SerializedName("mfs_id")
    private String mfsId;
    @SerializedName("request_datetime")
    private String requestDatetime;
    @SerializedName("response_msg")
    private String responseMsg;
    @SerializedName("response_msg2")
    private Object responseMsg2;
    @SerializedName("service_type")
    private String serviceType;
    @SerializedName("status_name_bill_pay")
    private Object statusNameBillPay;
    @SerializedName("status_name_enquiry")
    private String statusNameEnquiry;
    @SerializedName("total_amount")
    private String totalAmount;
    @SerializedName("transaction_status1")
    private String transactionStatus1;
    @SerializedName("transaction_status2")
    private Object transactionStatus2;
    @SerializedName("trx_id_1")
    private String trxId1;
    @SerializedName("trx_id_2")
    private Object trxId2;

    public String getBillCustomerPhone() {
        return billCustomerPhone;
    }

    public void setBillCustomerPhone(String billCustomerPhone) {
        this.billCustomerPhone = billCustomerPhone;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getMfsId() {
        return mfsId;
    }

    public void setMfsId(String mfsId) {
        this.mfsId = mfsId;
    }

    public String getRequestDatetime() {
        return requestDatetime;
    }

    public void setRequestDatetime(String requestDatetime) {
        this.requestDatetime = requestDatetime;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Object getResponseMsg2() {
        return responseMsg2;
    }

    public void setResponseMsg2(Object responseMsg2) {
        this.responseMsg2 = responseMsg2;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Object getStatusNameBillPay() {
        return statusNameBillPay;
    }

    public void setStatusNameBillPay(Object statusNameBillPay) {
        this.statusNameBillPay = statusNameBillPay;
    }

    public String getStatusNameEnquiry() {
        return statusNameEnquiry;
    }

    public void setStatusNameEnquiry(String statusNameEnquiry) {
        this.statusNameEnquiry = statusNameEnquiry;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionStatus1() {
        return transactionStatus1;
    }

    public void setTransactionStatus1(String transactionStatus1) {
        this.transactionStatus1 = transactionStatus1;
    }

    public Object getTransactionStatus2() {
        return transactionStatus2;
    }

    public void setTransactionStatus2(Object transactionStatus2) {
        this.transactionStatus2 = transactionStatus2;
    }

    public String getTrxId1() {
        return trxId1;
    }

    public void setTrxId1(String trxId1) {
        this.trxId1 = trxId1;
    }

    public Object getTrxId2() {
        return trxId2;
    }

    public void setTrxId2(Object trxId2) {
        this.trxId2 = trxId2;
    }

}
