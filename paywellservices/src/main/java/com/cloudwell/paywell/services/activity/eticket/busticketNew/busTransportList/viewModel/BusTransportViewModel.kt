package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel

import android.util.Log
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BoothInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.BusCalculationHelper
import com.google.gson.Gson
import java.util.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
class BusTransportViewModel : BusTicketBaseViewMode() {

    var view: IbusTransportListView? = null

    fun setIbusTransportListView(ibusTransportListView: IbusTransportListView) {
        this.view = ibusTransportListView
    }

    fun callLocalSearch(requestBusSearch: RequestBusSearch, transport: Transport) {
        view?.showProgress()
        BusTicketRepository().searchTransport(requestBusSearch).observeForever {
            it?.let { it1 ->
                if (it1.size == 0) {
                    view?.showNoTripFoundUI()
                } else {
                    com.orhanobut.logger.Logger.json("" + Gson().toJson(it1))

                    Collections.sort(it1) { car1, car2 ->
                        val a = BusCalculationHelper.getPrices(car1.busSchedule?.ticketPrice, requestBusSearch.date, transport).toDouble()
                        val b = BusCalculationHelper.getPrices(car2.busSchedule?.ticketPrice, requestBusSearch.date, transport).toDouble()

                        if (a < b) {
                            -1
                        } else if (a > b) {
                            1
                        } else {
                            0
                        }

                    }

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

    fun seatCheck(internetConnection: Boolean, model: TripScheduleInfoAndBusSchedule, requestBusSearch: RequestBusSearch, boothInfo: BoothInfo, seatLevel: String, seatId: String) {
        if (!internetConnection) {
            view?.showNoInternetConnectionFound()
        } else {
            BusTicketRepository().callBookingAPI(model, requestBusSearch, boothInfo, seatLevel, seatId).observeForever {

                Log.e("", "")
            }
        }

    }
}