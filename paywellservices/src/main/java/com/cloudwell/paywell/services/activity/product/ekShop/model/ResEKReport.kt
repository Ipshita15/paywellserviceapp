package com.cloudwell.paywell.services.activity.product.ekShop.model

import com.google.gson.annotations.SerializedName


class ResEKReport {

    @SerializedName("data")
    var data: List<Data> = mutableListOf()
    @SerializedName("message")
    var message: String = ""
    @SerializedName("status")
    var status: Int = 0

}
