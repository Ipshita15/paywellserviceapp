package com.cloudwell.paywell.services.activity.product.ekShop.model

import com.google.gson.annotations.SerializedName


class Data {

    @SerializedName("access_token")
    var accessToken: String? = null
    @SerializedName("cart_id")
    var cartId: String? = null
    @SerializedName("ekshop_commission")
    var ekshopCommission: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("order_code")
    var orderCode: String? = null
    @SerializedName("order_details")
    var orderDetails: List<OrderDetail>? = null
    @SerializedName("other_required_data")
    var otherRequiredData: String? = null
    @SerializedName("paywell_user_id")
    var paywellUserId: String? = null
    @SerializedName("pwl_commission")
    var pwlCommission: String? = null
    @SerializedName("pwl_user_commission")
    var pwlUserCommission: String? = null
    @SerializedName("request_datetime")
    var requestDatetime: String? = null
    @SerializedName("session_token")
    var sessionToken: String? = null
    @SerializedName("shipping_cost")
    var shippingCost: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("total_amount")
    var totalAmount: String? = null
    @SerializedName("total_commission")
    var totalCommission: String? = null
    @SerializedName("update_datetime")
    var updateDatetime: String? = null

}
