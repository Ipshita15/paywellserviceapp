package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel

import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
class BusTransportViewModel : BusTicketBaseViewMode() {

    var view: IbusTransportListView? = null

    fun setIbusTransportListView(ibusTransportListView: IbusTransportListView) {
        this.view = ibusTransportListView
    }

    fun callLocalSearch(requestBusSearch: RequestBusSearch) {
        view?.showProgress()
        BusTicketRepository().searchTransport(requestBusSearch).observeForever {
            it?.let { it1 ->
                if (it.size == 0) {
                    view?.showNoTripFoundUI()
                } else {
                    view?.setAdapter(it1)
                }
                view?.hiddenProgress()
            }

        }


    }

}