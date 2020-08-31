package com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TicketInfo(
        @SerializedName("basePrice")
        var basePrice: String? = null,

        @SerializedName("cancelTime")
        var cancelTime: String? = null,

        @SerializedName("cancellationFee")
        var cancellationFee: Long? = null,

        @SerializedName("coachNo")
        var coachNo: String? = null,

        @SerializedName("departureDateTime")
        var departureDateTime: String? = null,

        @SerializedName("isCancel")
        var isCancel: Long? = null,

        @SerializedName("journeyRoute")
        var journeyRoute: String? = null,

        @SerializedName("returnAmount")
        var returnAmount: Long? = null,

        @SerializedName("ticketNo")
        var ticketNo: String? = null,

        @SerializedName("totalAmount")
        var totalAmount: String? = null,

        @SerializedName("transportName")
        var transportName: String? = null,

        @SerializedName("trx_id")
        var trxId: String? = null,


        @SerializedName("journey_type")
        var journey_type: String? = null,

        @SerializedName("seat")
        var seat: String? = null,

        @SerializedName("boardingPoint")
        var boardingPoint: String? = null,

        @SerializedName("otp")
        var otp: String? = null


) : Parcelable