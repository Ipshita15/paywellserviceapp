package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-07-16.
 */
class PassengerForAPI {


    @SerializedName("Title")
    var title: String? = null


    @SerializedName("FirstName")
    var firstname: String? = null


    @SerializedName("LastName")
    var lastname: String? = null

    @SerializedName("PaxType")
    var paxtype: String? = null

    @SerializedName("DateOfBirth")
    var dateofbirth: String? = null


    @SerializedName("Gender")
    var gender: String? = null


    @SerializedName("PassportNumber")
    var passportNumber: String? = null


    @SerializedName("PassportExpiryDate")
    var passportExpiryDate: String? = null


    @SerializedName("PassportNationality")
    var passportNationality: String? = null

    @SerializedName("CountryCode")
    var countrycode: String? = null

    @SerializedName("Nationality")
    var nationality: String? = null


    @SerializedName("ContactNumber")
    var contactnumber: String? = null


    @SerializedName("Email")
    var email: String? = null


    @SerializedName("IsLeadPassenger")
    var isLeadPassenger: Boolean = false


}