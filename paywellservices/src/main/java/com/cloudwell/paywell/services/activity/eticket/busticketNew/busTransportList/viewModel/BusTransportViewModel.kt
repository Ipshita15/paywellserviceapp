package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTicketRepository.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.model.SearchFilter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BoothInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.GetSeatViewRquestPojo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.Passenger
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.RequestScheduledata
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.SeatBlockRequestPojo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata.ScheduleDataItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.BordingPoint
import com.cloudwell.paywell.services.activity.eticket.busticketNew.viewMode.BusTicketBaseViewMode
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.Gson
import org.json.JSONObject
import java.text.SimpleDateFormat
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


    val departureScheduleData = mutableListOf<ScheduleDataItem>()


    fun setIbusTransportListView(ibusTransportListView: IbusTransportListView) {
        this.view = ibusTransportListView
    }


    fun cancelAllRequest() {
        if (ApiUtils.getClient() != null) {
            ApiUtils.getClient().dispatcher().cancelAll()
        }

    }


    fun onSort(filterPara: SearchFilter, retrunTriple: Boolean) {
        view?.showProgress()

        var list = ArrayList<ScheduleDataItem>()
        if (retrunTriple == true) {
            list = returnTripTransportListMutableLiveData.value!!
        } else {
            list = singleTripTranportListMutableLiveData.value!!
        }

        val filterType = list.filter {
            if (filterPara.coachType.equals("Ac")) {
                it.coachType.equals("Ac")
            } else if (filterPara.coachType.equals("NonAc")) {
                it.coachType.equals("NonAc")
            } else if (filterPara.coachType.equals("All")) {
                it.coachType.equals("Ac") || it.coachType.equals("NonAc")
            } else {
                it.coachType.equals("")
            }
        }


        val filterTypeDepartingTime = filterType.filter {
            if (filterPara.departingTime.equals("Morning")) {
                it.departingTime.equals("Morning")
            } else if (filterPara.departingTime.equals("AfterNoon")) {
                it.departingTime.equals("AfterNoon")
            } else if (filterPara.departingTime.equals("Evening")) {
                it.departingTime.equals("Evening")
            } else if (filterPara.departingTime.equals("Night")) {
                it.departingTime.equals("Night")
            } else if (filterPara.departingTime.equals("Any")) {
                it.departingTime.equals("Morning") || it.departingTime.equals("AfterNoon") || it.departingTime.equals("Evening") || it.departingTime.equals("Night")
            } else {
                it.coachType.equals("")
            }
        }

        Collections.sort(filterTypeDepartingTime) { item1: ScheduleDataItem, item2: ScheduleDataItem ->
            val a = item1.fares
            val b = item2.fares

            if (filterPara.sortBy.equals("Low Price")) {
                if (a > b) {
                    1
                } else if (a < b) {
                    -1
                } else {
                    0
                }
            } else {
                if (a > b) {
                    -1
                } else if (a < b) {
                    1
                } else {
                    0
                }
            }

        }

        view?.hiddenProgress()

        view?.showFilterList(filterTypeDepartingTime)


    }

    private fun getTime(departingTime: String): String {


        val sdf = SimpleDateFormat("yyyy-dd-MM strtotime")
        val date: Date = sdf.parse(departingTime)
        val cal = Calendar.getInstance()
        cal.time = date


        val c = Calendar.getInstance()
        val timeOfDay = c[Calendar.HOUR_OF_DAY]

        if (timeOfDay >= 4 && timeOfDay < 12) {
            return "Morning"
        } else if (timeOfDay >= 12 && timeOfDay < 6) {
            return "After Noon"
        } else if (timeOfDay >= 6 && timeOfDay < 8) {
            return "Evening"
        } else if (timeOfDay >= 8 && timeOfDay < 4) {
            return "Night"
        } else {
            return "All"
        }

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
                    singleTripTranportListMutableLiveData.value.let {
                        it?.let { it1 -> view?.showFilterList(it1) }
                    }

                    val reternScheduleData = jsonObject.getJSONObject("reternschedule")
                    if (reternScheduleData.getInt("status") == 1) {
                        val array = reternScheduleData.getJSONArray("scheduleData")
                        reternScheduleData.getString("scheduleData")

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

                            returnscheduleDataItemHashMap.put(key, item)
                            Log.e("Item: " + returnscheduleDataItemHashMap.keys, returnscheduleDataItemHashMap.get(key)?.routeName)
                        }

                        //

                        val retrunSortedByLowPricesList = returnscheduleDataItemHashMap.values.sortedBy {
                            it.fares
                        }

                        returnTripTransportListMutableLiveData.value = ArrayList(retrunSortedByLowPricesList)

                    }


                } else {
                    view?.showErrorMessage(message)

                }
            } else {
                view?.showErrorMessage(message)
            }
        }


    }

    fun callSeatBookAndConfirmAPI(pinNo: String, passenger: Passenger) {

        val m = seatBlockRequestPojo.value
        m?.password = pinNo
        m?.passenger = passenger
        m?.transportType = requestScheduledata.value?.transportType ?: "0"

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

    fun init(requestScheduledata: RequestScheduledata, isRetrunTriple: Boolean) {
        if (!isRetrunTriple) {
            if (singleTripTranportListMutableLiveData.value == null) {
                search(requestScheduledata, isRetrunTriple)
            } else {
                singleTripTranportListMutableLiveData.value.let {
                    it?.let { it1 -> view?.showFilterList(it1) }
                }
            }

        } else {
            if (returnTripTransportListMutableLiveData.value == null) {
                search(requestScheduledata, isRetrunTriple)
            } else {
                returnTripTransportListMutableLiveData.value.let {
                    it?.let { it1 -> view?.showFilterList(it1) }
                }
            }
        }

    }

    fun needToUpdateData(position: Int, model: ScheduleDataItem, retrunTriple: Boolean) {

        val userName = AppHandler.getmInstance(AppController.getContext()).userName

        val po = GetSeatViewRquestPojo()

        if (!retrunTriple) {
            po.fromCity = requestScheduledata.value?.departure ?: ""
            po.toCity = requestScheduledata.value?.destination ?: ""
            po.departureDate = "" + requestScheduledata.value?.departingDate

        } else {
            po.fromCity = requestScheduledata.value?.destination ?: ""
            po.toCity = requestScheduledata.value?.departure ?: ""
            po.departureDate = "" + requestScheduledata.value?.returnDate
        }

        po.optionId = model.busServiceType + "_" + model.departureId
        po.username = userName
        po.transportType = requestScheduledata.value?.transportType ?: ""


        BusTicketRepository().getSeatView(po).observeForever {

            if (it != null) {
                var itemCount = 0
                if (!retrunTriple) {
                    val m = singleTripTranportListMutableLiveData.value?.get(position)
                    m?.resSeatInfo = it
                    m?.let {
                        singleTripTranportListMutableLiveData.value?.set(position, it)
                        itemCount = singleTripTranportListMutableLiveData.value?.size ?: 0
                    }
                } else {
                    val m = returnTripTransportListMutableLiveData.value?.get(position)
                    m?.resSeatInfo = it
                    m?.let {
                        returnTripTransportListMutableLiveData.value?.set(position, it)
                        itemCount = returnTripTransportListMutableLiveData.value?.size ?: 0

                    }


                }
                view?.updateListData(position, itemCount)
            }


        }

    }
}