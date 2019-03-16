package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel

import android.arch.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger


class AllSummaryActivityViewModel : AirTicketBaseViewMode() {
    var mListMutableLiveDPassengers = MutableLiveData<MutableList<Passenger>>()


    fun init(passengerIDS: String) {
        val split = passengerIDS.split(",")

        mAirTicketRepository.getAllPassengers(split).observeForever {

            mListMutableLiveDPassengers.value = it as MutableList<Passenger>?

        }

    }

}
