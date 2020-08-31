package com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize

data class RequestTicketInformationForCancel(@SerializedName("deviceId")
                                             var deviceId: String? = null, @SerializedName("password")
                                             var password: String? = null, @SerializedName("trxId")
                                             var trxId: String? = null, @SerializedName("username")
                                             var username: String? = null, @SerializedName("ticketNo")
                                             var ticketNo: String? = null, @SerializedName("otp")
                                             var otp: String? = null) : Parcelable {


}