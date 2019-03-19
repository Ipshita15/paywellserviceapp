package com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.viewmodel

import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.view.PassgerAddViewStatus

class AddPassengerViewModel : AirTicketBaseViewMode() {

    val mViewStatus = SingleLiveEvent<PassgerAddViewStatus>()

    fun addPassenger(passenger: Passenger) {
        baseViewStatus.value = BaseViewState(isProgressIndicatorShown = true)
        mAirTicketRepository.addPassenger(passenger).observeForever({
            baseViewStatus.value = BaseViewState(isProgressIndicatorShown = false)
            if (it != -1L) {
                mViewStatus.value = PassgerAddViewStatus(isPassengerAddSuccessful = true)
            }

        })
    }

    fun updatePassenger(passenger: Passenger) {
        baseViewStatus.value = BaseViewState(isProgressIndicatorShown = true)
        mAirTicketRepository.updatePassenger(passenger).observeForever({
            baseViewStatus.value = BaseViewState(isProgressIndicatorShown = false)
            if (it != -1) {
                mViewStatus.value = PassgerAddViewStatus(isPassengerAddSuccessful = true)
            }

        })

    }

}
