package com.cloudwell.paywell.services.activity.entertainment.Bongo.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Sepon on 3/31/2020.
 */
data class BongoEnquiryRqstPojo (

        @field:SerializedName("username")
        var username: String? = "",

        @field:SerializedName("referenceIdOrMobile")
        var referenceIdOrMobile : String? = "",

        @field:SerializedName("limit")
        var limit: String? = ""

)