package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingDatum {

    @SerializedName("adult_qty")
    private String mAdultQty;
    @SerializedName("api_requested")
    private Object mApiRequested;
    @SerializedName("availablity")
    private String mAvailablity;
    @SerializedName("balance_deduction_flag")
    private Object mBalanceDeductionFlag;
    @SerializedName("balance_return_flag")
    private Object mBalanceReturnFlag;
    @SerializedName("base_fare_total")
    private String mBaseFareTotal;
    @SerializedName("biller_commission")
    private String mBillerCommission;
    @SerializedName("booking_id")
    private String mBookingId;
    @SerializedName("booking_type")
    private Object mBookingType;
    @SerializedName("child_qty")
    private String mChildQty;
    @SerializedName("convenience_fee_total")
    private String mConvenienceFeeTotal;
    @SerializedName("currency")
    private String mCurrency;
    @SerializedName("cw_commission")
    private String mCwCommission;
    @SerializedName("dealler_commission")
    private String mDeallerCommission;
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
    @SerializedName("invoice_url")
    private Object mInvoiceUrl;
    @SerializedName("journey_type")
    private Object mJourneyType;
    @SerializedName("last_request_date_time")
    private Object mLastRequestDateTime;
    @SerializedName("last_ticket_date")
    private String mLastTicketDate;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("other_charges_total")
    private String mOtherChargesTotal;
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
    @SerializedName("service_fee_total")
    private String mServiceFeeTotal;
    @SerializedName("status_code")
    private String mStatusCode;
    @SerializedName("tax_total")
    private String mTaxTotal;
    @SerializedName("ticket_date_time")
    private Object mTicketDateTime;
    @SerializedName("total_fare")
    private String mTotalFare;
    @SerializedName("total_fare_calculated")
    private String mTotalFareCalculated;
    @SerializedName("trip_type")
    private String mTripType;
    @SerializedName("validating_career")
    private String mValidatingCareer;

    public String getAdultQty() {
        return mAdultQty;
    }

    public void setAdultQty(String adultQty) {
        mAdultQty = adultQty;
    }

    public Object getApiRequested() {
        return mApiRequested;
    }

    public void setApiRequested(Object apiRequested) {
        mApiRequested = apiRequested;
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

    public String getBaseFareTotal() {
        return mBaseFareTotal;
    }

    public void setBaseFareTotal(String baseFareTotal) {
        mBaseFareTotal = baseFareTotal;
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

    public Object getBookingType() {
        return mBookingType;
    }

    public void setBookingType(Object bookingType) {
        mBookingType = bookingType;
    }

    public String getChildQty() {
        return mChildQty;
    }

    public void setChildQty(String childQty) {
        mChildQty = childQty;
    }

    public String getConvenienceFeeTotal() {
        return mConvenienceFeeTotal;
    }

    public void setConvenienceFeeTotal(String convenienceFeeTotal) {
        mConvenienceFeeTotal = convenienceFeeTotal;
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

    public String getDeallerCommission() {
        return mDeallerCommission;
    }

    public void setDeallerCommission(String deallerCommission) {
        mDeallerCommission = deallerCommission;
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

    public Object getInvoiceUrl() {
        return mInvoiceUrl;
    }

    public void setInvoiceUrl(Object invoiceUrl) {
        mInvoiceUrl = invoiceUrl;
    }

    public Object getJourneyType() {
        return mJourneyType;
    }

    public void setJourneyType(Object journeyType) {
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

    public String getOtherChargesTotal() {
        return mOtherChargesTotal;
    }

    public void setOtherChargesTotal(String otherChargesTotal) {
        mOtherChargesTotal = otherChargesTotal;
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

    public String getServiceFeeTotal() {
        return mServiceFeeTotal;
    }

    public void setServiceFeeTotal(String serviceFeeTotal) {
        mServiceFeeTotal = serviceFeeTotal;
    }

    public String getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(String statusCode) {
        mStatusCode = statusCode;
    }

    public String getTaxTotal() {
        return mTaxTotal;
    }

    public void setTaxTotal(String taxTotal) {
        mTaxTotal = taxTotal;
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

    public String getTotalFareCalculated() {
        return mTotalFareCalculated;
    }

    public void setTotalFareCalculated(String totalFareCalculated) {
        mTotalFareCalculated = totalFareCalculated;
    }

    public String getTripType() {
        return mTripType;
    }

    public void setTripType(String tripType) {
        mTripType = tripType;
    }

    public String getValidatingCareer() {
        return mValidatingCareer;
    }

    public void setValidatingCareer(String validatingCareer) {
        mValidatingCareer = validatingCareer;
    }

}
