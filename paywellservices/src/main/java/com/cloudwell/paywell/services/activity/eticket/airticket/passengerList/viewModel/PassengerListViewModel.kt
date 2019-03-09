package com.cloudwell.paywell.services.activity.eticket.airticket.passengerList.viewModel

import android.arch.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger

class PassengerListViewModel : AirTicketBaseViewMode() {

    var mListMutableLiveDPassengers = MutableLiveData<MutableList<Passenger>>()

    fun getAllPassengers() {

        mAirTicketRepository.getAllPassengers().observeForever {
            it.let {
                val toMutableList = it?.toMutableList()
                mListMutableLiveDPassengers.value = toMutableList
            }

        }
    }
}
