package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel

import com.cloudwell.paywell.services.activity.eticket.busticketNew.viewMode.BusTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTicketRepository.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.*
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.BusCalculationHelper
import com.google.gson.Gson
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
class BusTransportViewModel : BusTicketBaseViewMode() {

    private val allBoothInfo = mutableSetOf<BoothInfo>()
    private val allBoothNameInfo = mutableSetOf<String>()

    var view: IbusTransportListView? = null

    var mistOfSchedule = mutableListOf<TripScheduleInfoAndBusSchedule>()
    lateinit var mRequestBusSearch: RequestBusSearch
    lateinit var mTransport: Transport

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
                    val listOfSchudleData: List<TripScheduleInfoAndBusSchedule>

                    if (!requestBusSearch.boadingPoint.equals("All")) {
                        // filter of boadingpoint match data
                        var isFound = false
                        listOfSchudleData = it1.filter {
                            val jsonObject = JSONObject(it.busSchedule?.booth_departure_info)
                            jsonObject.keys().forEach {
                                val model = jsonObject.get(it) as JSONObject
                                val boothName = model.getString("booth_name")

                                if (boothName.equals(requestBusSearch.boadingPoint)) {
                                    isFound = true

                                } else {
                                    isFound = false
                                }
                            }
                            isFound

                        }

                    } else {
                        // all data
                        listOfSchudleData = it1
                    }

                    Collections.sort(listOfSchudleData) { car1, car2 ->
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

                    mistOfSchedule = listOfSchudleData.toMutableList()
                    mRequestBusSearch = requestBusSearch
                    mTransport = transport


                    view?.setAdapter(listOfSchudleData)
                }
                view?.hiddenProgress()
            }

        }
    }

    fun getBoardingPoint(requestBusSearch: RequestBusSearch, transport: Transport) {
        view?.showProgress()
        BusTicketRepository().searchTransport(requestBusSearch).observeForever {
            it?.let { it1 ->
                if (it1.size == 0) {
                    view?.showNoTripFoundUI()
                } else {
                    com.orhanobut.logger.Logger.json("" + Gson().toJson(it1))

                    allBoothInfo.clear()
                    allBoothNameInfo.clear()
                    allBoothNameInfo.add("All")
                    it1.forEach {


                        val jsonObject = JSONObject(it.busSchedule?.booth_departure_info)
                        jsonObject.keys().forEach {
                            val model = jsonObject.get(it) as JSONObject
                            val boothName = model.getString("booth_name")
                            val boothDepartureTime = model.getString("booth_departure_time")
                            allBoothInfo.add(BoothInfo(it, boothName, boothDepartureTime))
                            allBoothNameInfo.add(boothName)
                        }
                    }

                    view?.setBoardingPoint(allBoothNameInfo)
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
                    view?.showErrorMessage("Server error please try again later")
                } else {
                    if (it.status == 200 || it.status == 100) {
                        view?.showSeatCheckAndBookingRepose(it)
                    } else {
                        it.message.let { it1 -> view?.showErrorMessage(it.message) }
                    }
                }
            }
        }

    }

    fun callConfirmPayment(internetConnection: Boolean, transId: String, fullNameTV: String, mobileNumber: String, address: String, etEmail: String, age: String, gender: String, password: String) {
        if (!internetConnection) {
            view?.showNoInternetConnectionFound()
        } else {
            view?.showProgress()
            BusTicketRepository().confirmPaymentAPI(transId, fullNameTV, mobileNumber, address, etEmail, age, gender, password).observeForever {
                view?.hiddenProgress()
                if (it == null) {
                    view?.showErrorMessage("Server error please try again later")
                } else {
                    if (it.status == 200 || it.status == 100) {
                        view?.showShowConfirmDialog(it)
                    } else {
                        it.message.let { it1 -> view?.showErrorMessage(it.message) }
                    }
                }

            }
        }

    }

    fun onSort(lowPrice: SortType, list: List<TripScheduleInfoAndBusSchedule>) {
        view?.showProgress()
        if (lowPrice.name.equals(SortType.LOW_PRICE.name)) {
            Collections.sort(list) { car1, car2 ->
                val a = BusCalculationHelper.getPricesWithExtraAmount(car1.busSchedule?.ticketPrice, mRequestBusSearch.date, mTransport, true).toDouble()
                val b = BusCalculationHelper.getPricesWithExtraAmount(car2.busSchedule?.ticketPrice, mRequestBusSearch.date, mTransport, true).toDouble()

                if (a < b) {
                    -1
                } else if (a > b) {
                    1
                } else {
                    0
                }
            }

        } else if (lowPrice.name.equals(SortType.HIGHT_PRICE.name)) {
            Collections.sort(list) { car1, car2 ->
                val a = BusCalculationHelper.getPricesWithExtraAmount(car1.busSchedule?.ticketPrice, mRequestBusSearch.date, mTransport, true).toDouble()
                val b = BusCalculationHelper.getPricesWithExtraAmount(car2.busSchedule?.ticketPrice, mRequestBusSearch.date, mTransport, true).toDouble()

                if (a > b) {
                    -1
                } else if (a < b) {
                    1
                } else {
                    0
                }
            }

        } else if (lowPrice.name.equals(SortType.LOW_DEPARTURE_TIME.name)) {
            Collections.sort(list) { car1, car2 ->

                val a = SimpleDateFormat("HH:mm").parse(car1.busSchedule?.scheduleTime).time
                val b = SimpleDateFormat("HH:mm").parse(car2.busSchedule?.scheduleTime).time

                if (a > b) {
                    1
                } else if (a < b) {
                    -1
                } else {
                    0
                }
            }

        } else if (lowPrice.name.equals(SortType.HIGH_DEPARTURE_TIME.name)) {
            Collections.sort(list) { car1, car2 ->
                val a = SimpleDateFormat("HH:mm").parse(car1.busSchedule?.scheduleTime).time
                val b = SimpleDateFormat("HH:mm").parse(car2.busSchedule?.scheduleTime).time

                if (a > b) {
                    -1
                } else if (a < b) {
                    1
                } else {
                    0
                }
            }

        } else if (lowPrice.name.equals(SortType.LOW_AVAILABLE_SEAT.name)) {
            Collections.sort(list) { car1, car2 ->
                val a = car1.resSeatInfo?.tototalAvailableSeat ?: 0
                val b = car2.resSeatInfo?.tototalAvailableSeat ?: 0

                if (a > b) {
                    1
                } else if (a < b) {
                    -1
                } else {
                    0
                }
            }

        } else if (lowPrice.name.equals(SortType.HIGH_AVAILABLE_SEAT.name)) {
            Collections.sort(list) { car1, car2 ->
                val a = car1.resSeatInfo?.tototalAvailableSeat ?: 0
                val b = car2.resSeatInfo?.tototalAvailableSeat ?: 0

                if (a > b) {
                    -1
                } else if (a < b) {
                    1
                } else {
                    0
                }
            }

        }

        view?.setAdapter(list)
        view?.hiddenProgress()

    }
}