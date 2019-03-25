package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
data class Passenger(val isDefault: Boolean) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Long = 0

    @ColumnInfo(name = "ContactNumber")
    @SerializedName("ContactNumber")
    var contactNumber: String = ""

    @ColumnInfo(name = "CountryCode")
    @SerializedName("CountryCode")
    var countryCode: String = ""

    @ColumnInfo(name = "Email")
    @SerializedName("Email")
    var email: String = ""


    @ColumnInfo(name = "FirstName")
    @SerializedName("FirstName")
    var firstName: String = ""

    @ColumnInfo(name = "Gender")
    @SerializedName("Gender")
    var gender: String = ""

    @ColumnInfo(name = "IsLeadPassenger")
    @SerializedName("IsLeadPassenger")
    var isLeadPassenger: Boolean = false

    @ColumnInfo(name = "LastName")
    @SerializedName("LastName")
    var lastName: String = ""

    @ColumnInfo(name = "Nationality")
    @SerializedName("Nationality")
    var nationality: String = ""

    @ColumnInfo(name = "PaxType")
    @SerializedName("PaxType")
    var paxType: String = ""


    @ColumnInfo(name = "Title")
    @SerializedName("Title")
    var title: String = ""


    @ColumnInfo(name = "isPassengerSleted")
    var isPassengerSleted: Boolean = false


    @ColumnInfo(name = "Country")
    var country: String = ""


    @SerializedName("passport_number")
    @ColumnInfo(name = "passport_number")
    var passportNumber: String = ""

    @ColumnInfo(name = "passportImagePath")
    var passportImagePath: String = ""


    @SerializedName("file_extension")
    @ColumnInfo(name = "file_extension")
    var file_extension: String = ""

    @SerializedName("nid_number")
    @ColumnInfo(name = "nid_number")
    var nIDnumber: String = ""


}
