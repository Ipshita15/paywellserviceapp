package com.cloudwell.paywell.services.activity.eticket.airticket.dosInfoUpdate.model

import com.google.gson.annotations.SerializedName


class ReissuePassenger {

    @SerializedName("ContactNumber")
    var contactnumber: String? = null
    @SerializedName("CountryCode")
    var countrycode: String? = null
    @SerializedName("DateOfBirth")
    var dateofbirth: String? = null
    @SerializedName("Email")
    var email: String? = null
    @SerializedName("FirstName")
    var firstname: String? = null
    @SerializedName("Gender")
    var gender: String? = null
    @SerializedName("LastName")
    var lastname: String? = null
    @SerializedName("Nationality")
    var nationality: String? = null
    @SerializedName("nid_number")
    var nidNumber: String? = null
    @SerializedName("PassportExpiryDate")
    var passportExpiryDate: String? = null
    @SerializedName("PassportNationality")
    var passportNationality: String? = null
    @SerializedName("PassportNumber")
    var passportNumber: String? = null
    @SerializedName("PaxType")
    var paxtype: String? = null
    @SerializedName("Title")
    var title: String? = null

}
