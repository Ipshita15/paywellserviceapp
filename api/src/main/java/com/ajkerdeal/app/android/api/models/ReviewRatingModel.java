package com.ajkerdeal.app.android.api.models;

/**
 * Created by mitu on 6/14/17.
 */

public class ReviewRatingModel {

    private int Id;
    private int ProfileId;
    private String CompanyName;
    private String CompanyNameBng;
    private String LogInEmail;
    private String Mobile;
    private int FulFillmentRating;
    private int DeliverySpeedInsideDhaka;
    private int DeliverySpeedOutsideDhaka;
    private String MerchantToken;
    private String RoutingName;
    private String InsertedOn;
    private int AwardType;
    private int AwardValue;
    private String AwardName;
    private int TotalScore;
    private int DeliverySpeed;

    private int DeliverySpeedDhakaOne;
    private int DeliverySpeedDhakaTwo;

    private int DeliverySpeedDhakaThree;
    private int DeliverySpeedDhakaFour;

    private int DeliverySpeedDhakaFive;
    private int DeliverySpeedDhakaSix;

    private int DeliverySpeedDhakaSeven;
    private int DeliverySpeedOutDhakaOne;

    private int DeliverySpeedOutDhakaTwo;
    private int DeliverySpeedOutDhakaThree;

    private int DeliverySpeedOutDhakaFour;
    private int DeliverySpeedOutDhakaFive;

    private int DeliverySpeedOutDhakaSix;
    private int DeliverySpeedOutDhakaSeven;

    private int TotalProduct;
    private int PositiveReview;

    private int NegativeReview;
    private int MerchantTotalOrder;

    private int CustomerTotalReview;
    private String InDhaka;
    private String OutDhaka;

    public ReviewRatingModel(int id, int profileId, String companyName, String companyNameBng, String logInEmail, String mobile, int fulFillmentRating, int deliverySpeedInsideDhaka, int deliverySpeedOutsideDhaka, String merchantToken, String routingName, String insertedOn, int awardType, int awardValue, String awardName, int totalScore, int deliverySpeed, int deliverySpeedDhakaOne, int deliverySpeedDhakaTwo, int deliverySpeedDhakaThree, int deliverySpeedDhakaFour, int deliverySpeedDhakaFive, int deliverySpeedDhakaSix, int deliverySpeedDhakaSeven, int deliverySpeedOutDhakaOne, int deliverySpeedOutDhakaTwo, int deliverySpeedOutDhakaThree, int deliverySpeedOutDhakaFour, int deliverySpeedOutDhakaFive, int deliverySpeedOutDhakaSix, int deliverySpeedOutDhakaSeven, int totalProduct, int positiveReview, int negativeReview, int merchantTotalOrder, int customerTotalReview, String inDhaka, String outDhaka) {
        Id = id;
        ProfileId = profileId;
        CompanyName = companyName;
        CompanyNameBng = companyNameBng;
        LogInEmail = logInEmail;
        Mobile = mobile;
        FulFillmentRating = fulFillmentRating;
        DeliverySpeedInsideDhaka = deliverySpeedInsideDhaka;
        DeliverySpeedOutsideDhaka = deliverySpeedOutsideDhaka;
        MerchantToken = merchantToken;
        RoutingName = routingName;
        InsertedOn = insertedOn;
        AwardType = awardType;
        AwardValue = awardValue;
        AwardName = awardName;
        TotalScore = totalScore;
        DeliverySpeed = deliverySpeed;
        DeliverySpeedDhakaOne = deliverySpeedDhakaOne;
        DeliverySpeedDhakaTwo = deliverySpeedDhakaTwo;
        DeliverySpeedDhakaThree = deliverySpeedDhakaThree;
        DeliverySpeedDhakaFour = deliverySpeedDhakaFour;
        DeliverySpeedDhakaFive = deliverySpeedDhakaFive;
        DeliverySpeedDhakaSix = deliverySpeedDhakaSix;
        DeliverySpeedDhakaSeven = deliverySpeedDhakaSeven;
        DeliverySpeedOutDhakaOne = deliverySpeedOutDhakaOne;
        DeliverySpeedOutDhakaTwo = deliverySpeedOutDhakaTwo;
        DeliverySpeedOutDhakaThree = deliverySpeedOutDhakaThree;
        DeliverySpeedOutDhakaFour = deliverySpeedOutDhakaFour;
        DeliverySpeedOutDhakaFive = deliverySpeedOutDhakaFive;
        DeliverySpeedOutDhakaSix = deliverySpeedOutDhakaSix;
        DeliverySpeedOutDhakaSeven = deliverySpeedOutDhakaSeven;
        TotalProduct = totalProduct;
        PositiveReview = positiveReview;
        NegativeReview = negativeReview;
        MerchantTotalOrder = merchantTotalOrder;
        CustomerTotalReview = customerTotalReview;
        InDhaka = inDhaka;
        OutDhaka = outDhaka;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyNameBng() {
        return CompanyNameBng;
    }

    public void setCompanyNameBng(String companyNameBng) {
        CompanyNameBng = companyNameBng;
    }

    public String getLogInEmail() {
        return LogInEmail;
    }

    public void setLogInEmail(String logInEmail) {
        LogInEmail = logInEmail;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getFulFillmentRating() {
        return FulFillmentRating;
    }

    public void setFulFillmentRating(int fulFillmentRating) {
        FulFillmentRating = fulFillmentRating;
    }

    public int getDeliverySpeedInsideDhaka() {
        return DeliverySpeedInsideDhaka;
    }

    public void setDeliverySpeedInsideDhaka(int deliverySpeedInsideDhaka) {
        DeliverySpeedInsideDhaka = deliverySpeedInsideDhaka;
    }

    public int getDeliverySpeedOutsideDhaka() {
        return DeliverySpeedOutsideDhaka;
    }

    public void setDeliverySpeedOutsideDhaka(int deliverySpeedOutsideDhaka) {
        DeliverySpeedOutsideDhaka = deliverySpeedOutsideDhaka;
    }

    public String getMerchantToken() {
        return MerchantToken;
    }

    public void setMerchantToken(String merchantToken) {
        MerchantToken = merchantToken;
    }

    public String getRoutingName() {
        return RoutingName;
    }

    public void setRoutingName(String routingName) {
        RoutingName = routingName;
    }

    public String getInsertedOn() {
        return InsertedOn;
    }

    public void setInsertedOn(String insertedOn) {
        InsertedOn = insertedOn;
    }

    public int getAwardType() {
        return AwardType;
    }

    public void setAwardType(int awardType) {
        AwardType = awardType;
    }

    public int getAwardValue() {
        return AwardValue;
    }

    public void setAwardValue(int awardValue) {
        AwardValue = awardValue;
    }

    public String getAwardName() {
        return AwardName;
    }

    public void setAwardName(String awardName) {
        AwardName = awardName;
    }

    public int getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(int totalScore) {
        TotalScore = totalScore;
    }

    public int getDeliverySpeed() {
        return DeliverySpeed;
    }

    public void setDeliverySpeed(int deliverySpeed) {
        DeliverySpeed = deliverySpeed;
    }

    public int getDeliverySpeedDhakaOne() {
        return DeliverySpeedDhakaOne;
    }

    public void setDeliverySpeedDhakaOne(int deliverySpeedDhakaOne) {
        DeliverySpeedDhakaOne = deliverySpeedDhakaOne;
    }

    public int getDeliverySpeedDhakaTwo() {
        return DeliverySpeedDhakaTwo;
    }

    public void setDeliverySpeedDhakaTwo(int deliverySpeedDhakaTwo) {
        DeliverySpeedDhakaTwo = deliverySpeedDhakaTwo;
    }

    public int getDeliverySpeedDhakaThree() {
        return DeliverySpeedDhakaThree;
    }

    public void setDeliverySpeedDhakaThree(int deliverySpeedDhakaThree) {
        DeliverySpeedDhakaThree = deliverySpeedDhakaThree;
    }

    public int getDeliverySpeedDhakaFour() {
        return DeliverySpeedDhakaFour;
    }

    public void setDeliverySpeedDhakaFour(int deliverySpeedDhakaFour) {
        DeliverySpeedDhakaFour = deliverySpeedDhakaFour;
    }

    public int getDeliverySpeedDhakaFive() {
        return DeliverySpeedDhakaFive;
    }

    public void setDeliverySpeedDhakaFive(int deliverySpeedDhakaFive) {
        DeliverySpeedDhakaFive = deliverySpeedDhakaFive;
    }

    public int getDeliverySpeedDhakaSix() {
        return DeliverySpeedDhakaSix;
    }

    public void setDeliverySpeedDhakaSix(int deliverySpeedDhakaSix) {
        DeliverySpeedDhakaSix = deliverySpeedDhakaSix;
    }

    public int getDeliverySpeedDhakaSeven() {
        return DeliverySpeedDhakaSeven;
    }

    public void setDeliverySpeedDhakaSeven(int deliverySpeedDhakaSeven) {
        DeliverySpeedDhakaSeven = deliverySpeedDhakaSeven;
    }

    public int getDeliverySpeedOutDhakaOne() {
        return DeliverySpeedOutDhakaOne;
    }

    public void setDeliverySpeedOutDhakaOne(int deliverySpeedOutDhakaOne) {
        DeliverySpeedOutDhakaOne = deliverySpeedOutDhakaOne;
    }

    public int getDeliverySpeedOutDhakaTwo() {
        return DeliverySpeedOutDhakaTwo;
    }

    public void setDeliverySpeedOutDhakaTwo(int deliverySpeedOutDhakaTwo) {
        DeliverySpeedOutDhakaTwo = deliverySpeedOutDhakaTwo;
    }

    public int getDeliverySpeedOutDhakaThree() {
        return DeliverySpeedOutDhakaThree;
    }

    public void setDeliverySpeedOutDhakaThree(int deliverySpeedOutDhakaThree) {
        DeliverySpeedOutDhakaThree = deliverySpeedOutDhakaThree;
    }

    public int getDeliverySpeedOutDhakaFour() {
        return DeliverySpeedOutDhakaFour;
    }

    public void setDeliverySpeedOutDhakaFour(int deliverySpeedOutDhakaFour) {
        DeliverySpeedOutDhakaFour = deliverySpeedOutDhakaFour;
    }

    public int getDeliverySpeedOutDhakaFive() {
        return DeliverySpeedOutDhakaFive;
    }

    public void setDeliverySpeedOutDhakaFive(int deliverySpeedOutDhakaFive) {
        DeliverySpeedOutDhakaFive = deliverySpeedOutDhakaFive;
    }

    public int getDeliverySpeedOutDhakaSix() {
        return DeliverySpeedOutDhakaSix;
    }

    public void setDeliverySpeedOutDhakaSix(int deliverySpeedOutDhakaSix) {
        DeliverySpeedOutDhakaSix = deliverySpeedOutDhakaSix;
    }

    public int getDeliverySpeedOutDhakaSeven() {
        return DeliverySpeedOutDhakaSeven;
    }

    public void setDeliverySpeedOutDhakaSeven(int deliverySpeedOutDhakaSeven) {
        DeliverySpeedOutDhakaSeven = deliverySpeedOutDhakaSeven;
    }

    public int getTotalProduct() {
        return TotalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        TotalProduct = totalProduct;
    }

    public int getPositiveReview() {
        return PositiveReview;
    }

    public void setPositiveReview(int positiveReview) {
        PositiveReview = positiveReview;
    }

    public int getNegativeReview() {
        return NegativeReview;
    }

    public void setNegativeReview(int negativeReview) {
        NegativeReview = negativeReview;
    }

    public int getMerchantTotalOrder() {
        return MerchantTotalOrder;
    }

    public void setMerchantTotalOrder(int merchantTotalOrder) {
        MerchantTotalOrder = merchantTotalOrder;
    }

    public int getCustomerTotalReview() {
        return CustomerTotalReview;
    }

    public void setCustomerTotalReview(int customerTotalReview) {
        CustomerTotalReview = customerTotalReview;
    }

    public String getInDhaka() {
        return InDhaka;
    }

    public void setInDhaka(String inDhaka) {
        InDhaka = inDhaka;
    }

    public String getOutDhaka() {
        return OutDhaka;
    }

    public void setOutDhaka(String outDhaka) {
        OutDhaka = outDhaka;
    }

    @Override
    public String toString() {
        return "ReviewRatingModel{" +
                "Id=" + Id +
                ", ProfileId=" + ProfileId +
                ", CompanyName='" + CompanyName + '\'' +
                ", CompanyNameBng='" + CompanyNameBng + '\'' +
                ", LogInEmail='" + LogInEmail + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", FulFillmentRating=" + FulFillmentRating +
                ", DeliverySpeedInsideDhaka=" + DeliverySpeedInsideDhaka +
                ", DeliverySpeedOutsideDhaka=" + DeliverySpeedOutsideDhaka +
                ", MerchantToken='" + MerchantToken + '\'' +
                ", RoutingName='" + RoutingName + '\'' +
                ", InsertedOn='" + InsertedOn + '\'' +
                ", AwardType=" + AwardType +
                ", AwardValue=" + AwardValue +
                ", AwardName='" + AwardName + '\'' +
                ", TotalScore=" + TotalScore +
                ", DeliverySpeed=" + DeliverySpeed +
                ", DeliverySpeedDhakaOne=" + DeliverySpeedDhakaOne +
                ", DeliverySpeedDhakaTwo=" + DeliverySpeedDhakaTwo +
                ", DeliverySpeedDhakaThree=" + DeliverySpeedDhakaThree +
                ", DeliverySpeedDhakaFour=" + DeliverySpeedDhakaFour +
                ", DeliverySpeedDhakaFive=" + DeliverySpeedDhakaFive +
                ", DeliverySpeedDhakaSix=" + DeliverySpeedDhakaSix +
                ", DeliverySpeedDhakaSeven=" + DeliverySpeedDhakaSeven +
                ", DeliverySpeedOutDhakaOne=" + DeliverySpeedOutDhakaOne +
                ", DeliverySpeedOutDhakaTwo=" + DeliverySpeedOutDhakaTwo +
                ", DeliverySpeedOutDhakaThree=" + DeliverySpeedOutDhakaThree +
                ", DeliverySpeedOutDhakaFour=" + DeliverySpeedOutDhakaFour +
                ", DeliverySpeedOutDhakaFive=" + DeliverySpeedOutDhakaFive +
                ", DeliverySpeedOutDhakaSix=" + DeliverySpeedOutDhakaSix +
                ", DeliverySpeedOutDhakaSeven=" + DeliverySpeedOutDhakaSeven +
                ", TotalProduct=" + TotalProduct +
                ", PositiveReview=" + PositiveReview +
                ", NegativeReview=" + NegativeReview +
                ", MerchantTotalOrder=" + MerchantTotalOrder +
                ", CustomerTotalReview=" + CustomerTotalReview +
                ", InDhaka='" + InDhaka + '\'' +
                ", OutDhaka='" + OutDhaka + '\'' +
                '}';
    }
}
