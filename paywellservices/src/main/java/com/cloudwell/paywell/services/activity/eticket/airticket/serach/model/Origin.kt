package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Origin : Parcelable {

    @SerializedName("Airport")
    var airport: Airport? = null
    @SerializedName("DepTime")
    var depTime: String? = null

}
