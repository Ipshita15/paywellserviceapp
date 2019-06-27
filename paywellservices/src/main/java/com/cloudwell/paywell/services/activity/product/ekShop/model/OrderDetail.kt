package com.cloudwell.paywell.services.activity.product.ekShop.model

import com.google.gson.annotations.SerializedName


class OrderDetail {

    @SerializedName("cart_id")
    var cartId: String? = null
    @SerializedName("commission")
    var commission: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("image")
    var image: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("order_code")
    var orderCode: String? = null
    @SerializedName("price")
    var price: String? = null
    @SerializedName("product_url")
    var productUrl: String? = null
    @SerializedName("qty")
    var qty: String? = null
    @SerializedName("unit_price")
    var unitPrice: String? = null

}
