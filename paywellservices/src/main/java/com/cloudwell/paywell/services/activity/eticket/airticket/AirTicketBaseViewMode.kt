package com.cloudwell.paywell.services.activity.eticket.airticket

import com.cloudwell.paywell.services.activity.base.BaseViewModel
import com.cloudwell.paywell.services.app.AppController

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
open class AirTicketBaseViewMode : BaseViewModel() {
    var mAirTicketRepository: AirThicketRepository

    init {
        mAirTicketRepository = AirThicketRepository(AppController.getContext())
    }


}
