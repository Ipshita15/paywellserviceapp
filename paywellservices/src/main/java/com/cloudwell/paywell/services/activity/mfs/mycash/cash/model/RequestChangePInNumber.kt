package com.cloudwell.paywell.services.activity.mfs.mycash.cash.model

import com.google.gson.annotations.SerializedName

class RequestChangePInNumber {
    @SerializedName("newMycashPin")
    var newMycashPin: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("myCashPin")
    var myCashPin: String = ""
    @SerializedName("service_type")
    var serviceType: String = ""
    @SerializedName("username")
    var username: String = ""

}