package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel

import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.Gson

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
                if (it1.size == 0) {
                    view?.showNoTripFoundUI()
                } else {
                    com.orhanobut.logger.Logger.json("" + Gson().toJson(it1))
                    view?.setAdapter(it1)
                }
                view?.hiddenProgress()
            }

        }


    }

    fun cancelAllRequest() {
        if (ApiUtils.getClient() != null) {
            ApiUtils.getClient().dispatcher.cancelAll()
        }


    }

}