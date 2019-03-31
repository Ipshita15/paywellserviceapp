package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.ReposeAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.RequestAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Result
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Segment
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.view.SeachViewStatus

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class FlightSearchViewModel : AirTicketBaseViewMode() {

    val mViewStatus = SingleLiveEvent<SeachViewStatus>()

    // view data

    val mListMutableLiveDataFlightData = MutableLiveData<List<Result>>()
    val mSearchId = MutableLiveData<String>()


    fun init(internetConnection: Boolean, requestAirSearch: RequestAirSearch) {
        if (!internetConnection) {
            baseViewStatus.value = BaseViewState(isNoInternectConnectionFoud = true)
        } else {
            callFlightSearch(requestAirSearch);
        }
    }

    private fun callFlightSearch(requestAirSearch: RequestAirSearch) {
        mViewStatus.value = SeachViewStatus(isShowShimmerView = true, isShowProcessIndicator = true)
        mAirTicketRepository.getAirSearchData(requestAirSearch).observeForever(object : Observer<ReposeAirSearch> {
            override fun onChanged(t: ReposeAirSearch?) {
                mViewStatus.value = SeachViewStatus(isShowShimmerView = false, isShowProcessIndicator = false)
                val checkNetworkAndStatusCode = isOkNetworkAndStatusCode(t)
                if (checkNetworkAndStatusCode) {
                    handleRepose(t)
                } else {

                }
            }
        })
    }


    fun onSetDate(internetConnection: Boolean, date: String, requestAirSearch: RequestAirSearch) {
        if (!internetConnection) {
            baseViewStatus.value = BaseViewState(isNoInternectConnectionFoud = true)
        } else {

            val updateDateSegments = mutableListOf<Segment>()
            requestAirSearch.segments.forEach {
                it.departureDateTime = date
                updateDateSegments.add(it)
            }
            requestAirSearch.segments = updateDateSegments
            callFlightSearch(requestAirSearch)
        }

    }

    fun isOkNetworkAndStatusCode(t: ReposeAirSearch?): Boolean {
        t?.let {
            if (it.throwable != null) {
                baseViewStatus.value = it.throwable!!.message.let { it1 ->
                    BaseViewState(errorMessage = it1.toString())
                }

                mViewStatus.value = SeachViewStatus(noSerachFoundMessage = "No Result Found!!", isShowShimmerView = false, isShowProcessIndicator = false);

                return false
            } else if (it.status == 313) {
                baseViewStatus.value = t.message?.let { it1 -> BaseViewState(errorMessage = it1) }
                return false
            } else if (it.status == 307) {
                it.message.let {
                    mViewStatus.value = SeachViewStatus(it.toString(), false, isShowProcessIndicator = false)
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

    private fun handleRepose(t: ReposeAirSearch?) {
        t.let {
            it?.data?.results.let {
                val sortedListTotalFare = it?.sortedWith(compareBy(Result::totalFare, Result::totalFare))
                mListMutableLiveDataFlightData.value = sortedListTotalFare
                mSearchId.value = t?.data?.searchId
            }
        }
    }
}






