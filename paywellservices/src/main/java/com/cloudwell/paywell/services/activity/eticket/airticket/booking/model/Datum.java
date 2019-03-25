
package com.cloudwell.paywell.services.activity.eticket.airticket.booking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Datum {

    @SerializedName("adult_qty")
    private String mAdultQty;
    @SerializedName("availablity")
    private String mAvailablity;
    @SerializedName("balance_deduction_flag")
    private Object mBalanceDeductionFlag;
    @SerializedName("balance_return_flag")
    private Object mBalanceReturnFlag;
    @SerializedName("biller_commission")
    private String mBillerCommission;
    @SerializedName("booking_id")
    private String mBookingId;
    @SerializedName("child_qty")
    private String mChildQty;
    @SerializedName("currency")
    private String mCurrency;
    @SerializedName("cw_commission")
    private String mCwCommission;
    @SerializedName("dealler_id")
    private String mDeallerId;
    @SerializedName("discount")
    private String mDiscount;
    @SerializedName("distributor_commission")
    private String mDistributorCommission;
    @SerializedName("distributor_id")
    private String mDistributorId;
    @SerializedName("fare_type")
    private String mFareType;
    @SerializedName("first_request_date_time")
    private String mFirstRequestDateTime;
    @SerializedName("id")
    private String mId;
    @SerializedName("infant_qty")
    private String mInfantQty;
    @SerializedName("journey_type")
    private String mJourneyType;
    @SerializedName("last_request_date_time")
    private Object mLastRequestDateTime;
    @SerializedName("last_ticket_date")
    private String mLastTicketDate;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("passengers")
    private List<Passenger> mPassengers;
    @SerializedName("result_id")
    private String mResultId;
    @SerializedName("retailer_commission")
    private String mRetailerCommission;
    @SerializedName("retailer_id")
    private String mRetailerId;
    @SerializedName("search_id")
    private String mSearchId;
    @SerializedName("status_code")
    private String mStatusCode;
    @SerializedName("ticket_date_time")
    private Object mTicketDateTime;
    @SerializedName("total_fare")
    private String mTotalFare;
    @SerializedName("validating_career")
    private String mValidatingCareer;

    @SerializedName("invoice_url")
    private String invoiceUrl;

    public String getAdultQty() {
        return mAdultQty;
    }

    public void setAdultQty(String adultQty) {
        mAdultQty = adultQty;
    }

    public String getAvailablity() {
        return mAvailablity;
    }

    public void setAvailablity(String availablity) {
        mAvailablity = availablity;
    }

    public Object getBalanceDeductionFlag() {
        return mBalanceDeductionFlag;
    }

    public void setBalanceDeductionFlag(Object balanceDeductionFlag) {
        mBalanceDeductionFlag = balanceDeductionFlag;
    }

    public Object getBalanceReturnFlag() {
        return mBalanceReturnFlag;
    }

    public void setBalanceReturnFlag(Object balanceReturnFlag) {
        mBalanceReturnFlag = balanceReturnFlag;
    }

    public String getBillerCommission() {
        return mBillerCommission;
    }

    public void setBillerCommission(String billerCommission) {
        mBillerCommission = billerCommission;
    }

    public String getBookingId() {
        return mBookingId;
    }

    public void setBookingId(String bookingId) {
        mBookingId = bookingId;
    }

    public String getChildQty() {
        return mChildQty;
    }

    public void setChildQty(String childQty) {
        mChildQty = childQty;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public String getCwCommission() {
        return mCwCommission;
    }

    public void setCwCommission(String cwCommission) {
        mCwCommission = cwCommission;
    }

    public String getDeallerId() {
        return mDeallerId;
    }

    public void setDeallerId(String deallerId) {
        mDeallerId = deallerId;
    }

    public String getDiscount() {
        return mDiscount;
    }

    public void setDiscount(String discount) {
        mDiscount = discount;
    }

    public String getDistributorCommission() {
        return mDistributorCommission;
    }

    public void setDistributorCommission(String distributorCommission) {
        mDistributorCommission = distributorCommission;
    }

    public String getDistributorId() {
        return mDistributorId;
    }

    public void setDistributorId(String distributorId) {
        mDistributorId = distributorId;
    }

    public String getFareType() {
        return mFareType;
    }

    public void setFareType(String fareType) {
        mFareType = fareType;
    }

    public String getFirstRequestDateTime() {
        return mFirstRequestDateTime;
    }

    public void setFirstRequestDateTime(String firstRequestDateTime) {
        mFirstRequestDateTime = firstRequestDateTime;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getInfantQty() {
        return mInfantQty;
    }

    public void setInfantQty(String infantQty) {
        mInfantQty = infantQty;
    }

    public String getJourneyType() {
        return mJourneyType;
    }

    public void setJourneyType(String journeyType) {
        mJourneyType = journeyType;
    }

    public Object getLastRequestDateTime() {
        return mLastRequestDateTime;
    }

    public void setLastRequestDateTime(Object lastRequestDateTime) {
        mLastRequestDateTime = lastRequestDateTime;
    }

    public String getLastTicketDate() {
        return mLastTicketDate;
    }

    public void setLastTicketDate(String lastTicketDate) {
        mLastTicketDate = lastTicketDate;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Passenger> getPassengers() {
        return mPassengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        mPassengers = passengers;
    }

    public String getResultId() {
        return mResultId;
    }

    public void setResultId(String resultId) {
        mResultId = resultId;
    }

    public String getRetailerCommission() {
        return mRetailerCommission;
    }

    public void setRetailerCommission(String retailerCommission) {
        mRetailerCommission = retailerCommission;
    }

    public String getRetailerId() {
        return mRetailerId;
    }

    public void setRetailerId(String retailerId) {
        mRetailerId = retailerId;
    }

    public String getSearchId() {
        return mSearchId;
    }

    public void setSearchId(String searchId) {
        mSearchId = searchId;
    }

    public String getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(String statusCode) {
        mStatusCode = statusCode;
    }

    public Object getTicketDateTime() {
        return mTicketDateTime;
    }

    public void setTicketDateTime(Object ticketDateTime) {
        mTicketDateTime = ticketDateTime;
    }

    public String getTotalFare() {
        return mTotalFare;
    }

    public void setTotalFare(String totalFare) {
        mTotalFare = totalFare;
    }

    public String getValidatingCareer() {
        return mValidatingCareer;
    }

    public void setValidatingCareer(String validatingCareer) {
        mValidatingCareer = validatingCareer;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }
}
