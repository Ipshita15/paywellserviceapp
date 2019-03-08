package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger

class FlightDetails2ViewModel : AirTicketBaseViewMode() {


    val mListMutableLiveDPassengers = MutableLiveData<List<Passenger>>()


    fun getAllPassengers() {

        mAirTicketRepository.getAllPassengers().observeForever {
            it.let {
                val toMutableList = it?.toMutableList()
                toMutableList?.add(Passenger(true))
                mListMutableLiveDPassengers.value = toMutableList
            }

        }
    }
}
