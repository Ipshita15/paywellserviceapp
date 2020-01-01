package com.cloudwell.paywell.services.activity.eticket.airticket.ticketCencel.model

data class Fare(
    val active_status: String,
    val base_fare: String,
    val booking_id: String,
    val convenience_fee: String,
    val discount: String,
    val id: String,
    val other_charges: String,
    val passenger_count: String,
    val pax_type: String,
    val service_fee: String,
    val tax: String
)