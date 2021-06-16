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
        return this.from
    }

    fun setFromName(name: String) {
        this.from = name
    }

    fun getFromPortName(): String {
        return this.fromPort
    }

    fun setFromPortName(portName: String) {
        this.fromPort = portName
    }

    fun getToName(): String {
        return this.to
    }

    fun setToName(name: String) {
        this.to = name
    }

    fun getToPortName(): String {
        return this.toPort
    }

    fun setToPortName(portName: String) {
        this.toPort = portName
    }


}