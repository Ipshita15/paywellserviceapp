package com.cloudwell.paywell.services.activity.eticket.airticket

class SearchRoundTripModel {
    var from: String
    var fromPort: String
    var to: String
    var toPort: String
    var departDate: String = ""

    constructor(from: String, to: String, fromPort: String, toPort: String) {
        this.from = from
        this.fromPort = fromPort
        this.to = to
        this.toPort = toPort
    }

    fun getFromName(): String {
        return from
    }

    fun setFromName(name: String) {
        from = name
    }

    fun getFromPortName(): String {
        return fromPort
    }

    fun setFromPortName(portName: String) {
        fromPort = portName
    }

    fun getToName(): String {
        return to
    }

    fun setToName(name: String) {
        to = name
    }

    fun getToPortName(): String {
        return toPort
    }

    fun setToPortName(portName: String) {
        toPort = portName
    }


}