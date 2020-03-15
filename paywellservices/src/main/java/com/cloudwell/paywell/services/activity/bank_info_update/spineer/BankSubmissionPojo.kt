package com.cloudwell.paywell.services.activity.bank_info_update.spineer

import com.google.gson.annotations.SerializedName

/**
 * Created by Sepon on 3/10/2020.
 */
data class BankSubmissionPojo (

        @field: SerializedName("bankName")
   var bankName : String? = "",

        @field: SerializedName ("bankId")
   var bankId : String? = "",

        @field: SerializedName ("districtName")
   var districtName : String? = "",

        @field: SerializedName ("districtId")
   var districtId : String? = "",

        @field: SerializedName ("branchName")
   var branchName : String? = "",

        @field: SerializedName ("branchId")
   var branchId : String? = "",

        @field: SerializedName ("accountNumber")
   var accountNumber : String? = ""

)