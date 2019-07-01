package com.cloudwell.paywell.services.activity.eticket.busticketNew

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.*
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.orhanobut.logger.Logger
import okhttp3.ResponseBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class BusTicketRepository() {

    private var mAppHandler: AppHandler? = null
    private var mContext: Context? = null

    init {
        mContext = AppController.getContext()
    }

    val isFinshedDataLoad = MutableLiveData<Boolean>()


    fun getBusList(): MutableLiveData<List<Bus>> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.imeiNo
        val skey = ApiUtils.KEY_SKEY

        val data = MutableLiveData<List<Bus>>()

        ApiUtils.getAPIServicePHP7().getBusListData(userName, skey).enqueue(object : Callback<ResGetBusListData> {
            override fun onResponse(call: Call<ResGetBusListData>, response: Response<ResGetBusListData>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body.let {

                        if (it?.status ?: 0 == 200) {
                            val accessKey = it?.accessKey
                            AppStorageBox.put(mContext, AppStorageBox.Key.ACCESS_KEY, accessKey)

                            it?.data?.data?.let { it1 ->
                                saveBuss(it1)
                                data.value = it1
                            }
                        }

                    }

                    com.orhanobut.logger.Logger.v("" + body)
                }
            }

            override fun onFailure(call: Call<ResGetBusListData>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }


    public fun getBusScheduleDate(transport_id: String): MutableLiveData<Boolean> {
        mAppHandler = AppHandler.getmInstance(mContext)

        val userName = mAppHandler!!.imeiNo
        val skey = ApiUtils.KEY_SKEY

        val accessKey = AppStorageBox.get(mContext, AppStorageBox.Key.ACCESS_KEY) as String
        ApiUtils.getAPIServicePHP7().getBusSchedule(userName, transport_id, skey, accessKey).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body.let {
                        val jsonObject = JSONObject(it?.string())
                        if (jsonObject.getInt("status") == 200) {
                            handleResponse(jsonObject)
                        } else {
                            isFinshedDataLoad.value = false
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                isFinshedDataLoad.value = false

            }
        })
        return isFinshedDataLoad
    }

    private fun handleResponse(jsonObject: JSONObject) {

        doAsync {
            val inseredIds = DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().clearLocalBusDB()
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().clearSchedule()
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().clearTripScheduleInfo()
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().clearBoothInfo()

            uiThread {
                insertBusScullerData(jsonObject)
            }
        }


    }


    private fun insertBusScullerData(jsonObject: JSONObject) {
        val dataObject = jsonObject.getJSONObject("data")
        val busInfoObject = dataObject.getJSONObject("bus_info")
        val scheduleInfoObject = dataObject.getJSONObject("schedule_info")

        val keys = busInfoObject.keys()

        val allData = mutableListOf<BusLocalDB>()

        keys.forEach {
            val busId = it

            val name = busInfoObject.getJSONObject(busId).getString("name")
            val structure_type = busInfoObject.getJSONObject(busId).getString("structure_type")
            val total_columns = busInfoObject.getJSONObject(busId).getString("total_columns")
            val total_rows = busInfoObject.getJSONObject(busId).getString("total_rows")
            val total_seats = busInfoObject.getJSONObject(busId).getString("total_seats")
            val columns_in_right = busInfoObject.getJSONObject(busId).getString("columns_in_right")
            val seat_structure = busInfoObject.getJSONObject(busId).getString("seat_structure")
            val bus_is_ac = busInfoObject.getJSONObject(busId).getString("bus_is_ac")
            val bus_col_in_middle = busInfoObject.getJSONObject(busId).getString("bus_col_in_middle")
            val empty_rows_in_left = busInfoObject.getJSONObject(busId).getString("empty_rows_in_left")
            val empty_rows_in_middle = busInfoObject.getJSONObject(busId).getString("empty_rows_in_middle")
            val empty_rows_in_right = busInfoObject.getJSONObject(busId).getString("empty_rows_in_right")

            val busLocalDB = BusLocalDB()
            busLocalDB.busID = "" + busId
            busLocalDB.name = "" + name
            busLocalDB.structureType = "" + structure_type
            busLocalDB.totalColumns = "" + total_columns
            busLocalDB.totalRows = "" + total_rows
            busLocalDB.columnsInRight = "" + columns_in_right
            busLocalDB.seatStructure = "" + seat_structure
            busLocalDB.busIsAc = "" + bus_is_ac
            busLocalDB.busColInMiddle = "" + bus_col_in_middle
            busLocalDB.emptyRowsInLeft = "" + empty_rows_in_left
            busLocalDB.emptyRowsInMiddle = "" + empty_rows_in_middle
            busLocalDB.emptyRowsInRight = "" + empty_rows_in_right
            busLocalDB.totalSeats = "" + total_seats

            allData.add(busLocalDB)

        }

        val data = MutableLiveData<LongArray>()

        doAsync {

            val inseredIds = DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().insertLocalBus(allData)

            uiThread {
                data.value = inseredIds
            }
        }


//////////////////////////////////////////////////////////////////////////////////////////////


        val allScheduleData = mutableListOf<BusSchedule>()
        val allTripScheduleInfo = mutableListOf<TripScheduleInfo>()
        val allBoothInfo = mutableListOf<BoothInfo>()


        val keys1 = scheduleInfoObject.keys()
        keys1.forEach {
            val FormKey = it
            val to = scheduleInfoObject.getJSONObject(FormKey)

            val fromKeys = to.keys()

            fromKeys.forEach {
                val toKey = it
                val schedules = to.getJSONObject(toKey).getJSONObject("schedules")

                schedules.keys().forEach {
                    val scheduleId = it


                    val model = schedules.getJSONObject(scheduleId)
                    val schedule_time = model.getString("schedule_time")
                    val bus_id = model.getString("bus_id")
                    val coach_no = model.getString("coach_no")
                    val schedule_type = model.getString("schedule_type")
                    val validity_date = model.getString("validity_date")

                    val tripScheduleInfo = TripScheduleInfo(FormKey, toKey, scheduleId, validity_date)
                    allTripScheduleInfo.add(tripScheduleInfo)


                    val priceObject = model.getJSONObject("ticket_price")

                    var ticket_price = ""
                    val dateKey = priceObject.keys()
                    dateKey.forEach {
                        ticket_price = "" + priceObject.get(it)
                    }

                    var allowedSeatStoreString = ""
                    val allowedSeatNumbersObject = model.getJSONObject("allowed_seat_numbers")
                    allowedSeatStoreString = allowedSeatNumbersObject.toString()


                    val boothDepartureInfo = model.getJSONObject("booth_departure_info")
                    boothDepartureInfo.keys().forEach {
                        val boothId = it
                        val boothObject = boothDepartureInfo.get(it) as JSONObject
                        val boothName = boothObject.getString("booth_name")
                        val boothDepartureTime = boothObject.getString("booth_departure_time")

                        allBoothInfo.add(BoothInfo(boothId, scheduleId, boothName, boothDepartureTime))
                    }


                    val schedule = BusSchedule(scheduleId, schedule_time, bus_id, coach_no, schedule_type, validity_date, ticket_price, dateKey.toString(), allowedSeatStoreString)
                    allScheduleData.add(schedule)
                }

            }

        }


        doAsync {
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().insertTripScheduleInfo(allTripScheduleInfo)
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().insertSchedule(allScheduleData)
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().insertBoothInfo(allBoothInfo)

            Logger.v("doAsync")

            uiThread {
                Logger.v("Local data insert successful")
                isFinshedDataLoad.value = true
            }
        }


    }


    fun saveBuss(Buss: List<Bus>): MutableLiveData<LongArray> {


        val data = MutableLiveData<LongArray>()
        doAsync {

            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().clearBus()
            val inseredIds = DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().insert(Buss)

            uiThread {
                data.value = inseredIds
            }
        }

        return data

    }

    fun getSeatCheck(transport_id: String, route: String, bus_id: String, departure_id: String, departure_date: String, seat_ids: String): MutableLiveData<ResSeatInfo> {

        mAppHandler = AppHandler.getmInstance(mContext)

        val userName = mAppHandler!!.imeiNo
        val skey = ApiUtils.KEY_SKEY

        val accessKey = AppStorageBox.get(mContext, AppStorageBox.Key.ACCESS_KEY) as String

        val data = MutableLiveData<ResSeatInfo>()

        ApiUtils.getAPIServicePHP7().seatCheck(
                userName,
                skey,
                accessKey,
                transport_id,
                route,
                bus_id,
                departure_id,
                departure_date,
                seat_ids
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body.let {
                        val allBusSeat = mutableListOf<BusSeat>()
                        var tototalAvailableSeat = 0
                        val jsonObject = JSONObject(it?.string())
                        if (jsonObject.getInt("status") == 200) {
                            val seatInfo = jsonObject.getJSONObject("seatInfo")
                            val keys = seatInfo.keys()
                            keys.forEach {
                                val o = seatInfo.get(it) as JSONObject
                                if (o.getString("status").equals("Available")) {
                                    tototalAvailableSeat = tototalAvailableSeat + 1
                                }
                                allBusSeat.add(BusSeat(o.getString("seat_lbls"), o.getString("status"), o.getInt("value")))
                            }
                            data.value = ResSeatInfo(tototalAvailableSeat, allBusSeat)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
            }
        })
        return data


    }

    private fun handleResponseseatCheck(it1: String) {
        Logger.v("", "")

    }

    fun searchTransport(requestBusSearch: RequestBusSearch): MutableLiveData<List<TripScheduleInfoAndBusSchedule>> {

        val data = MutableLiveData<List<TripScheduleInfoAndBusSchedule>>()

        doAsync {
            val search = DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().search(requestBusSearch.to, requestBusSearch.from, requestBusSearch.date)
            uiThread {
                data.value = search
            }
        }
        return data

    }


//    fun loadAPIData() {
//
//        mAppHandler = AppHandler.getmInstance(mContext)
//        val userName = mAppHandler!!.imeiNo
//        val skey = ApiUtils.KEY_SKEY
//        var accessKey = AppStorageBox.get(mContext, AppStorageBox.Key.ACCESS_KEY) as? String
//        if (accessKey == null) {
//            accessKey = ""
//        }
//
//        val busListDataObservable = ApiUtils.getAPIServicePHP7().getBusListData(userName, skey)
//        val busScheduleObservable = ApiUtils.getAPIServicePHP7().getBusSchedule(userName, "37", skey, accessKey)
//
//        Observable.zip(busListDataObservable.subscribeOn(Schedulers.io()), busScheduleObservable.subscribeOn(Schedulers.io()), BiFunction<ResGetBusListData, ResponseBody, BusLoadData> { resGetBusListData: ResGetBusListData, responseBody: ResponseBody ->
//
//
//            BusLoadData(resGetBusListData, responseBody)
//
//        }).subscribe(object : Observer<BusLoadData> {
//            override fun onComplete() {
//
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//
//            }
//
//            override fun onNext(t: BusLoadData) {
//                if (t.responseBody.string().equals("")) {
//                    loadAPIData()
//                } else {
//                    val accessKey = t.resGetBusListData.accessKey
//                    AppStorageBox.put(mContext, AppStorageBox.Key.ACCESS_KEY, accessKey)
//                    BusTicketRepository(mContext).saveBuss(t.resGetBusListData.data.data)
//                    handleResponse(t.responseBody.string())
//                }
//            }
//
//            override fun onError(e: Throwable) {
//
//            }
//
//        })
//
//    }


}