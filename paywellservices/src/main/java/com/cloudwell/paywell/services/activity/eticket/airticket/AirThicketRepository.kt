package com.cloudwell.paywell.services.activity.eticket.airticket

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.RequestAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.ResGetAirports
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.ReposeAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.RequestAirSearch
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class AirThicketRepository(private val mContext: Context) {

    private var mAppHandler: AppHandler? = null

    fun getAirSearchData(requestAirSearch: RequestAirSearch): MutableLiveData<ReposeAirSearch> {
        mAppHandler = AppHandler.getmInstance(mContext)
//        val userName = mAppHandler!!.imeiNo
        val userName = "cwntcl"

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

    fun getAllCity(s: String): MutableLiveData<ResGetAirports> {

        mAppHandler = AppHandler.getmInstance(mContext)
//        val userName = mAppHandler!!.imeiNo
        val userName = "cwntcl"
        val iso = "BD,IN"

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

    fun airPriceSearch(requestAirSearch: RequestAirPriceSearch): MutableLiveData<ResposeAirPriceSearch> {
        mAppHandler = AppHandler.getmInstance(mContext)
//        val userName = mAppHandler!!.imeiNo
        val userName = "cwntcl"

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
}