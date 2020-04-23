package com.cloudwell.paywell.services.activity.home.model

import com.google.gson.annotations.SerializedName

class ResponseDetailsUserProfile {

    @SerializedName("businessTypeStatus")
    val mBusinessTypeStatus: Int = 0
    @SerializedName("changePinStatus")
    val mChangePinStatus: String? = null
    @SerializedName("count")
    val mCount: Long? = null
    @SerializedName("menuData")
    val menuData: List<Menu>? = null

    @SerializedName("data")
    val data: List<String>? = null
    @SerializedName("displayPictureCount")
    val mDisplayPictureCount: Int = 0
    @SerializedName("imageLink")
    val mImageLink: List<String>? = null

    @SerializedName("imageAddress")
    val imageAddress: List<String>? = null


    @SerializedName("details")
    val details: List<String>? = null



    @SerializedName("isPhoneVerfied")
    val mIsPhoneVerfied: String? = null
    @SerializedName("mobile_number")
    val mMobileNumber: String? = null
    @SerializedName("retailer_code")
    val mRetailerCode: String? = null
    @SerializedName("SME")
    val mSME: Int = 0
    @SerializedName("Status")
    val mStatus: Int = 0
    @SerializedName("StatusName")
    val mStatusName: String? = null

    @SerializedName("message")
    val message: String? = null

}
