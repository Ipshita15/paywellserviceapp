package com.cloudwell.paywell.services.activity.healthInsurance.model

data class ActivePakage(
        var password: Int? = null,
        var gender: String? = null,
        var nomineePhoneNumber: String? = null,
        var packagesId: String? = null,
        var mobile: String? = null,
        var memberName: String? = null,
        val identificationNumber: String? = null,
        var identificationType: String? = null,
        var dateOfBirth: String? = null,
        var username: String? = null
)

