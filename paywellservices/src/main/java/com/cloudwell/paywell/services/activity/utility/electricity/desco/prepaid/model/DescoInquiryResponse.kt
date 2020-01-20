package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model

import com.google.gson.annotations.SerializedName

class DescoInquiryResponse {
    @SerializedName("ApiStatus")
    var apiStatus: Int = 0
    @SerializedName("ApiStatusName")
    var apiStatusName: String = ""
    @SerializedName("ResponseDetails")
    var descoInquiryResponseDetails: DescoInquiryResponseDetails? = null

}