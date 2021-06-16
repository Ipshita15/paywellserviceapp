package com.cloudwell.paywell.services.activity.eticket.airticket.booking.model

import com.google.gson.annotations.SerializedName


class Trip {

    @SerializedName("airline_code")
    var airlineCode: String? = null
    @SerializedName("airline_name")
    var airlineName: String? = null
    @SerializedName("airline_pnr")
    var airlinePnr: String? = null
    @SerializedName("arrival_time")
    var arrivalTime: String? = null
    @SerializedName("baggage")
    var baggage: String? = null
    @SerializedName("booking_class")
    var bookingClass: String? = null
    @SerializedName("booking_id")
    var bookingId: String? = null
    @SerializedName("cabin_class")
    var cabinClass: String? = null
    @SerializedName("departure_time")
    var departureTime: String? = null
    @SerializedName("destination_airport_code")
    var destinationAirportCode: String? = null
    @SerializedName("destination_airport_name")
    var destinationAirportName: String? = null
    @SerializedName("destination_city")
    var destinationCity: String? = null
    @SerializedName("destination_country")
    var destinationCountry: String? = null
    @SerializedName("destination_terminal")
    var destinationTerminal: String? = null
    @SerializedName("equipment")
    var equipment: String? = null
    @SerializedName("flight_number")
    var flightNumber: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("journey_duration")
    var journeyDuration: String? = null
    @SerializedName("operating_career")
    var operatingCareer: String? = null
    @SerializedName("origin_airport_code")
    var originAirportCode: String? = null
    @SerializedName("origin_airport_name")
    var originAirportName: String? = null
    @SerializedName("origin_city")
    var originCity: String? = null
    @SerializedName("origin_country")
    var originCountry: String? = null
    @SerializedName("origin_terminal")
    var originTerminal: String? = null
    @SerializedName("search_booking_class")
    var searchBookingClass: String? = null
    @SerializedName("stop_quantity")
    var stopQuantity: String? = null
    @SerializedName("trip_indicator")
    var tripIndicator: String? = null

}
