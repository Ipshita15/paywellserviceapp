package com.cloudwell.paywell.services.activity.bank_info_update.model

import com.google.gson.annotations.SerializedName

class ResposeAddLinkBankAccount {
    @SerializedName("message")
    var message: String = ""

    @SerializedName("status")
    var status: Long = 0

}