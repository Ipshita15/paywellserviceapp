package com.cloudwell.paywell.services.activity.mfs.mycash.cash.model

import com.google.gson.annotations.SerializedName


class RequestBalanceTransferConfirm {
    @SerializedName("password")
    var password: String = ""
    @SerializedName("username")
    var username: String = ""

}