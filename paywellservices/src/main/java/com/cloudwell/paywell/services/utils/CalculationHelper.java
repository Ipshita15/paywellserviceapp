package com.cloudwell.paywell.services.utils;


import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Fare;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/4/19.
 */
public class CalculationHelper {

    public static String getTotalFare(List<Fare> fares) {

        double totalPrice = 0;
        for (Fare fare : fares) {
            double prices = (fare.getBaseFare() + fare.getTax() + fare.getOtherCharges() + fare.getServiceFee()) * fare.getPassengerCount();
            totalPrice = totalPrice + prices;
        }

        long v = (long) totalPrice;
        return NumberFormat.getInstance().format(v);
    }

    public static String getTotalFareDetati(List<com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare> fares) {
        double totalPrice = 0;
        for (com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare fare : fares) {
            double prices = (fare.getBaseFare() + fare.getTax() + fare.getOtherCharges() + fare.getServiceFee()) * fare.getPassengerCount();
            totalPrice = totalPrice + prices;
        }

        long v = (long) totalPrice;
        return NumberFormat.getInstance().format(v);
    }


}
