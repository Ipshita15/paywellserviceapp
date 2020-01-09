package com.cloudwell.paywell.services.activity.eticket.busticketNew.viewMode


import androidx.lifecycle.ViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTicketRepository.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport
import com.cloudwell.paywell.services.activity.eticket.busticketNew.transportSelect.view.IBusSeletedView

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
open class BusTicketBaseViewMode : ViewModel() {


    private lateinit var view: IBusSeletedView

    fun setView(view: IBusSeletedView) {
        this.view = view
    }


    fun getSchudle(internetConnection: Boolean, transport: Transport, uniqueKey: String) {
        if (!internetConnection) {
            view.showNoInternetConnectionFound()
        } else {
            view.showProgress()
            BusTicketRepository().getBusScheduleDate(transport.busid, uniqueKey).observeForever({ status ->
                view.hiddenProgress()
                if (status.equals("done")) {
                    view.openNextActivity()
                } else {
                    view.showErrorMessage(status)
                }
            })
        }


    }


}
