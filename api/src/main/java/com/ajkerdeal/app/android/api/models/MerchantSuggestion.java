package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/10/17.
 */

public class MerchantSuggestion {
    @SerializedName("Id")

    public int id;
    @SerializedName("ProfileId")

    public int profileId;
    @SerializedName("CompanyName")

    public String companyName;
    @SerializedName("CompanyNameBng")

    public String companyNameBng;
    @SerializedName("LogInEmail")

    public String logInEmail;
    @SerializedName("Mobile")

    public String mobile;
    @SerializedName("FulFillmentRating")

    public int fulFillmentRating;
    @SerializedName("DeliverySpeedInsideDhaka")

    public int deliverySpeedInsideDhaka;
    @SerializedName("DeliverySpeedOutsideDhaka")

    public int deliverySpeedOutsideDhaka;
    @SerializedName("MerchantToken")

    public String merchantToken;
    @SerializedName("RoutingName")

    public String routingName;
    @SerializedName("InsertedOn")

    public String insertedOn;
    @SerializedName("AwardType")

    public int awardType;
    @SerializedName("AwardValue")

    public int awardValue;
    @SerializedName("AwardName")

    public String awardName;
    @SerializedName("TotalScore")

    public int totalScore;
    @SerializedName("DeliverySpeed")

    public int deliverySpeed;
    @SerializedName("DeliverySpeedDhakaOne")

    public int deliverySpeedDhakaOne;
    @SerializedName("DeliverySpeedDhakaTwo")

    public int deliverySpeedDhakaTwo;
    @SerializedName("DeliverySpeedDhakaThree")

    public int deliverySpeedDhakaThree;
    @SerializedName("DeliverySpeedDhakaFour")

    public int deliverySpeedDhakaFour;
    @SerializedName("DeliverySpeedDhakaFive")

    public int deliverySpeedDhakaFive;
    @SerializedName("DeliverySpeedDhakaSix")

    public int deliverySpeedDhakaSix;
    @SerializedName("DeliverySpeedDhakaSeven")

    public int deliverySpeedDhakaSeven;
    @SerializedName("DeliverySpeedOutDhakaOne")

    public int deliverySpeedOutDhakaOne;
    @SerializedName("DeliverySpeedOutDhakaTwo")

    public int deliverySpeedOutDhakaTwo;
    @SerializedName("DeliverySpeedOutDhakaThree")

    public int deliverySpeedOutDhakaThree;
    @SerializedName("DeliverySpeedOutDhakaFour")

    public int deliverySpeedOutDhakaFour;
    @SerializedName("DeliverySpeedOutDhakaFive")

    public int deliverySpeedOutDhakaFive;
    @SerializedName("DeliverySpeedOutDhakaSix")

    public int deliverySpeedOutDhakaSix;
    @SerializedName("DeliverySpeedOutDhakaSeven")

    public int deliverySpeedOutDhakaSeven;
    @SerializedName("TotalProduct")

    public int totalProduct;
    @SerializedName("PositiveReview")

    public int positiveReview;
    @SerializedName("NegativeReview")

    public int negativeReview;
    @SerializedName("MerchantTotalOrder")

    public int merchantTotalOrder;
    @SerializedName("CustomerTotalReview")

    public int customerTotalReview;
    @SerializedName("InDhaka")

    public String inDhaka;
    @SerializedName("OutDhaka")

    public String outDhaka;

    public MerchantSuggestion(int id, int profileId, String companyName, String companyNameBng, String logInEmail, String mobile, int fulFillmentRating, int deliverySpeedInsideDhaka, int deliverySpeedOutsideDhaka, String merchantToken, String routingName, String insertedOn, int awardType, int awardValue, String awardName, int totalScore, int deliverySpeed, int deliverySpeedDhakaOne, int deliverySpeedDhakaTwo, int deliverySpeedDhakaThree, int deliverySpeedDhakaFour, int deliverySpeedDhakaFive, int deliverySpeedDhakaSix, int deliverySpeedDhakaSeven, int deliverySpeedOutDhakaOne, int deliverySpeedOutDhakaTwo, int deliverySpeedOutDhakaThree, int deliverySpeedOutDhakaFour, int deliverySpeedOutDhakaFive, int deliverySpeedOutDhakaSix, int deliverySpeedOutDhakaSeven, int totalProduct, int positiveReview, int negativeReview, int merchantTotalOrder, int customerTotalReview, String inDhaka, String outDhaka) {
        this.id = id;
        this.profileId = profileId;
        this.companyName = companyName;
        this.companyNameBng = companyNameBng;
        this.logInEmail = logInEmail;
        this.mobile = mobile;
        this.fulFillmentRating = fulFillmentRating;
        this.deliverySpeedInsideDhaka = deliverySpeedInsideDhaka;
        this.deliverySpeedOutsideDhaka = deliverySpeedOutsideDhaka;
        this.merchantToken = merchantToken;
        this.routingName = routingName;
        this.insertedOn = insertedOn;
        this.awardType = awardType;
        this.awardValue = awardValue;
        this.awardName = awardName;
        this.totalScore = totalScore;
        this.deliverySpeed = deliverySpeed;
        this.deliverySpeedDhakaOne = deliverySpeedDhakaOne;
        this.deliverySpeedDhakaTwo = deliverySpeedDhakaTwo;
        this.deliverySpeedDhakaThree = deliverySpeedDhakaThree;
        this.deliverySpeedDhakaFour = deliverySpeedDhakaFour;
        this.deliverySpeedDhakaFive = deliverySpeedDhakaFive;
        this.deliverySpeedDhakaSix = deliverySpeedDhakaSix;
        this.deliverySpeedDhakaSeven = deliverySpeedDhakaSeven;
        this.deliverySpeedOutDhakaOne = deliverySpeedOutDhakaOne;
        this.deliverySpeedOutDhakaTwo = deliverySpeedOutDhakaTwo;
        this.deliverySpeedOutDhakaThree = deliverySpeedOutDhakaThree;
        this.deliverySpeedOutDhakaFour = deliverySpeedOutDhakaFour;
        this.deliverySpeedOutDhakaFive = deliverySpeedOutDhakaFive;
        this.deliverySpeedOutDhakaSix = deliverySpeedOutDhakaSix;
        this.deliverySpeedOutDhakaSeven = deliverySpeedOutDhakaSeven;
        this.totalProduct = totalProduct;
        this.positiveReview = positiveReview;
        this.negativeReview = negativeReview;
        this.merchantTotalOrder = merchantTotalOrder;
        this.customerTotalReview = customerTotalReview;
        this.inDhaka = inDhaka;
        this.outDhaka = outDhaka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNameBng() {
        return companyNameBng;
    }

    public void setCompanyNameBng(String companyNameBng) {
        this.companyNameBng = companyNameBng;
    }

    public String getLogInEmail() {
        return logInEmail;
    }

    public void setLogInEmail(String logInEmail) {
        this.logInEmail = logInEmail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getFulFillmentRating() {
        return fulFillmentRating;
    }

    public void setFulFillmentRating(int fulFillmentRating) {
        this.fulFillmentRating = fulFillmentRating;
    }

    public int getDeliverySpeedInsideDhaka() {
        return deliverySpeedInsideDhaka;
    }

    public void setDeliverySpeedInsideDhaka(int deliverySpeedInsideDhaka) {
        this.deliverySpeedInsideDhaka = deliverySpeedInsideDhaka;
    }

    public int getDeliverySpeedOutsideDhaka() {
        return deliverySpeedOutsideDhaka;
    }

    public void setDeliverySpeedOutsideDhaka(int deliverySpeedOutsideDhaka) {
        this.deliverySpeedOutsideDhaka = deliverySpeedOutsideDhaka;
    }



    public String getRoutingName() {
        return routingName;
    }

    public void setRoutingName(String routingName) {
        this.routingName = routingName;
    }



    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public int getAwardValue() {
        return awardValue;
    }

    public void setAwardValue(int awardValue) {
        this.awardValue = awardValue;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getDeliverySpeed() {
        return deliverySpeed;
    }

    public void setDeliverySpeed(int deliverySpeed) {
        this.deliverySpeed = deliverySpeed;
    }

    public int getDeliverySpeedDhakaOne() {
        return deliverySpeedDhakaOne;
    }

    public void setDeliverySpeedDhakaOne(int deliverySpeedDhakaOne) {
        this.deliverySpeedDhakaOne = deliverySpeedDhakaOne;
    }

    public int getDeliverySpeedDhakaTwo() {
        return deliverySpeedDhakaTwo;
    }

    public void setDeliverySpeedDhakaTwo(int deliverySpeedDhakaTwo) {
        this.deliverySpeedDhakaTwo = deliverySpeedDhakaTwo;
    }

    public int getDeliverySpeedDhakaThree() {
        return deliverySpeedDhakaThree;
    }

    public void setDeliverySpeedDhakaThree(int deliverySpeedDhakaThree) {
        this.deliverySpeedDhakaThree = deliverySpeedDhakaThree;
    }

    public int getDeliverySpeedDhakaFour() {
        return deliverySpeedDhakaFour;
    }

    public void setDeliverySpeedDhakaFour(int deliverySpeedDhakaFour) {
        this.deliverySpeedDhakaFour = deliverySpeedDhakaFour;
    }

    public int getDeliverySpeedDhakaFive() {
        return deliverySpeedDhakaFive;
    }

    public void setDeliverySpeedDhakaFive(int deliverySpeedDhakaFive) {
        this.deliverySpeedDhakaFive = deliverySpeedDhakaFive;
    }

    public int getDeliverySpeedDhakaSix() {
        return deliverySpeedDhakaSix;
    }

    public void setDeliverySpeedDhakaSix(int deliverySpeedDhakaSix) {
        this.deliverySpeedDhakaSix = deliverySpeedDhakaSix;
    }

    public int getDeliverySpeedDhakaSeven() {
        return deliverySpeedDhakaSeven;
    }

    public void setDeliverySpeedDhakaSeven(int deliverySpeedDhakaSeven) {
        this.deliverySpeedDhakaSeven = deliverySpeedDhakaSeven;
    }

    public int getDeliverySpeedOutDhakaOne() {
        return deliverySpeedOutDhakaOne;
    }

    public void setDeliverySpeedOutDhakaOne(int deliverySpeedOutDhakaOne) {
        this.deliverySpeedOutDhakaOne = deliverySpeedOutDhakaOne;
    }

    public int getDeliverySpeedOutDhakaTwo() {
        return deliverySpeedOutDhakaTwo;
    }

    public void setDeliverySpeedOutDhakaTwo(int deliverySpeedOutDhakaTwo) {
        this.deliverySpeedOutDhakaTwo = deliverySpeedOutDhakaTwo;
    }

    public int getDeliverySpeedOutDhakaThree() {
        return deliverySpeedOutDhakaThree;
    }

    public void setDeliverySpeedOutDhakaThree(int deliverySpeedOutDhakaThree) {
        this.deliverySpeedOutDhakaThree = deliverySpeedOutDhakaThree;
    }

    public int getDeliverySpeedOutDhakaFour() {
        return deliverySpeedOutDhakaFour;
    }

    public void setDeliverySpeedOutDhakaFour(int deliverySpeedOutDhakaFour) {
        this.deliverySpeedOutDhakaFour = deliverySpeedOutDhakaFour;
    }

    public int getDeliverySpeedOutDhakaFive() {
        return deliverySpeedOutDhakaFive;
    }

    public void setDeliverySpeedOutDhakaFive(int deliverySpeedOutDhakaFive) {
        this.deliverySpeedOutDhakaFive = deliverySpeedOutDhakaFive;
    }

    public int getDeliverySpeedOutDhakaSix() {
        return deliverySpeedOutDhakaSix;
    }

    public void setDeliverySpeedOutDhakaSix(int deliverySpeedOutDhakaSix) {
        this.deliverySpeedOutDhakaSix = deliverySpeedOutDhakaSix;
    }

    public int getDeliverySpeedOutDhakaSeven() {
        return deliverySpeedOutDhakaSeven;
    }

    public void setDeliverySpeedOutDhakaSeven(int deliverySpeedOutDhakaSeven) {
        this.deliverySpeedOutDhakaSeven = deliverySpeedOutDhakaSeven;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
    }

    public int getPositiveReview() {
        return positiveReview;
    }

    public void setPositiveReview(int positiveReview) {
        this.positiveReview = positiveReview;
    }

    public int getNegativeReview() {
        return negativeReview;
    }

    public void setNegativeReview(int negativeReview) {
        this.negativeReview = negativeReview;
    }

    public int getMerchantTotalOrder() {
        return merchantTotalOrder;
    }

    public void setMerchantTotalOrder(int merchantTotalOrder) {
        this.merchantTotalOrder = merchantTotalOrder;
    }

    public int getCustomerTotalReview() {
        return customerTotalReview;
    }

    public void setCustomerTotalReview(int customerTotalReview) {
        this.customerTotalReview = customerTotalReview;
    }

    public String getInDhaka() {
        return inDhaka;
    }

    public void setInDhaka(String inDhaka) {
        this.inDhaka = inDhaka;
    }

    public String getOutDhaka() {
        return outDhaka;
    }

    public void setOutDhaka(String outDhaka) {
        this.outDhaka = outDhaka;
    }

    public String getMerchantToken() {
        return merchantToken;
    }

    public void setMerchantToken(String merchantToken) {
        this.merchantToken = merchantToken;
    }

    public String getInsertedOn() {
        return insertedOn;
    }

    public void setInsertedOn(String insertedOn) {
        this.insertedOn = insertedOn;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    @Override
    public String toString() {
        return "MerchantSuggestion{" +
                "id=" + id +
                ", profileId=" + profileId +
                ", companyName='" + companyName + '\'' +
                ", companyNameBng='" + companyNameBng + '\'' +
                ", logInEmail='" + logInEmail + '\'' +
                ", mobile='" + mobile + '\'' +
                ", fulFillmentRating=" + fulFillmentRating +
                ", deliverySpeedInsideDhaka=" + deliverySpeedInsideDhaka +
                ", deliverySpeedOutsideDhaka=" + deliverySpeedOutsideDhaka +
                ", merchantToken=" + merchantToken +
                ", routingName='" + routingName + '\'' +
                ", insertedOn=" + insertedOn +
                ", awardType=" + awardType +
                ", awardValue=" + awardValue +
                ", awardName=" + awardName +
                ", totalScore=" + totalScore +
                ", deliverySpeed=" + deliverySpeed +
                ", deliverySpeedDhakaOne=" + deliverySpeedDhakaOne +
                ", deliverySpeedDhakaTwo=" + deliverySpeedDhakaTwo +
                ", deliverySpeedDhakaThree=" + deliverySpeedDhakaThree +
                ", deliverySpeedDhakaFour=" + deliverySpeedDhakaFour +
                ", deliverySpeedDhakaFive=" + deliverySpeedDhakaFive +
                ", deliverySpeedDhakaSix=" + deliverySpeedDhakaSix +
                ", deliverySpeedDhakaSeven=" + deliverySpeedDhakaSeven +
                ", deliverySpeedOutDhakaOne=" + deliverySpeedOutDhakaOne +
                ", deliverySpeedOutDhakaTwo=" + deliverySpeedOutDhakaTwo +
                ", deliverySpeedOutDhakaThree=" + deliverySpeedOutDhakaThree +
                ", deliverySpeedOutDhakaFour=" + deliverySpeedOutDhakaFour +
                ", deliverySpeedOutDhakaFive=" + deliverySpeedOutDhakaFive +
                ", deliverySpeedOutDhakaSix=" + deliverySpeedOutDhakaSix +
                ", deliverySpeedOutDhakaSeven=" + deliverySpeedOutDhakaSeven +
                ", totalProduct=" + totalProduct +
                ", positiveReview=" + positiveReview +
                ", negativeReview=" + negativeReview +
                ", merchantTotalOrder=" + merchantTotalOrder +
                ", customerTotalReview=" + customerTotalReview +
                ", inDhaka='" + inDhaka + '\'' +
                ", outDhaka='" + outDhaka + '\'' +
                '}';
    }
}
