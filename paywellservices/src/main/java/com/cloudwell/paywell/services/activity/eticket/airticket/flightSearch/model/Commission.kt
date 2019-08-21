package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName


class Commission {
    @ColumnInfo(name = "active")
    @SerializedName("active")
    var active: String? = null

    @ColumnInfo(name = "active")
    @SerializedName("actual_id")
    var actualId: Any? = null
    @SerializedName("api")
    var api: Any? = null
    @SerializedName("api2")
    var api2: Any? = null
    @SerializedName("archive_table")
    var archiveTable: Any? = null
    @SerializedName("commission_type")
    var commissionType: String? = null
    @SerializedName("crs_commission")
    var crsCommission: String? = null
    @SerializedName("data_table")
    var dataTable: Any? = null
    @SerializedName("dealer_commission")
    var dealerCommission: String? = null
    @SerializedName("distributor_commission")
    var distributorCommission: String? = null
    @SerializedName("frozen_amount")
    var frozenAmount: Any? = null
    @SerializedName("frozen_updated")
    var frozenUpdated: Any? = null
    @SerializedName("information_table")
    var informationTable: Any? = null
    @SerializedName("last_updated")
    var lastUpdated: Any? = null
    @SerializedName("prefix")
    var prefix: Any? = null
    @SerializedName("retailer_commission")
    var retailerCommission = 0.0
    @SerializedName("service_id")
    var serviceId: String? = null
    @SerializedName("stock")
    var stock: String? = null
    @SerializedName("sub_service_id")
    var subServiceId: String? = null
    @SerializedName("sub_service_name")
    var subServiceName: String? = null
    @SerializedName("sub_service_short_name")
    var subServiceShortName: String? = null
    @SerializedName("total_commission")
    var totalCommission = 0.0
    @SerializedName("used_stock")
    var usedStock: String? = null

}
