package com.cloudwell.paywell.services.activity.bank_info_update.model

import com.google.gson.annotations.SerializedName

data class BankPojo(

        @field:SerializedName("username")
		var username: String? = null,

        @field:SerializedName("ref_id")
	var refId: String? = null,

        @field:SerializedName("branchId")
	var branchId: String? = null,

        @field:SerializedName("password")
	var password: String? = null,

        @field:SerializedName("bankId")
        var bankId: String? = null,

        @field:SerializedName("districtId")
        var districtId: String? = null,

        @field:SerializedName("accountNo")
        var accountNo: String? = null,

        @field:SerializedName("accountName")
        var accountName: String? = null,

        @field:SerializedName("checkBookSlip")
        var checkBookSlip: String? = null

)