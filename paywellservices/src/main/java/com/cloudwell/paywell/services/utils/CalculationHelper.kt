package com.cloudwell.paywell.services.utils

import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Airline
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Fare
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.model.Commission
import com.google.gson.Gson
import java.text.NumberFormat


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/4/19.
 */
object CalculationHelper {


    val allLocalAirline = mutableListOf<Airline>()

    init {
        allLocalAirline.clear()
        val bangladesh = Airline();
        bangladesh.airlineCode = "BG"
        allLocalAirline.add(bangladesh)

        val UnitedAirways = Airline();
        UnitedAirways.airlineCode = "UAB"
        allLocalAirline.add(UnitedAirways)

        val NovoairAirlines = Airline();
        NovoairAirlines.airlineCode = "VQ"
        allLocalAirline.add(NovoairAirlines)


        val RegentAirways = Airline();
        RegentAirways.airlineCode = "RX"
        allLocalAirline.add(RegentAirways)

        val USBanglaAirlines = Airline();
        USBanglaAirlines.airlineCode = "BS"
        allLocalAirline.add(USBanglaAirlines)

    }

    fun getTotalFare(fares: List<Fare>, airlineCode: String?): String {
        val readData = InternalStorageHelper.readData(InternalStorageHelper.CombustionfileName)
        val commission = Gson().fromJson(readData, Commission::class.java)
        val retailerCommission = commission.retailerCommission
        val total_commission = commission.totalCommission

        var totalBaseFare = 0.0
        var totalTax = 0.0
        var totalOtherCharges = 0.0
        var totalServiceFee = 0.0
        var totalConvenienceFee = 0.0
        var totalRetailerCommission = 0.0

        var isLocalAirless = false
        isLocalAirless = checkIsLocalAirlines(airlineCode, isLocalAirless)


        for (fare in fares.indices) {

            val passengerCount = fares[fare].passengerCount;
            val baseFare = Math.ceil((fares[fare].baseFare) * passengerCount)
            totalBaseFare += baseFare;
            val Tax = Math.ceil(fares[fare].tax * passengerCount)
            totalTax += Tax

            val OtherCharges = Math.ceil(fares[fare].otherCharges * passengerCount)
            totalOtherCharges += OtherCharges;

            val ServiceFee = Math.ceil(fares[fare].serviceFee * passengerCount)
            totalServiceFee += ServiceFee;
            if (isLocalAirless) {
                totalServiceFee = 0.0
            }

            var convenienceFee = 0.0
            val Discount = (fares[fare].discount) * passengerCount
            if (commission.commissionType?.toInt() == 1) {

                val discountOnBasefare = (total_commission / 100) * baseFare;
                totalRetailerCommission += (retailerCommission / 100) * baseFare;
                if (Discount < discountOnBasefare) {
                    convenienceFee = Math.ceil(discountOnBasefare - Discount);
                    totalConvenienceFee += convenienceFee
                }
            }
            var subTotal = baseFare + Tax + OtherCharges + ServiceFee + convenienceFee;

        }


        val totalCalculated = totalBaseFare + totalTax + totalOtherCharges + totalServiceFee + totalConvenienceFee;

        val format = NumberFormat.getInstance().format(totalCalculated)

        return format
    }

    private fun checkIsLocalAirlines(airlineCode: String?, isAirlines: Boolean): Boolean {
        var isAirlines1 = isAirlines
        allLocalAirline.forEach {
            if (it.airlineCode.equals(airlineCode)) {
                isAirlines1 = true

            }
        }
        return isAirlines1
    }

    fun getFare(fares: com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare, airlineCode: String): com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare {
        val readData = InternalStorageHelper.readData(InternalStorageHelper.CombustionfileName)
        val commission = Gson().fromJson(readData, Commission::class.java)
        val retailerCommission = commission.retailerCommission
        val total_commission = commission.totalCommission

        var totalBaseFare = 0.0
        var totalTax = 0.0
        var totalOtherCharges = 0.0
        var totalServiceFee = 0.0
        var totalConvenienceFee = 0.0
        var totalRetailerCommission = 0.0


        var isLocalAirless = false
        isLocalAirless = checkIsLocalAirlines(airlineCode, isLocalAirless)


        val passengerCount = fares.passengerCount;
        val baseFare = Math.ceil((fares.baseFare) * passengerCount)
        fares.baseFare = baseFare
        totalBaseFare += baseFare;


        val Tax = Math.ceil(fares.tax * passengerCount)
        fares.tax = Tax
        totalTax += Tax;


        val OtherCharges = Math.ceil(fares.otherCharges * passengerCount)
        fares.otherCharges = OtherCharges
        totalOtherCharges += OtherCharges;


        val ServiceFee = Math.ceil(fares.serviceFee * passengerCount)
        fares.serviceFee = ServiceFee
        totalServiceFee += ServiceFee

        if (isLocalAirless) {
            totalServiceFee = 0.0
            fares.serviceFee = 0.0
        }

        var convenienceFee = 0.0

        val Discount = fares.discount * passengerCount
        if (commission.commissionType?.toInt() == 1) {

            val discountOnBasefare = (total_commission / 100) * baseFare;
            totalRetailerCommission += (retailerCommission / 100) * baseFare;
            if (Discount < discountOnBasefare) {
                convenienceFee = Math.ceil(discountOnBasefare - Discount);
                fares.convenienceFee = convenienceFee
                totalConvenienceFee += convenienceFee
            }
        }
        var subTotal = baseFare + Tax + OtherCharges + ServiceFee + convenienceFee;


        val totalCalculated = totalBaseFare + totalTax + totalOtherCharges + totalServiceFee + totalConvenienceFee;


        fares.amount = "" + NumberFormat.getInstance().format(totalCalculated)



        return fares
    }


    fun getTotalFareDetati(fares: List<com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare>, airlineCode: String): String {
        val readData = InternalStorageHelper.readData(InternalStorageHelper.CombustionfileName)
        val commission = Gson().fromJson(readData, Commission::class.java)
        val retailerCommission = commission.retailerCommission
        val total_commission = commission.totalCommission

        var totalBaseFare = 0.0
        var totalTax = 0.0
        var totalOtherCharges = 0.0
        var totalServiceFee = 0.0
        var totalConvenienceFee = 0.0
        var totalRetailerCommission = 0.0


        var isLocalAirless = false
        isLocalAirless = checkIsLocalAirlines(airlineCode, isLocalAirless)


        for (fare in fares.indices) {

            val passengerCount = fares[fare].passengerCount;
            val baseFare = Math.ceil((fares[fare].baseFare) * passengerCount)
            totalBaseFare += baseFare;
            val Tax = Math.ceil(fares[fare].tax * passengerCount)
            totalTax += Tax;

            val OtherCharges = Math.ceil(fares[fare].otherCharges * passengerCount)
            totalOtherCharges += OtherCharges;

            val ServiceFee = Math.ceil(fares[fare].serviceFee * passengerCount)
            totalServiceFee += ServiceFee

            if (isLocalAirless) {
                totalServiceFee = 0.0
            }

            var convenienceFee = 0.0;

            val Discount = fares[fare].discount * passengerCount;
            if (commission.commissionType?.toInt() == 1) {

                val discountOnBasefare = (total_commission / 100) * baseFare;
                totalRetailerCommission += (retailerCommission / 100) * baseFare;
                if (Discount < discountOnBasefare) {
                    convenienceFee = Math.ceil(discountOnBasefare - Discount);
                    totalConvenienceFee += convenienceFee;
                }
            }
            var subTotal = baseFare + Tax + OtherCharges + ServiceFee + convenienceFee;

        }


        val totalCalculated = totalBaseFare + totalTax + totalOtherCharges + totalServiceFee + totalConvenienceFee;

        val format = NumberFormat.getInstance().format(totalCalculated)

        return format
    }

    fun retailerEarning(fare: MutableList<com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare>): String? {

        val readData = InternalStorageHelper.readData(InternalStorageHelper.CombustionfileName)
        val commission = Gson().fromJson(readData, Commission::class.java)

        var totalBaseFare = 0.0

        fare.forEach {
            totalBaseFare = totalBaseFare + (it.baseFare * it.passengerCount)
        }

        val totalEarning = (totalBaseFare / 100) * (commission.retailerCommission?.toFloat()!!)

        val format = NumberFormat.getInstance().format(totalEarning.toInt())

        return format


    }

}




