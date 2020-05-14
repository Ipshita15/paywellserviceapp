package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTicketRepository.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.model.SearchFilter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.*
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.Passenger
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.RequestScheduledata
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.SeatBlockRequestPojo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata.ScheduleDataItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.BordingPoint
import com.cloudwell.paywell.services.activity.eticket.busticketNew.viewMode.BusTicketBaseViewMode
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
class BusTransportViewModel : BusTicketBaseViewMode() {


    var extraCharge = MutableLiveData<Double>()

    val singleTripTranportListMutableLiveData: MutableLiveData<ArrayList<ScheduleDataItem>> = MutableLiveData<ArrayList<ScheduleDataItem>>()
    val returnTripTransportListMutableLiveData: MutableLiveData<ArrayList<ScheduleDataItem>> = MutableLiveData<ArrayList<ScheduleDataItem>>()

    val requestScheduledata: MutableLiveData<RequestScheduledata> = MutableLiveData<RequestScheduledata>()
    val seatBlockRequestPojo: MutableLiveData<SeatBlockRequestPojo> = MutableLiveData<SeatBlockRequestPojo>()


    val singleBordingPoint: MutableLiveData<BordingPoint> = MutableLiveData<BordingPoint>()
    val retrunBordingPoint: MutableLiveData<BordingPoint> = MutableLiveData<BordingPoint>()

    val singleScheduleDataItem: MutableLiveData<ScheduleDataItem> = MutableLiveData<ScheduleDataItem>()
    val retrunScheduleDataItem: MutableLiveData<ScheduleDataItem> = MutableLiveData<ScheduleDataItem>()


    val singleTotalAmount: MutableLiveData<Double> = MutableLiveData<Double>()
    val retrunTotalAmount: MutableLiveData<Double> = MutableLiveData<Double>()


    private val allBoothInfo = mutableSetOf<BoothInfo>()
    private val allBoothNameInfo = mutableSetOf<String>()

    var view: IbusTransportListView? = null


    val departureScheduleData = mutableListOf<ScheduleDataItem>();


    fun setIbusTransportListView(ibusTransportListView: IbusTransportListView) {
        this.view = ibusTransportListView
    }


    fun cancelAllRequest() {
        if (ApiUtils.getClient() != null) {
            ApiUtils.getClient().dispatcher().cancelAll()
        }


    }


    fun onSort(filterPara: SearchFilter, list: ArrayList<ScheduleDataItem>?) {
        view?.showProgress()

        val filterType = list?.filter {
            if (filterPara.coachType.equals("AC")) {
                it.coachType.equals("Ac")
            } else if (filterPara.coachType.equals("Non AC")) {
                it.coachType.equals("Non AC")
            } else {
                it.coachType.equals("")
            }
        }

        val filterTime = list?.filter {
            if (filterPara.coachType.equals("AC")) {
                it.coachType.equals("Ac")
            } else if (filterPara.coachType.equals("Non AC")) {
                it.coachType.equals("Non AC")
            } else {
                it.coachType.equals("")
            }
        }


        Collections.sort(filterTime) { item1: ScheduleDataItem, item2: ScheduleDataItem ->
            val a = item1.fares
            val b = item2.fares

            if (a > b) {
                -1
            } else if (a < b) {
                1
            } else {
                0
            }
        }

        view?.hiddenProgress()

    }

    fun search(p: RequestScheduledata, retrunTriple: Boolean) {

        requestScheduledata.value = p

        view?.showProgress()
        BusTicketRepository().getScheduleData(p).observeForever {
            view?.hiddenProgress()

            val jsonObject = JSONObject(it)
            val status = jsonObject.getInt("status_code")
            val message = jsonObject.getString("message")

            val scheduleDataItemHashMap = HashMap<String, ScheduleDataItem>()
            val returnscheduleDataItemHashMap = HashMap<String, ScheduleDataItem>()

            if (status == 200) {
                //departureScheduleData Data
                val departureScheduleData = jsonObject.getJSONObject("departureScheduleData")
                if (departureScheduleData.getInt("status") == 1) {
                    val array = departureScheduleData.getJSONArray("scheduleData")
                    departureScheduleData.getString("scheduleData")

                    val scheduleData_array: JSONObject = array.optJSONObject(0)
                    val keys: Iterator<*> = scheduleData_array.keys()
                    while (keys.hasNext()) {
                        val key = keys.next() as String
                        val itemString: String = scheduleData_array.get(key).toString()
                        val gson = Gson()
                        val item: ScheduleDataItem = gson.fromJson(itemString, ScheduleDataItem::class.java)

                        val jsonObject = JSONObject(itemString).getJSONObject("fares")
                        for (key in jsonObject.keys()) {
                            val prices = jsonObject.getString(key)
                            item.fares = prices.toDouble()
                            item.seatTypes = key

                        }

                        scheduleDataItemHashMap.put(key, item)
                        Log.e("Item: " + scheduleDataItemHashMap.keys, scheduleDataItemHashMap.get(key)?.routeName)
                    }

                    //

                    val sortedByLowPricesList = scheduleDataItemHashMap.values.sortedBy {
                        it.fares
                    }

                    extraCharge.value = jsonObject.getDouble("extraCharge")
                    singleTripTranportListMutableLiveData.value = ArrayList(sortedByLowPricesList)


//                        val get = sortedByLowPricesList.get(sortedByLowPricesList.size - 1)
//                        val tempdata  = mutableListOf<ScheduleDataItem>()
//                        tempdata.add(get)
//
//                        //returnTripTransportListMutableLiveData.value = ArrayList(tempdata)


                    //return ScheduleData data

                    val departureScheduleData = jsonObject.getJSONObject("reternschedule")
                    if (departureScheduleData.getInt("status") == 1) {
                        val array = departureScheduleData.getJSONArray("scheduleData")
                        departureScheduleData.getString("scheduleData")

                        val scheduleData_array: JSONObject = array.optJSONObject(0)
                        val keys: Iterator<*> = scheduleData_array.keys()
                        while (keys.hasNext()) {
                            val key = keys.next() as String
                            val itemString: String = scheduleData_array.get(key).toString()
                            val gson = Gson()
                            val item: ScheduleDataItem = gson.fromJson(itemString, ScheduleDataItem::class.java)

                            val jsonObject = JSONObject(itemString).getJSONObject("fares")
                            for (key in jsonObject.keys()) {
                                val prices = jsonObject.getString(key)
                                item.fares = prices.toDouble()
                                item.seatTypes = key

                            }

                            scheduleDataItemHashMap.put(key, item)
                            Log.e("Item: " + scheduleDataItemHashMap.keys, scheduleDataItemHashMap.get(key)?.routeName)
                        }

                        //

                        val sortedByLowPricesList = scheduleDataItemHashMap.values.sortedBy {
                            it.fares
                        }

                        //extraCharge.value = jsonObject.getDouble("extraCharge")
                        returnTripTransportListMutableLiveData.value = ArrayList(sortedByLowPricesList)

                    }


                } else {
                    view?.showErrorMessage(message)

                }
            }
        }


    }

    fun callSeatBookAndConfirmAPI(pinNo: String, passenger: Passenger) {
        val m = seatBlockRequestPojo.value
        m?.password = pinNo ?: ""
        m?.passenger = passenger

        view?.showProgress()
        BusTicketRepository().getseatBlock(m!!).observeForever {
            view?.hiddenProgress()
            if (it == null) {
                view?.showErrorMessage("Server error please try again later")
            } else {

                if (it.statusCode != 200L) {
                    it.message?.let { it1 -> view?.showErrorMessage(it1) }
                } else {
                    view?.showShowConfirmDialog(it)
                }

            }
        }


    }
}