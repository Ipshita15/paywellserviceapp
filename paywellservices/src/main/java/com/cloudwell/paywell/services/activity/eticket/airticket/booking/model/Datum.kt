package com.cloudwell.paywell.services.activity.eticket.airticket.booking.model

import com.google.gson.annotations.SerializedName

class Datum {

    @SerializedName("adult_qty")
    var adultQty: String? = null
    @SerializedName("availablity")
    var availablity: String? = null
    @SerializedName("balance_deduction_flag")
    var balanceDeductionFlag: Any? = null
    @SerializedName("balance_return_flag")
    var balanceReturnFlag: Any? = null
    @SerializedName("biller_commission")
    var billerCommission: String? = null
    @SerializedName("booking_id")
    var bookingId: String? = null
    @SerializedName("child_qty")
    var childQty: String? = null
    @SerializedName("currency")
    var currency: String? = null
    @SerializedName("cw_commission")
    var cwCommission: String? = null
    @SerializedName("dealler_id")
    var deallerId: String? = null
    @SerializedName("discount")
    var discount: String? = null
    @SerializedName("distributor_commission")
    var distributorCommission: String? = null
    @SerializedName("distributor_id")
    var distributorId: String? = null
    @SerializedName("fare_type")
    var fareType: String? = null
    @SerializedName("first_request_date_time")
    var firstRequestDateTime: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("infant_qty")
    var infantQty: String? = null
    @SerializedName("journey_type")
    var journeyType: String? = null
    @SerializedName("last_request_date_time")
    var lastRequestDateTime: Any? = null
    @SerializedName("last_ticket_date")
    var lastTicketDate: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("passengers")
    var passengers: List<Passenger>? = null
    @SerializedName("result_id")
    var resultId: String? = null
    @SerializedName("retailer_commission")
    var retailerCommission: String? = null
    @SerializedName("retailer_id")
    var retailerId: String? = null
    @SerializedName("search_id")
    var searchId: String? = null
    @SerializedName("status_code")
    var statusCode: String? = null
    @SerializedName("ticket_date_time")
    var ticketDateTime: Any? = null
    @SerializedName("total_fare")
    var totalFare: String? = null
    @SerializedName("validating_career")
    var validatingCareer: String? = null

    @SerializedName("invoice_url")
    var invoiceUrl: String = ""


}
