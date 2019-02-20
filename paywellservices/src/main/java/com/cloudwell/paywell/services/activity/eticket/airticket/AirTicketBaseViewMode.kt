package com.cloudwell.paywell.services.activity.eticket.airticket

import com.cloudwell.paywell.services.activity.base.BaseViewModel
import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.ReposeAirSearch
import com.cloudwell.paywell.services.app.AppController

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
open class AirTicketBaseViewMode : BaseViewModel() {
    var mAirTicketRepository: AirThicketRepository

    init {
        mAirTicketRepository = AirThicketRepository(AppController.getContext())
    }

    fun isOkNetworkAndStatusCode(t: ReposeAirSearch?): Boolean {
        t?.let {
            if (it.throwable != null) {
                baseViewStatus.value = it.throwable.message?.let { it1 ->
                    BaseViewState(errorMessage = it1)
                }

                return false
            } else if (it.status == 313) {
                baseViewStatus.value = BaseViewState(errorMessage = t.message)
                return false
            } else if (it.status != 200) {
                baseViewStatus.value = BaseViewState(errorMessage = t.message)
                return false
            }
            return true
        }
        return false
    }

}
