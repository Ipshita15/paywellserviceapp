package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel

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
                        val a = BusCalculationHelper.getPricesWithExtraAmount(car1.busSchedule?.ticketPrice, requestBusSearch.date, transport, true).toDouble()
                        val b = BusCalculationHelper.getPricesWithExtraAmount(car2.busSchedule?.ticketPrice, requestBusSearch.date, transport, true).toDouble()

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
            ApiUtils.getClient().dispatcher().cancelAll()
        }


    }

    fun bookingAPI(internetConnection: Boolean, model: TripScheduleInfoAndBusSchedule, requestBusSearch: RequestBusSearch, boothInfo: BoothInfo, seatLevel: String, seatId: String, totalAPIValuePrices: String) {
        if (!internetConnection) {
            view?.showNoInternetConnectionFound()
        } else {

            view?.showProgress()
            BusTicketRepository().callBookingAPI(model, requestBusSearch, boothInfo, seatLevel, seatId, totalAPIValuePrices).observeForever {
                view?.hiddenProgress()
                if (it == null) {
                    view?.showErrorMessage("message")
                } else {
                    if (it.status == 200 || it.status == 100) {
                        view?.showSeatCheckAndBookingRepose(it)
                    } else {
                        it.meassage.let { it1 -> view?.showErrorMessage(it.meassage) }
                    }
                }
            }
        }

    }

    fun callConfirmPayment(internetConnection: Boolean, transId: String, fullNameTV: String, mobileNumber: String, address: String, etEmail: String, age: String, password: String) {
        if (!internetConnection) {
            view?.showNoInternetConnectionFound()
        } else {
            view?.showProgress()
            BusTicketRepository().confirmPaymentAPI(transId, fullNameTV, mobileNumber, address, etEmail, age, password).observeForever {
                if (it == null) {
                    view?.showErrorMessage("message")
                } else {
                    if (it.status == 200 || it.status == 100) {
                        view?.showShowConfirmDialog(it)
                    } else {
                        it.meassage.let { it1 -> view?.showErrorMessage(it.meassage) }
                    }
                }

            }
        }

    }
}