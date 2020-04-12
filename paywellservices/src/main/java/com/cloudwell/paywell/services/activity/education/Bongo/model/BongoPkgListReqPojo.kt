package com.cloudwell.paywell.services.activity.education.Bongo.model

import com.cloudwell.paywell.services.activity.education.BBC.model.Data
import com.google.gson.annotations.SerializedName

/**
 * Created by Sepon on 3/31/2020.
 */
data class BongoPkgListReqPojo (

        @field:SerializedName("locale")
        var language: String? = ""

//        @field:SerializedName("username")
//        var username: String = ""
)