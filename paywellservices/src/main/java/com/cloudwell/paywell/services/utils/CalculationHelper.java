package com.cloudwell.paywell.services.utils;


import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Fare;

import java.text.NumberFormat;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/4/19.
 */
public class CalculationHelper {

    public static String getTotalFare(Fare fares) {
        double totalPrice = fares.getBaseFare() + fares.getTax() + fares.getOtherCharges() + fares.getServiceFee();

        return NumberFormat.getInstance().format(totalPrice);
    }

    public static String getTotalFareDetati(com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare fares) {
        double totalPrice = fares.getBaseFare() + fares.getTax() + fares.getOtherCharges() + fares.getServiceFee();
        return NumberFormat.getInstance().format(totalPrice);
    }


}
