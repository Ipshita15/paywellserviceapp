package com.cloudwell.paywell.services.activity.eticket.airticket

class ClassModel {

    var className: String
    var isSelected: Boolean

    constructor(className: String, isSelected: Boolean) {
        this.className = className
        this.isSelected = isSelected
    }

    fun getClassSelectedName(): String? {
        return className
    }

    fun setClassSelectedName(name: String) {
        className = name
    }

    fun getClassSelectedStatus(): Boolean? {
        return isSelected
    }

    fun setClassSelectedStatus(selected: Boolean) {
        isSelected = selected
    }

}