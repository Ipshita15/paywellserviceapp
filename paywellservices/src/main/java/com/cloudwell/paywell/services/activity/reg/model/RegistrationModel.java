package com.cloudwell.paywell.services.activity.reg.model;

public class RegistrationModel {

    private String outletName;
    private String outletAddress;
    private String ownerName;
    private String phnNumber;
    private String businessId;
    private String businessType;
    private String emailAddress;
    private String districtName;
    private String thanaName;
    private String postcodeName;
    private String postcodeId;
    private String landmark;
    private String salesCode;
    private String collectionCode;
    private String outletImage;
    private String nidFront;
    private String nidBack;
    private String ownerImage;
    private String tradeLicense;
    private String passport;
    private String birthCertificate;
    private String drivingLicense;
    private String visitingCard;

    private String smartCardFront;
    private String smartCardBack;

    public RegistrationModel() {
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhnNumber() {
        return phnNumber;
    }

    public void setPhnNumber(String phnNumber) {
        this.phnNumber = phnNumber;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getThanaName() {
        return thanaName;
    }

    public void setThanaName(String thanaName) {
        this.thanaName = thanaName;
    }

    public String getPostcodeName() {
        return postcodeName;
    }

    public void setPostcodeName(String postcodeName) {
        this.postcodeName = postcodeName;
    }

    public String getPostcodeId() {
        return postcodeId;
    }

    public void setPostcodeId(String postcodeId) {
        this.postcodeId = postcodeId;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getOutletImage() {
        return outletImage;
    }

    public void setOutletImage(String outletImage) {
        this.outletImage = outletImage;
    }

    public String getNidFront() {
        return nidFront;
    }

    public void setNidFront(String nidFront) {
        this.nidFront = nidFront;
    }

    public String getNidBack() {
        return nidBack;
    }

    public void setNidBack(String nidBack) {
        this.nidBack = nidBack;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getTradeLicense() {
        return tradeLicense;
    }

    public void setTradeLicense(String tradeLicense) {
        this.tradeLicense = tradeLicense;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getBirthCertificate() {
        return birthCertificate;
    }

    public void setBirthCertificate(String birthCertificate) {
        this.birthCertificate = birthCertificate;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getVisitingCard() {
        return visitingCard;
    }

    public void setVisitingCard(String visitingCard) {
        this.visitingCard = visitingCard;
    }

    @Override
    public String toString() {
        return "RegistrationModel{" +
                "outletName='" + outletName + '\'' +
                ", outletAddress='" + outletAddress + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", phnNumber='" + phnNumber + '\'' +
                ", businessId='" + businessId + '\'' +
                ", businessType='" + businessType + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", districtName='" + districtName + '\'' +
                ", thanaName='" + thanaName + '\'' +
                ", postcodeName='" + postcodeName + '\'' +
                ", postcodeId='" + postcodeId + '\'' +
                ", landmark='" + landmark + '\'' +
                ", salesCode='" + salesCode + '\'' +
                ", collectionCode='" + collectionCode + '\'' +
                ", outletImage='" + outletImage + '\'' +
                ", nidFront='" + nidFront + '\'' +
                ", nidBack='" + nidBack + '\'' +
                ", ownerImage='" + ownerImage + '\'' +
                ", tradeLicense='" + tradeLicense + '\'' +
                ", passport='" + passport + '\'' +
                ", birthCertificate='" + birthCertificate + '\'' +
                ", drivingLicense='" + drivingLicense + '\'' +
                ", visitingCard='" + visitingCard + '\'' +
                '}';
    }

    public void setSmartCardBack(String smartCardBack) {
        this.smartCardBack = smartCardBack;
    }

    public String getSmartCardBack() {
        return smartCardBack;
    }

    public String getSmartCardFront() {
        return smartCardFront;
    }

    public void setSmartCardFront(String smartCardFront) {
        this.smartCardFront = smartCardFront;
    }
}
