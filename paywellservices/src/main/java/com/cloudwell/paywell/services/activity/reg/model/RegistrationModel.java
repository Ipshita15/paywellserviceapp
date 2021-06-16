package com.cloudwell.paywell.services.activity.reg.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class RegistrationModel {
    @SerializedName("imei")
    private String imei;
    @SerializedName("alternate_imei")
    private String alternate_imei;
    @SerializedName("dtype")
    private String dtype;
    @SerializedName("downloadSource")
    private String downloadSource;
    @SerializedName("operators")
    private String operators;
    @SerializedName("outlet_name")
    private String outletName = "";
    @SerializedName("outlet_address")
    private String outletAddress;
    @SerializedName("owner_name")
    private String ownerName;
    @SerializedName("mobile_number")
    private String phnNumber;
    @SerializedName("business_type_id")
    private String businessId;
    @SerializedName("business_type")
    private String businessType;
    @SerializedName("email")
    private String emailAddress;
    @SerializedName("district")
    private String districtName;
    @SerializedName("thana")
    private String thanaName;
    @SerializedName("post_code")
    private String postcodeName;
    @SerializedName("post_office_id")
    private String postcodeId;
    @SerializedName("landmark")
    private String landmark;



    @SerializedName("outlet_img")
    private String outletImage = "";
    @SerializedName("nidFrontPic")
    private String nidFront = "";
    @SerializedName("nidBackPic")
    private String nidBack = "";
    @SerializedName("owner_img")
    private String ownerImage = "";
    @SerializedName("trade_license_img")
    private String tradeLicense = "";
    @SerializedName("image_passport")
    private String passport = "";
    @SerializedName("birth_certificate_img")
    private String birthCertificate = "";
    @SerializedName("driving_license_imege")
    private String drivingLicense= "";
    @SerializedName("visiting_card_img")
    private String visitingCard = "";

    @SerializedName("smartCardFrontPic")
    private String smartCardFront = "";
    @SerializedName("smartCardBackPic")
    private String smartCardBack = "";


    @SerializedName("nidNumber")
    public String nidNumber;
    @SerializedName("nidFatherName")
    public String nidFatherName;
    @SerializedName("nidName")
    public String nidName;
    @SerializedName("nidNameEngish")
    public String nidNameEngish;
    @SerializedName("nidMotherName")
    public String nidMotherName;
    @SerializedName("nidBirthday")
    public String nidBirthday;
    @SerializedName("nidAddress")
    public String nidAddress;
    @SerializedName("smartCardNumber")
    public String smartCardNumber;
    @SerializedName("smartCardName")
    public String smartCardName;
    @SerializedName("smartCardFatherName")
    public String smartCardFatherName;
    @SerializedName("smartCardMotherName")
    public String smartCardMotherName;
    @SerializedName("smartCardBirthday")
    public String smartCardBirthday;
    @SerializedName("smartCardAddress")
    public String smartCardAddress;

    @SerializedName("reference_code")
    private String referenceCode;

    @SerializedName("lan")
    private String lan;

    private String mBusinessaTypeAPIRespose;
    private int mBusinesstypeAdapterPosition = 0;
    private String mDistrictAPIRespose;
    private String mThanaResponseAPIRespose;
    private String mPostCodeResponseAPIRespose;
    private int mDistrictAdapterPosition;
    private int mThanaAdapterPosition;
    private int mPostCodeAdapterPosition;
    private String mMrchantType;
    private String mDistrict;
    private String salesCode;
    private String collectionCode;


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


    public String getNidNumber() {
        return nidNumber;
    }

    public void setNidNumber(String nidNumber) {
        this.nidNumber = nidNumber;
    }

    public String getNidFatherName() {
        return nidFatherName;
    }

    public void setNidFatherName(String nidFatherName) {
        this.nidFatherName = nidFatherName;
    }

    public String getNidName() {
        return nidName;
    }

    public void setNidName(String nidName) {
        this.nidName = nidName;
    }

    public String getNidMotherName() {
        return nidMotherName;
    }

    public void setNidMotherName(String nidMotherName) {
        this.nidMotherName = nidMotherName;
    }

    public String getNidBirthday() {
        return nidBirthday;
    }

    public void setNidBirthday(String nidBirthday) {
        this.nidBirthday = nidBirthday;
    }

    public String getNidAddress() {
        return nidAddress;
    }

    public void setNidAddress(String nidAddress) {
        this.nidAddress = nidAddress;
    }

    public String getSmartCardNumber() {
        return smartCardNumber;
    }

    public void setSmartCardNumber(String smartCardNumber) {
        this.smartCardNumber = smartCardNumber;
    }

    public String getSmartCardName() {
        return smartCardName;
    }

    public void setSmartCardName(String smartCardName) {
        this.smartCardName = smartCardName;
    }

    public String getSmartCardFatherName() {
        return smartCardFatherName;
    }

    public void setSmartCardFatherName(String smartCardFatherName) {
        this.smartCardFatherName = smartCardFatherName;
    }

    public String getSmartCardMotherName() {
        return smartCardMotherName;
    }

    public void setSmartCardMotherName(String smartCardMotherName) {
        this.smartCardMotherName = smartCardMotherName;
    }

    public String getSmartCardBirthday() {
        return smartCardBirthday;
    }

    public void setSmartCardBirthday(String smartCardBirthday) {
        this.smartCardBirthday = smartCardBirthday;
    }

    @NotNull
    public String getSmartCardAddress() {
        return smartCardAddress;
    }

    public void setSmartCardAddress(@NotNull String smartCardAddress) {
        this.smartCardAddress = smartCardAddress;
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

    public void setAlternate_imei(String alternate_imei) {
        this.alternate_imei = alternate_imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAlternate_imei() {
        return alternate_imei;
    }

    public String getImei() {
        return imei;
    }

    public void setDownloadSource(String downloadSource) {
        this.downloadSource = downloadSource;
    }

    public String getDownloadSource() {
        return downloadSource;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getDtype() {
        return dtype;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public String getOperators() {
        return operators;
    }

    public void setBusinessaTypeAPIRespose(String businessaTypeAPIRespose) {
        mBusinessaTypeAPIRespose = businessaTypeAPIRespose;
    }

    public String getBusinessaTypeAPIRespose() {
        return mBusinessaTypeAPIRespose;
    }

    public void setBusinesstypeAdapterPosition(int businesstypeAdapterPosition) {
        mBusinesstypeAdapterPosition = businesstypeAdapterPosition;
    }

    public int getBusinesstypeAdapterPosition() {
        return mBusinesstypeAdapterPosition;
    }

    public void setDistrictAPIRespose(String districtAPIRespose) {
        mDistrictAPIRespose = districtAPIRespose;
    }

    public String getDistrictAPIRespose() {
        return mDistrictAPIRespose;
    }

    public void setThanaResponseAPIRespose(String thanaResponseAPIRespose) {
        mThanaResponseAPIRespose = thanaResponseAPIRespose;
    }

    public String getThanaResponseAPIRespose() {
        return mThanaResponseAPIRespose;
    }

    public void setPostCodeResponseAPIRespose(String postCodeResponseAPIRespose) {
        mPostCodeResponseAPIRespose = postCodeResponseAPIRespose;
    }

    public String getPostCodeResponseAPIRespose() {
        return mPostCodeResponseAPIRespose;
    }

    public void setDistrictAdapterPosition(int districtAdapterPosition) {
        mDistrictAdapterPosition = districtAdapterPosition;
    }

    public int getDistrictAdapterPosition() {
        return mDistrictAdapterPosition;
    }

    public void setThanaAdapterPosition(int thanaAdapterPosition) {
        mThanaAdapterPosition = thanaAdapterPosition;
    }

    public int getThanaAdapterPosition() {
        return mThanaAdapterPosition;
    }

    public void setPostCodeAdapterPosition(int postCodeAdapterPosition) {
        mPostCodeAdapterPosition = postCodeAdapterPosition;
    }

    public int getPostCodeAdapterPosition() {
        return mPostCodeAdapterPosition;
    }



    public void setMrchantType(String mrchantType) {
        this.mMrchantType = mrchantType;
    }

    public String getMMrchantType() {
        return mMrchantType;
    }

    public void setDistrict(String district) {
        mDistrict = district;
    }

    public String getDistrict() {
        return mDistrict;
    }

    public String getNidNameEngish() {
        return nidNameEngish;
    }

    public void setNidNameEngish(String nidNameEngish) {
        this.nidNameEngish = nidNameEngish;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public int getmBusinesstypeAdapterPosition() {
        return mBusinesstypeAdapterPosition;
    }
}
