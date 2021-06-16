package com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ResponseTicketInformationCancel(
        @SerializedName("message")
        var message: String? = null,

        @SerializedName("status_code")
        var statusCode: Int? = null,

        @SerializedName("ticketInfo")
        var ticketInfo: List<TicketInfo>? = null
) : Parcelable

