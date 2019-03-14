package com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity
class Airport : Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: String = ""

    @ColumnInfo(name = "airport_name")
    @SerializedName("airport_name")
    var airportName: String = ""

    @ColumnInfo(name = "city")
    @SerializedName("city")
    var city: String = ""

    @ColumnInfo(name = "country")
    @SerializedName("country")
    var country: String = ""

    @ColumnInfo(name = "iata")
    @SerializedName("iata")
    var iata: String = ""


    @ColumnInfo(name = "icao")
    @SerializedName("icao")
    var icao: String = ""


    @ColumnInfo(name = "iso")
    @SerializedName("iso")
    var iso: String = ""


    @ColumnInfo(name = "state")
    @SerializedName("state")
    var state: String = ""

    @ColumnInfo(name = "time_zone")
    @SerializedName("time_zone")
    var timeZone: String = ""


    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: String = ""


}
