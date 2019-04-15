package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model

class BookingStatuViewStatus(var isShowProcessIndicatior: Boolean = false, var noSerachFoundMessage: String = "", var successMessage: String = "") {
    public var successMessageTricketStatus: String = ""
    var modelPriceChange: List<Datum>? = null


}
