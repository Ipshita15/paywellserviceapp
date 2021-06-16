package com.cloudwell.paywell.services.activity.eticket.airticket.ticketCencel.model

import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum

data class ResSingleBooking(
        val data : List<Datum>,
        val date_today: String,
        val message: String,
        val status: Int
)