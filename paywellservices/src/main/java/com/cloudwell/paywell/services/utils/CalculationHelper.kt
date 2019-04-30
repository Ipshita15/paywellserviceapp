package com.cloudwell.paywell.service

import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Fare
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.model.Commission
import com.cloudwell.paywell.services.utils.InternalStorageHelper
import com.google.gson.Gson
import java.text.NumberFormat


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/4/19.
 */
object CalculationHelper {

    fun getTotalFare(fares: List<Fare>): String {
        val readData = InternalStorageHelper.readData(InternalStorageHelper.CombustionfileName)
        val commission = Gson().fromJson(readData, Commission::class.java)


        var totalBaseFare = 0.0
        var totalTax = 0.0
        var totalOtherCharges = 0.0
        var totalServiceFee = 0.0
        var totalConvenienceFee = 0.0
        var totalRetailerCommission = 0.0
        val total_commission = 0.0

        val retailer_commission = 0.0

        for (fare in fares.indices) {

            val passengerCount = fares[fare].passengerCount;
            val baseFare = Math.ceil((fares[fare].baseFare) * passengerCount)
            totalBaseFare += baseFare;
            val Tax = Math.ceil(fares[fare].tax) * passengerCount;
            totalTax += Tax;

            val OtherCharges = Math.ceil(fares[fare].otherCharges) * passengerCount;
            totalOtherCharges += OtherCharges;

            val ServiceFee = Math.ceil(Math.ceil(fares[fare].serviceFee) * passengerCount)
            totalServiceFee += ServiceFee;
            var convenienceFee = 0.0;

            var Discount = fares[fare].discount * passengerCount;
            if (commission.commissionType?.toInt() == 1) {

                var discountOnBasefare = (total_commission / 100) * baseFare;
                totalRetailerCommission += (retailer_commission / 100) * baseFare;
                if (Discount < discountOnBasefare) {
                    convenienceFee = Math.ceil(discountOnBasefare - Discount);
                    totalConvenienceFee += convenienceFee;
                }
            }
            var subTotal = baseFare + Tax + OtherCharges + ServiceFee + convenienceFee;

        }


        var totalCalculated = totalBaseFare + totalTax + totalOtherCharges + totalServiceFee + totalConvenienceFee;

        val format = NumberFormat.getInstance().format(totalCalculated)

        return format
    }

    fun getFare(fares: com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare): com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare {
        val readData = InternalStorageHelper.readData(InternalStorageHelper.CombustionfileName)
        val commission = Gson().fromJson(readData, Commission::class.java)


        var totalBaseFare = 0.0
        var totalTax = 0.0
        var totalOtherCharges = 0.0
        var totalServiceFee = 0.0
        var totalConvenienceFee = 0.0
        var totalRetailerCommission = 0.0
        val total_commission = 0.0
        val retailer_commission = 0.0


        val passengerCount = fares.passengerCount;
        fares.baseFare = Math.ceil((fares.baseFare))
        val baseFare = Math.ceil((fares.baseFare) * passengerCount)
        totalBaseFare += baseFare;

        fares.tax = Math.ceil(fares.tax)
        val Tax = Math.ceil(fares.tax) * passengerCount;

        totalTax += Tax;

        fares.otherCharges = Math.ceil(fares.otherCharges)
        val OtherCharges = Math.ceil(fares.otherCharges) * passengerCount;

        totalOtherCharges += OtherCharges;

        fares.serviceFee = Math.ceil(Math.ceil(fares.serviceFee))
        val ServiceFee = Math.ceil(Math.ceil(fares.serviceFee) * passengerCount)

        totalServiceFee += ServiceFee
        var convenienceFee = 0.0;

        val Discount = fares.discount * passengerCount
        if (commission.commissionType?.toInt() == 1) {

            val discountOnBasefare = (total_commission / 100) * baseFare;
            totalRetailerCommission += (retailer_commission / 100) * baseFare;
            if (Discount < discountOnBasefare) {
                convenienceFee = Math.ceil(discountOnBasefare - Discount);
                fares.convenienceFee = Math.ceil(discountOnBasefare - Discount)
                totalConvenienceFee += convenienceFee
            }
        }
        var subTotal = baseFare + Tax + OtherCharges + ServiceFee + convenienceFee;


        val totalCalculated = totalBaseFare + totalTax + totalOtherCharges + totalServiceFee + totalConvenienceFee;

        fares.amount = "" + totalCalculated



        return fares
    }


    fun getTotalFareDetati(fares: List<com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare>): String {
        val readData = InternalStorageHelper.readData(InternalStorageHelper.CombustionfileName)
        val commission = Gson().fromJson(readData, Commission::class.java)

        var totalBaseFare = 0.0
        var totalTax = 0.0
        var totalOtherCharges = 0.0
        var totalServiceFee = 0.0

        val total_commission = 0.0
        var totalRetailerCommission = 0.0
        val retailer_commission = 0.0
        var totalConvenienceFee = 0.0

        for (i in fares.indices) {

            val passengerCount = fares[i].passengerCount
            //console.log("passenger "+passengerCount);
            val baseFare = (Math.ceil(fares[i].baseFare)) * passengerCount
            //console.log("baseFare "+baseFare);
            totalBaseFare += baseFare
            //console.log("totalBaseFare "+totalBaseFare);
            val Tax = (Math.ceil(fares[i].tax)) * passengerCount
            totalTax += Tax
            val OtherCharges = (Math.ceil(fares.get(i).otherCharges)) * passengerCount
            totalOtherCharges += OtherCharges

            val ServiceFee = (Math.ceil((fares.get(0).serviceFee))) * passengerCount
            totalServiceFee += ServiceFee

            var convenienceFee: Double

            val Discount = (fares.get(i).discount) * passengerCount



            if (commission.commissionType?.toInt() == 1) {

                val discountOnBasefare = total_commission / 100 * baseFare
                totalRetailerCommission += retailer_commission / 100 * baseFare
                if (Discount < discountOnBasefare) {
                    convenienceFee = Math.ceil((discountOnBasefare - Discount))
                    totalConvenienceFee += convenienceFee
                }
            }

        }
        val totalCalculated = totalBaseFare + totalTax + totalOtherCharges + totalServiceFee + totalConvenienceFee


        return NumberFormat.getInstance().format(totalCalculated)
    }

    fun retailerEaring(fare: MutableList<com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare>): String? {

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




