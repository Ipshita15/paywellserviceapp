package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTicketRepository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.*
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.*
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.BordingPoint
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.SeatstructureDetailsItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm.ReqConfirmTicket
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm.ResBookAPI
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm.ResposeTicketConfirm
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm_cancel.ConfirmTicketCancelResponse
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
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
class BusTicketRepository {

    private var mAppHandler: AppHandler? = null
    private var mContext: Context? = null
    private var mValidityDate = ""

    init {
        mContext = AppController.getContext()
    }

    val statusOfDateInserted = SingleLiveEvent<String>()


    var resposeTicketConfirm = MutableLiveData<ResposeTicketConfirm>()


    fun getBusList(uniqueKey: String): MutableLiveData<List<Transport>> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.userName
        val skey = ApiUtils.KEY_SKEY

        val data = MutableLiveData<List<Transport>>()

        ApiUtils.getAPIServicePHP7().getBusListData(userName, skey, uniqueKey).enqueue(object : Callback<ResGetBusListData> {
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
                } else {
                    data.value = null
                }
            }

            override fun onFailure(call: Call<ResGetBusListData>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }


    fun getBusScheduleDate(transport_id: String, uniqueKey: String): MutableLiveData<String> {
        mAppHandler = AppHandler.getmInstance(mContext)

        val userName = mAppHandler!!.userName
        val skey = ApiUtils.KEY_SKEY

        val accessKey = AppStorageBox.get(mContext, AppStorageBox.Key.ACCESS_KEY) as String
        ApiUtils.getAPIServicePHP7().getBusSchedule(userName, transport_id, skey, accessKey, uniqueKey).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body.let {
                        val jsonObject = JSONObject(it?.string())
                        if (jsonObject.getInt("status") == 200) {
                            handleResponse(jsonObject)
                        } else {
                            statusOfDateInserted.postValue(jsonObject.getString("message"))


                        }

                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                statusOfDateInserted.postValue(mContext?.getString(R.string.network_error))

            }
        })
        return statusOfDateInserted
    }

    private fun handleResponse(jsonObject: JSONObject) {

        doAsync {
            val inseredIds = DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().clearLocalBusDB()
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().clearSchedule()
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().clearTripScheduleInfo()
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
                    mValidityDate = validity_date


                    val tripScheduleInfo = TripScheduleInfo(FormKey, toKey, scheduleId, validity_date)
                    allTripScheduleInfo.add(tripScheduleInfo)


                    val priceObject = model.getJSONObject("ticket_price")
                    val ticket_price = priceObject.toString()


                    var allowedSeatStoreString = ""
                    val allowedSeatNumbersObject = model.getJSONObject("allowed_seat_numbers")
                    allowedSeatStoreString = allowedSeatNumbersObject.toString()


                    val boothDepartureInfo = model.getJSONObject("booth_departure_info")

                    val schedule = BusSchedule(scheduleId, schedule_time, bus_id, coach_no, schedule_type, validity_date, ticket_price, allowedSeatStoreString, boothDepartureInfo.toString())
                    allScheduleData.add(schedule)
                }

            }

        }

        AppStorageBox.put(mContext, AppStorageBox.Key.BUS_VALIDATE_DATE, mValidityDate)

        doAsync {
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().insertTripScheduleInfo(allTripScheduleInfo)
            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().insertSchedule(allScheduleData)

            Logger.v("doAsync")

            uiThread {
                Logger.v("Local data insert successful")
                statusOfDateInserted.postValue("done")
            }
        }


    }


    fun saveBuss(busses: List<Transport>): MutableLiveData<LongArray> {


        val data = MutableLiveData<LongArray>()
        doAsync {

            DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().clearBus()
            val inseredIds = DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().insert(busses)

            uiThread {
                data.value = inseredIds
            }
        }

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

    fun callBookingAPI(model: TripScheduleInfoAndBusSchedule, requestBusSearch: RequestBusSearch, boothInfo: BoothInfo, seatLevel: String, seatId: String, totalAPIValuePrices: String, uniqueKey: String): MutableLiveData<ResSeatCheckBookAPI> {

        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.userName
        val skey = ApiUtils.KEY_SKEY
        val accessKey = AppStorageBox.get(mContext, AppStorageBox.Key.ACCESS_KEY) as String

        val transport = AppStorageBox.get(mContext, AppStorageBox.Key.SELETED_BUS_INFO) as Transport
        val data = MutableLiveData<ResSeatCheckBookAPI>()

//        ApiUtils.getAPIServicePHP7().seatCheckAndBlock(
//                userName,
//                skey,
//                accessKey,
//                (AppStorageBox.get(mContext, AppStorageBox.Key.SELETED_BUS_INFO) as Transport).busid,
//                (AppStorageBox.get(mContext, AppStorageBox.Key.SELETED_BUS_INFO) as Transport).busname,
//                requestBusSearch.from + "-" + requestBusSearch.to,
//                model.busLocalDB?.busID,
//                model.busLocalDB?.name,
//                model.busSchedule?.coachNo,
//                model.busSchedule?.schedule_time_id,
//                requestBusSearch.date,
//                boothInfo.boothDepartureTime,
//                boothInfo.boothID,
//                boothInfo.boothName,
//                seatId,
//                seatLevel,
//                model.busLocalDB?.busIsAc,
//                transport.extraCharge,
//                BusCalculationHelper.getPricesWithExtraAmount(model.busSchedule?.ticketPrice, requestBusSearch.date, transport = transport, isExtraAmount = false),
//                totalAPIValuePrices,uniqueKey)
//                .enqueue(object : Callback<ResSeatCheckBookAPI> {
//                    override fun onFailure(call: Call<ResSeatCheckBookAPI>, t: Throwable) {
//                        data.value = null
//                    }
//
//                    override fun onResponse(call: Call<ResSeatCheckBookAPI>, response: Response<ResSeatCheckBookAPI>) {
//                        if (response.isSuccessful) {
//                            data.value = response.body()
//                        }
//                    }
//                })


        return data

    }

    fun confirmPaymentAPI(transId: String, fullNameTV: String, mobileNumber: String, address: String, etEmail: String, age: String, gender: String, password: String): MutableLiveData<ResPaymentBookingAPI> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.userName
        val skey = ApiUtils.KEY_SKEY
        val accessKey = AppStorageBox.get(mContext, AppStorageBox.Key.ACCESS_KEY) as String

        val data = MutableLiveData<ResPaymentBookingAPI>()
        val uniqueKey = UniqueKeyGenerator.getUniqueKey(mAppHandler!!.rid)
        ApiUtils.getAPIServicePHP7().confirmPayment(
                userName,
                skey,
                accessKey,
                transId,
                fullNameTV,
                mobileNumber,
                address,
                etEmail,
                age,
                gender,
                password, uniqueKey).enqueue(object : Callback<ResPaymentBookingAPI> {
            override fun onFailure(call: Call<ResPaymentBookingAPI>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<ResPaymentBookingAPI>, response: Response<ResPaymentBookingAPI>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                }
            }
        })

//        ApiUtils.getAPIServicePHP7().confirmPayment(
//                "https://enr1gk8z5p91.x.pipedream.net",
//                userName,
//                skey,
//                accessKey,
//                transId,
//                fullNameTV,
//                mobileNumber,
//                address,
//                etEmail,
//                age,
//                gender,
//                password).enqueue(object : Callback<ResPaymentBookingAPI> {
//            override fun onFailure(call: Call<ResPaymentBookingAPI>, t: Throwable) {
//                data.value = null
//            }
//
//            override fun onResponse(call: Call<ResPaymentBookingAPI>, response: Response<ResPaymentBookingAPI>) {
//                if (response.isSuccessful) {
//                    data.value = response.body()
//                }
//            }
//        })


        return data
    }


    //New Version Api call

    fun getbusAndLaunchCities(busLunCityPojo: BusLunCityRequest): SingleLiveEvent<List<CitiesListItem>> {

        mAppHandler = AppHandler.getmInstance(mContext)

        val data = SingleLiveEvent<List<CitiesListItem>>()

        ApiUtils.getAPIServiceV2().getbusAndLaunchCities(busLunCityPojo).enqueue(object : Callback<BusLunCityResponse> {
            override fun onResponse(call: Call<BusLunCityResponse>, response: Response<BusLunCityResponse>) {
                if (response.isSuccessful) {

                    val request_response = response.body()

                    if (request_response?.statusCode == 200) {
                        val citiesList: List<CitiesListItem>? = request_response.citiesList
                        data.postValue(citiesList!!)
                    }

                }
            }

            override fun onFailure(call: Call<BusLunCityResponse>, t: Throwable) {

                data.postValue(null)

            }
        })
        return data

    }


    fun getScheduleData(schedulePojo: RequestScheduledata): MutableLiveData<String> {

        mAppHandler = AppHandler.getmInstance(mContext)

        val userName = mAppHandler!!.userName
        val data = MutableLiveData<String>()

        ApiUtils.getAPIServiceV2().getScheduleData(schedulePojo).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    data.value = response.body()?.string()

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                data.postValue(mContext?.getString(R.string.network_error))

            }
        })
        return data

    }

    fun getSeatView(schedulePojo: GetSeatViewRquestPojo): MutableLiveData<ResSeatInfo> {

        mAppHandler = AppHandler.getmInstance(mContext)

        val userName = mAppHandler!!.userName
        val data = MutableLiveData<ResSeatInfo>()

        ApiUtils.getAPIServiceV2().getSeatView(schedulePojo).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val rootObject: JSONObject = JSONObject(response.body()?.string())
                    var tototalAvailableSeat = 0

                    if (rootObject.getInt("status_code") == 200) {
                        val seatViewDataObject = rootObject.getJSONObject("seatViewData")

                        // parse BordingPoint
                        var bordingPoints = ArrayList<BordingPoint>()
                        val jsonArray = seatViewDataObject.getJSONArray("bording_points")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            var m = BordingPoint()
                            m.counterName = jsonObject.getString("counter_name")
                            m.reportingBranchId = jsonObject.getInt("reporting_branch_id")
                            m.scheduleTime = jsonObject.getString("schedule_time")
                            bordingPoints.add(m)
                        }


                        // parse SeatstructureDetails
                        var allBusSeat = ArrayList<SeatstructureDetailsItem>()
                        var seatsPattenStr = ""
                        seatsPattenStr = seatsPattenStr + "/"


                        val detailsJsonObj = seatViewDataObject.getJSONObject("seatstructure_details") as JSONObject
                        detailsJsonObj.keys().forEach {
                            val columeObject = detailsJsonObj.getJSONObject(it)
                            columeObject.keys().forEach {
                                val seatObject = columeObject.getJSONObject(it)
                                val m = SeatstructureDetailsItem()
                                m.fare = seatObject.getDouble("fare") ?: 0.0
                                m.yAxis = seatObject.getInt("y_axis")
                                m.xAxis = seatObject.getInt("x_axis")
                                m.seatNo = seatObject.getString("seat_no")
                                m.status = seatObject.getString("status")
                                m.seatid = seatObject.getInt("seatid")

                                val status = seatObject.getString("status")

                                if (status.equals("Available")) {
                                    tototalAvailableSeat = tototalAvailableSeat + 1
                                    seatsPattenStr = seatsPattenStr + "A"
                                    allBusSeat.add(m)

                                } else if (status.equals("Booked")) {
                                    seatsPattenStr = seatsPattenStr + "R"
                                    allBusSeat.add(m)
                                } else if (status.equals("Passage")) {
                                    seatsPattenStr = seatsPattenStr + "_"
                                } else {
                                    seatsPattenStr = seatsPattenStr + "U"
                                    allBusSeat.add(m)
                                }
                            }

                            seatsPattenStr = seatsPattenStr + "/"
                        }
                        data.value = ResSeatInfo(tototalAvailableSeat, seatsPattenStr, allBusSeat, bordingPoints)
                    }


                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                data.postValue(null)

            }
        })
        return data

    }

//    fun getSeatCheck(transport_id: String, route: String, bus_id: String, departure_id: String, departure_date: String, seat_ids: String): MutableLiveData<ResSeatInfo> {
//
//        mAppHandler = AppHandler.getmInstance(mContext)
//
//        val userName = mAppHandler!!.userName
//        val skey = ApiUtils.KEY_SKEY
//
//        val accessKey = AppStorageBox.get(mContext, AppStorageBox.Key.ACCESS_KEY) as String
//        val uniqueKey = UniqueKeyGenerator.getUniqueKey(mAppHandler!!.rid)
//
//        val data = MutableLiveData<ResSeatInfo>()
//
//        ApiUtils.getAPIServicePHP7().seatCheck(
//                userName,
//                skey,
//                accessKey,
//                transport_id,
//                route,
//                bus_id,
//                departure_id,
//                departure_date,
//                seat_ids,uniqueKey
//
//        ).enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//
//
//                    val body = response.body()
//                    body.let {
//                        val allBusSeat = mutableListOf<BusSeat>()
//                        var tototalAvailableSeat = 0
//                        val jsonObject = JSONObject(it?.string())
//                        if (jsonObject.getInt("status") == 200) {
//                            val seatInfo = jsonObject.getJSONObject("seatInfo")
//                            val keys = seatInfo.keys()
//                            keys.forEach {
//                                val o = seatInfo.get(it) as JSONObject
//                                if (o.getString("status").equals("Available")) {
//                                    tototalAvailableSeat = tototalAvailableSeat + 1
//                                }
//                                allBusSeat.add(BusSeat(o.getInt("seat_id"), o.getString("seat_lbls"), o.getString("status"), o.getInt("value")))
//                            }
//                            data.value = ResSeatInfo(tototalAvailableSeat, allBusSeat)
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                com.orhanobut.logger.Logger.e("" + t.message)
//            }
//        })
//        return data
//
//
//    }


    fun getSeatStatus(getSeatViewRquestPojo: GetSeatViewRquestPojo): MutableLiveData<ResSeatInfo> {

        mAppHandler = AppHandler.getmInstance(mContext)

        val data = MutableLiveData<ResSeatInfo>()

        ApiUtils.getAPIServiceV2().getSeatStatus(getSeatViewRquestPojo).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
//
//                    val request_response = response.body()
//
//                    if (request_response?.statusCode == 200){
//                        val citiesList: List<CitiesListItem?>? = request_response?.citiesList
//                        data.postValue(citiesList.toString())
//                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                data.postValue(null)

            }
        })
        return data

    }

    fun getseatBlock(schedulePojo: SeatBlockRequestPojo): MutableLiveData<ResposeTicketConfirm> {

        ApiUtils.getAPIServiceV2().seatBlock(schedulePojo).enqueue(object : Callback<ResBookAPI> {
            override fun onResponse(call: Call<ResBookAPI>, response: Response<ResBookAPI>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode.equals("100")) {
                        confirmTicket(body?.trxId, schedulePojo)
                    } else {
                        val resposeTicketConfirm1 = ResposeTicketConfirm()
                        resposeTicketConfirm1.message = body?.message
                        resposeTicketConfirm1.statusCode = body?.statusCode?.toLong()
                        resposeTicketConfirm.value = resposeTicketConfirm1
                    }
                }
            }

            override fun onFailure(call: Call<ResBookAPI>, t: Throwable) {
                resposeTicketConfirm.postValue(null)

            }
        })
        return resposeTicketConfirm

    }

    private fun confirmTicket(trxId: String?, schedulePojo: SeatBlockRequestPojo) {
        val m = ReqConfirmTicket()
        m.password = schedulePojo.password
        m.deviceId = schedulePojo.deviceId
        m.trxId = trxId
        m.username = schedulePojo.username
        m.transportType = schedulePojo.transportType

        val data = MutableLiveData<ResposeTicketConfirm>()

        ApiUtils.getAPIServiceV2().confirmTicket(m).enqueue(object : Callback<ResposeTicketConfirm> {
            override fun onResponse(call: Call<ResposeTicketConfirm>, response: Response<ResposeTicketConfirm>) {

                resposeTicketConfirm.value = response.body()

            }

            override fun onFailure(call: Call<ResposeTicketConfirm>, t: Throwable) {
                data.postValue(null)

            }
        })


    }

    fun getcancelBookedTicket(schedulePojo: CancelBookedTicketReques): String {

        mAppHandler = AppHandler.getmInstance(mContext)

        val userName = mAppHandler!!.userName
        val data = SingleLiveEvent<String>()

        ApiUtils.getAPIServiceV2().cancelBookedTicket(schedulePojo).enqueue(object : Callback<CancelBookedTicketResponse> {
            override fun onResponse(call: Call<CancelBookedTicketResponse>, response: Response<CancelBookedTicketResponse>) {
                if (response.isSuccessful) {
//
                    val cancelResponse: CancelBookedTicketResponse = response.body()!!
                    val msg: String? = cancelResponse.message
                    //show this msg
//
//                    if (request_response?.statusCode == 200){
//                        val citiesList: List<CitiesListItem?>? = request_response?.citiesList
//                        data.postValue(citiesList.toString())
//                    }

                }
            }

            override fun onFailure(call: Call<CancelBookedTicketResponse>, t: Throwable) {

                data.postValue(mContext?.getString(R.string.network_error))

            }
        })
        return data.toString()

    }


    fun getticketInformationForCancel(schedulePojo: TicketInformationForCancelRequest): String {

        mAppHandler = AppHandler.getmInstance(mContext)

        val userName = mAppHandler!!.userName
        val data = SingleLiveEvent<String>()

        ApiUtils.getAPIServiceV2().ticketInformationForCancel(schedulePojo).enqueue(object : Callback<ConfirmTicketCancelResponse> {
            override fun onResponse(call: Call<ConfirmTicketCancelResponse>, response: Response<ConfirmTicketCancelResponse>) {
                if (response.isSuccessful) {

                    val ticketCancel = response.body()

//                    val request_response = response.body()
//
//                    if (request_response?.statusCode == 200){
//                        val citiesList: List<CitiesListItem?>? = request_response?.citiesList
//                        data.postValue(citiesList.toString())
//                    }

                }
            }

            override fun onFailure(call: Call<ConfirmTicketCancelResponse>, t: Throwable) {

                data.postValue(mContext?.getString(R.string.network_error))

            }
        })
        return data.toString()

    }


}