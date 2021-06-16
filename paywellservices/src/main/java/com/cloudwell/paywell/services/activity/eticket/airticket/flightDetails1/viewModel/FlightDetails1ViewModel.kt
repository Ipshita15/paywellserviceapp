package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.FlightDetails1Status
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.RequestAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.airRules.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.airRules.ResposeAirRules

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 4/3/19.
 */
class FlightDetails1ViewModel : AirTicketBaseViewMode() {
    val mViewStatus = SingleLiveEvent<FlightDetails1Status>()


    val mListMutableLiveDataResults = MutableLiveData<ResposeAirPriceSearch>()
    val mListMutableLiveDataAirRules = MutableLiveData<List<Datum>?>()


    fun callAirPriceSearch(requestAirSearch: RequestAirPriceSearch) {
        mViewStatus.value = FlightDetails1Status(isShowProcessIndicator = true, noSerachFoundMessage = "")
        mAirTicketRepository.airPriceSearch(requestAirSearch).observeForever(object : Observer<ResposeAirPriceSearch> {
            override fun onChanged(t: ResposeAirPriceSearch?) {
                mViewStatus.value = FlightDetails1Status(isShowProcessIndicator = false, noSerachFoundMessage = "")
                val checkNetworkAndStatusCode = isOkNetworkAndStatusCode(t)
                if (checkNetworkAndStatusCode) {
                    handleRepose(t)
                } else {

                }
            }
        })
    }

    private fun handleRepose(t: ResposeAirPriceSearch?) {

        t.let {
            it?.data?.results.let {
                mListMutableLiveDataResults.value = t
            }
        }
    }

    fun isOkNetworkAndStatusCode(t: ResposeAirPriceSearch?): Boolean {
        t?.let {
            if (it.throwable != null) {
                baseViewStatus.value = it.throwable!!.message.let { it1 ->
                    BaseViewState(errorMessage = it1.toString())
                }
                mViewStatus.value = FlightDetails1Status(noSerachFoundMessage = "No Result Found!!", isShowProcessIndicator = false);

                return false
            } else if (it.status == 313) {
                baseViewStatus.value = t.message?.let { it1 -> BaseViewState(errorMessage = it1) }
                return false
            } else if (it.status == 306) {
                mViewStatus.value = FlightDetails1Status(isShowProcessIndicator = false, noSerachFoundMessage = it.toString())

                return false
            } else if (it.status == 307) {
                it.message.let {
                    mViewStatus.value = FlightDetails1Status(isShowProcessIndicator = false, noSerachFoundMessage = it.toString())
                }
                return false
            } else if (it.status != 200) {
                baseViewStatus.value = t.message?.let { it1 -> BaseViewState(errorMessage = it1) }
                return false
            }
            return true
        }
        return false
    }

    fun callAirRolesAPI(requestAirPriceSearch: RequestAirPriceSearch) {
        mViewStatus.value = FlightDetails1Status(isShowProcessIndicator = true, noSerachFoundMessage = "")
        mAirTicketRepository.airRulesSearch(requestAirPriceSearch).observeForever(object : Observer<ResposeAirRules> {
            override fun onChanged(t: ResposeAirRules?) {
                mViewStatus.value = FlightDetails1Status(isShowProcessIndicator = false, noSerachFoundMessage = "")
                val checkNetworkAndStatusCode = isOkNetworkAndStatusCode(t)
                if (checkNetworkAndStatusCode) {
                    handleRepose(t)
                } else {

                }
            }
        })

    }

    private fun handleRepose(t: ResposeAirRules?) {

        val size = t?.data?.size
        if (size == 0) {

        } else {
            mListMutableLiveDataAirRules.value = t?.data
        }


    }

    private fun isOkNetworkAndStatusCode(t: ResposeAirRules?): Boolean {
        t?.let {
            if (it.throwable != null) {
                baseViewStatus.value = it.throwable!!.message.let { it1 ->
                    BaseViewState(errorMessage = it1.toString())
                }
                mViewStatus.value = FlightDetails1Status(noSerachFoundMessage = "No Result Found!!", isShowProcessIndicator = false);
                return false
            } else if (it.status == 313) {
                baseViewStatus.value = t.message?.let { it1 -> BaseViewState(errorMessage = it1) }
                return false
            } else if (it.status == 306) {
                mViewStatus.value = FlightDetails1Status(isShowProcessIndicator = false, noSerachFoundMessage = it.toString())

                return false
            } else if (it.status == 307) {
                it.message.let {
                    mViewStatus.value = FlightDetails1Status(isShowProcessIndicator = false, noSerachFoundMessage = it.toString())
                }
                return false
            } else if (it.status != 200) {
                baseViewStatus.value = t.message?.let { it1 -> BaseViewState(errorMessage = it1) }
                return false
            }
            return true
        }
        return false

    }

}