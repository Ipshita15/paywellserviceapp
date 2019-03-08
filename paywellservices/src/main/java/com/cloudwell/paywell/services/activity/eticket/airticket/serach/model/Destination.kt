package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Destination : Parcelable {

    @SerializedName("Airport")
    var airport: Airport = Airport()
    @SerializedName("ArrTime")
    var arrTime: String = ""

}
