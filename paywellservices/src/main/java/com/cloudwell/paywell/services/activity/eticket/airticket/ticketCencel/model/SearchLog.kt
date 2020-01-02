package com.cloudwell.paywell.services.activity.eticket.airticket.ticketCencel.model

data class SearchLog(
    val adult_qty: String,
    val cabin_class: String,
    val child_qty: String,
    val departure_date_time: Any,
    val destination_port: String,
    val id: String,
    val infant_qty: String,
    val origin_port: String,
    val search_date_time: Any,
    val search_id: String,
    val user_id: Any
)