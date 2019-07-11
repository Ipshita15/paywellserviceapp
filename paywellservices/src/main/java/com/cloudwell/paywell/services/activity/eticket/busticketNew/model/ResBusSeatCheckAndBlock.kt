package com.cloudwell.paywell.services.activity.eticket.busticketNew.model


import com.google.gson.annotations.SerializedName

class ResBusSeatCheckAndBlock {

    @SerializedName("meassage")
    var meassage: String = ""
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("ticketInfo")
    var ticketInfo: TicketInfo? = null
    @SerializedName("trans_id")
    var transId: String? = null

}
