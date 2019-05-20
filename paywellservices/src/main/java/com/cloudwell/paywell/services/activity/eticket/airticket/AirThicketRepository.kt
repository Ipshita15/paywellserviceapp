package com.cloudwell.paywell.services.activity.eticket.airticket

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.ReposeAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.RequestAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.ResGetAirports
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.ResIssueTicket
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.FileUploadReqSearchPara
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.RequestAirPrebookingSearchParams
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResAirPreBooking
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResBookingAPI
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.RequestAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.airRules.ResposeAirRules
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.model.ResCommistionMaping
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.model.ResInvoideEmailAPI
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.InternalStorageHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class AirThicketRepository(private val mContext: Context) {

    private var mAppHandler: AppHandler? = null


    fun getAirSearchData(requestAirSearch: RequestAirSearch): MutableLiveData<ReposeAirSearch> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.imeiNo
//        val userName = "cwntcl"

        val data = MutableLiveData<ReposeAirSearch>()

        val callAirSearch = ApiUtils.getAPIService().callAirSearch(userName, requestAirSearch)
        callAirSearch.enqueue(object : Callback<ReposeAirSearch> {
            override fun onResponse(call: Call<ReposeAirSearch>, response: Response<ReposeAirSearch>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<ReposeAirSearch>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = ReposeAirSearch(t)
            }
        })
        return data
    }

    fun getAirports(iso: String): MutableLiveData<ResGetAirports> {

        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.imeiNo
//        val userName = "cwntcl"


        val data = MutableLiveData<ResGetAirports>()

        val callAirSearch = ApiUtils.getAPIService().getAirports(userName, "json", iso)
        callAirSearch.enqueue(object : Callback<ResGetAirports> {
            override fun onResponse(call: Call<ResGetAirports>, response: Response<ResGetAirports>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResGetAirports>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = ResGetAirports(t)
            }
        })
        return data

    }

    private fun getLocalAirportData(): MutableLiveData<List<Airport>> {
        val data = MutableLiveData<List<Airport>>()

        val airport = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().airport
        doAsync {
            uiThread {
                data.value = airport
            }
        }

        return data;
    }

    fun airPriceSearch(requestAirSearch: RequestAirPriceSearch): MutableLiveData<ResposeAirPriceSearch> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.imeiNo
//        val userName = "cwntcl"

        val data = MutableLiveData<ResposeAirPriceSearch>()

        val callAirSearch = ApiUtils.getAPIService().callairPriceSearch(userName, requestAirSearch)
        callAirSearch.enqueue(object : Callback<ResposeAirPriceSearch> {
            override fun onResponse(call: Call<ResposeAirPriceSearch>, response: Response<ResposeAirPriceSearch>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResposeAirPriceSearch>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = ResposeAirPriceSearch(t)
            }
        })
        return data
    }

    fun airRulesSearch(requestAirPriceSearch: RequestAirPriceSearch): MutableLiveData<ResposeAirRules> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.imeiNo
//        val userName = "cwntcl"

        val data = MutableLiveData<ResposeAirRules>()

        val callAirSearch = ApiUtils.getAPIService().airRulesSearch(userName, requestAirPriceSearch)
        callAirSearch.enqueue(object : Callback<ResposeAirRules> {
            override fun onResponse(call: Call<ResposeAirRules>, response: Response<ResposeAirRules>) {
                if (response.isSuccessful) {

//                    val mock = Gson().fromJson(DummayData().airRues, ResposeAirRules::class.java)

                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResposeAirRules>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = ResposeAirRules(t)
            }
        })
        return data


    }

    fun callAirPreBookingAPI(requestAirPrebookingSearchParams: RequestAirPrebookingSearchParams): MutableLiveData<ResAirPreBooking> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.imeiNo
//        val userName = "cwntcl"
        val format = "json"

        val data = MutableLiveData<ResAirPreBooking>()

        val callAirSearch = ApiUtils.getAPIService().airPreBooking(userName, format, requestAirPrebookingSearchParams)
        callAirSearch.enqueue(object : Callback<ResAirPreBooking> {
            override fun onResponse(call: Call<ResAirPreBooking>, response: Response<ResAirPreBooking>) {
                if (response.isSuccessful) {
//                    data.value = Gson().fromJson(DummayData().mockPreBooking, ResAirPreBooking::class.java)
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResAirPreBooking>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = ResAirPreBooking(t)
            }
        })
        return data

    }

    fun uploadBookingFiles(bookingID: String?, dataList: List<FileUploadReqSearchPara>): MutableLiveData<JsonObject> {

        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.imeiNo
//        val userName = "cwntcl"


        val data = MutableLiveData<JsonObject>()

        val toTypedArray = dataList.toTypedArray()
        val toJson = Gson().toJson(toTypedArray)


        val callAirSearch = ApiUtils.getAPIService().uploadBookingFiles(userName, bookingID, toJson)
        callAirSearch.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
//                    data.value = Gson().fromJson(DummayData().mockPreBooking, ResAirPreBooking::class.java)
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)


            }
        })
        return data


    }

    fun callInvoiceAPI(bookingID: String, emailString: String): MutableLiveData<ResInvoideEmailAPI> {

        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.imeiNo
//        val userName = "cwntcl"


        val data = MutableLiveData<ResInvoideEmailAPI>()


        val callAirSearch = ApiUtils.getAPIService().callSendInvoiceAPI(userName, bookingID, emailString)
        callAirSearch.enqueue(object : Callback<ResInvoideEmailAPI> {
            override fun onResponse(call: Call<ResInvoideEmailAPI>, response: Response<ResInvoideEmailAPI>) {
                if (response.isSuccessful) {
//                    data.value = Gson().fromJson(DummayData().mockPreBooking, ResAirPreBooking::class.java)
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResInvoideEmailAPI>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = ResInvoideEmailAPI(t)

            }
        })
        return data


    }

    fun callAirBookingAPI(piN_NO: String, requestAirPrebookingSearchParams: RequestAirPrebookingSearchParams): MutableLiveData<ResBookingAPI> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val userName = mAppHandler!!.imeiNo
        val format = "json"

        val data = MutableLiveData<ResBookingAPI>()

        val callAirSearch = ApiUtils.getAPIService().airBooking(userName, piN_NO, format, requestAirPrebookingSearchParams)
        callAirSearch.enqueue(object : Callback<ResBookingAPI> {
            override fun onResponse(call: Call<ResBookingAPI>, response: Response<ResBookingAPI>) {
                if (response.isSuccessful) {
//                    data.value = Gson().fromJson(DummayData().mockPreBooking, ResAirPreBooking::class.java)
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResBookingAPI>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = ResBookingAPI(t)
            }
        })

        return data

    }


    fun callGetBookingStatusAPI(limit: Int): MutableLiveData<BookingList> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val username = mAppHandler!!.imeiNo
//        val username = "cwntcl"

        val data = MutableLiveData<BookingList>()

        val responseBodyCall = ApiUtils.getAPIService().callAirBookingListSearch(username, limit)
        responseBodyCall.enqueue(object : Callback<BookingList> {
            override fun onResponse(call: Call<BookingList>, response: Response<BookingList>) {

                if (response.isSuccessful) {
//                    data.value = Gson().fromJson(DummayData().mockPreBooking, ResAirPreBooking::class.java)
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<BookingList>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = BookingList(throwable = t)
            }
        })
        return data

    }

    fun callCommissionMappingAPI(): MutableLiveData<ResCommistionMaping> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val username = mAppHandler!!.imeiNo

        val data = MutableLiveData<ResCommistionMaping>()

        val responseBodyCall = ApiUtils.getAPIService().callGetCommissionMappingAPI(username)
        responseBodyCall.enqueue(object : Callback<ResCommistionMaping> {
            override fun onResponse(call: Call<ResCommistionMaping>, response: Response<ResCommistionMaping>) {

                if (response.isSuccessful) {
//                    data.value = Gson().fromJson(DummayData().mockPreBooking, ResAirPreBooking::class.java)
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResCommistionMaping>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = ResCommistionMaping(throwable = t)
            }
        })
        return data

    }

    fun callIssueTicketAPI(pinNumber: String, bookingId: String, ssAcceptedPriceChangeandIssueTicket: Boolean): MutableLiveData<ResIssueTicket> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val username = mAppHandler!!.imeiNo

        val data = MutableLiveData<ResIssueTicket>()

        val responseBodyCall = ApiUtils.getAPIService().callIssueTicketAPI(username, pinNumber, bookingId, ssAcceptedPriceChangeandIssueTicket)
        responseBodyCall.enqueue(object : Callback<ResIssueTicket> {
            override fun onResponse(call: Call<ResIssueTicket>, response: Response<ResIssueTicket>) {

                if (response.isSuccessful) {
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResIssueTicket>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                data.value = ResIssueTicket(throwable = t)
            }
        })
        return data
    }


    fun getAllPassengers(): MutableLiveData<List<Passenger>> {
        val data = MutableLiveData<List<Passenger>>()
        doAsync {
            val all: List<Passenger> = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().all
            uiThread {
                data.value = all
            }
        }
        return data
    }

    fun addPassenger(passenger: Passenger): MutableLiveData<Long> {
        val data = MutableLiveData<Long>()

        doAsync {
            val insert = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().insert(passenger)

            uiThread {
                if (insert != -1L) {
                    AppStorageBox.put(mContext, AppStorageBox.Key.COUNTER_PASSENGER, insert)
                }
                data.value = insert
            }
        }

        return data
    }

    fun updatePassenger(passenger: Passenger): MutableLiveData<Int> {
        val data = MutableLiveData<Int>()

        doAsync {
            val insert = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().update(passenger)

            uiThread {
                if (insert != -1) {
                    AppStorageBox.put(mContext, AppStorageBox.Key.COUNTER_PASSENGER, insert)
                }
                data.value = insert
            }
        }

        return data

    }

    fun deletedPassenger(passenger: Passenger): MutableLiveData<Int> {
        val data = MutableLiveData<Int>()

        doAsync {
            val insert = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().deleted(passenger)

            uiThread {
                if (insert != -1) {
                    if (insert == 1) {
                        AppStorageBox.put(mContext, AppStorageBox.Key.COUNTER_PASSENGER, null)
                    } else {
                        AppStorageBox.put(mContext, AppStorageBox.Key.COUNTER_PASSENGER, insert)
                    }

                }
                data.value = insert
            }
        }

        return data
    }

    fun getRecentSearches(): MutableLiveData<List<Airport>> {
        val data = MutableLiveData<List<Airport>>()

        doAsync {
            val result = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().getRecentSearches()

            uiThread {
                data.value = result
            }
        }
        return data
    }

    fun addRecentAirport(airport: Airport): MutableLiveData<Long> {
        val data = MutableLiveData<Long>()
        doAsync {
            val result = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().insertRecentAirport(airport)

            uiThread {
                data.value = result
            }
        }
        return data
    }

    fun getAllPassengers(split: List<String>): MutableLiveData<List<Passenger>> {
        val data = MutableLiveData<List<Passenger>>()
        doAsync {
            val all: List<Passenger> = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().getAll(split)
            uiThread {
                data.value = all
            }
        }
        return data
    }

    fun getAllAirportForLocal(serachParameter: String): MutableLiveData<List<Airport>> {
        val data = MutableLiveData<List<Airport>>()

        doAsync {
            var airport = mutableListOf<Airport>()
            if (serachParameter.equals("")) {
                airport = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().airport
            } else {
                airport = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().getAirport(serachParameter)
            }
            uiThread {
                data.value = airport
            }
        }


        return data;
    }

    fun insertAirportData(airports: List<Airport>) {
        doAsync {

            DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().insertAirportList(airports)

            uiThread {
                com.orhanobut.logger.Logger.v("Data insert successfully")
            }
        }

    }

    fun deleteAirportData(): MutableLiveData<Boolean> {

        val data = MutableLiveData<Boolean>()
        doAsync {

            DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().clearAirportsData()

            uiThread {
                data.value = true
                com.orhanobut.logger.Logger.v("Data insert successfully")
            }
        }

        return data;

    }

    fun saveCombustionData(it: ResCommistionMaping?) {
        val toJson = Gson().toJson(it?.commission)
        InternalStorageHelper.writeData(toJson, InternalStorageHelper.CombustionfileName)
    }

    fun readCommissionData(file: File): String {
        return InternalStorageHelper.readData(InternalStorageHelper.CombustionfileName)
    }


}