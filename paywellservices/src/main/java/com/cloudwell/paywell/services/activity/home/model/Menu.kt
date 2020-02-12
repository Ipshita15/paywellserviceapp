package com.cloudwell.paywell.services.activity.home.model

import com.google.gson.annotations.SerializedName

class Menu {
    @SerializedName("menu_id")
    var menuId: String? = null
    @SerializedName("menu_name")
    var menuName: String? = null
    @SerializedName("parent_id")
    var parentId: String? = null

}