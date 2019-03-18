package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel

import android.arch.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.RequestAirPrebookingSearchParams
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResAirPreBooking
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel.view.AllSummaryStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.storage.AppStorageBox


class AllSummaryActivityViewModel : AirTicketBaseViewMode() {
    var mListMutableLiveDPassengers = MutableLiveData<MutableList<Passenger>>()
    val mViewStatus = SingleLiveEvent<AllSummaryStatus>()


    fun init(passengerIDS: String) {
        val split = passengerIDS.split(",")

        mAirTicketRepository.getAllPassengers(split).observeForever {

            mListMutableLiveDPassengers.value = it as MutableList<Passenger>?

        }

    }

    fun callAirBookingAPI(piN_NO: String, passengerIDS: String, internetConnection1: Boolean) {
        if (!internetConnection1) {
            baseViewStatus.value = BaseViewState(isNoInternectConnectionFoud = true)
        } else {
            mViewStatus.value = AllSummaryStatus(noSerachFoundMessage = "", isShowProcessIndicatior = true)

            val resposeAirPriceSearch = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.AIR_PRE_BOOKKING) as ResAirPreBooking


            mAirTicketRepository.getAllPassengers(passengerIDS.split(",")).observeForever {
                it?.let {

                    val requestModel = RequestAirPrebookingSearchParams()
                    requestModel.resultID = resposeAirPriceSearch.data?.results?.get(0)?.resultID
                    requestModel.searchId = resposeAirPriceSearch.data?.searchId
                    requestModel.passengers = it


                    mAirTicketRepository.callAirBookingAPI(piN_NO, requestModel).observeForever {
                        mViewStatus.value = AllSummaryStatus(noSerachFoundMessage = "", isShowProcessIndicatior = false)
                        val okNetworkAndStatusCode = isOkNetworkAndStatusCode(it)
                        if (okNetworkAndStatusCode) {

                            mViewStatus.value = it?.let { it1 -> AllSummaryStatus("", false, it1) }

                        }
                    }


                }
            }


        }
    }


    fun callAirPreBookingAPI(piN_NO: String, passengerIDS: String, internetConnection1: Boolean) {
        if (!internetConnection1) {
            baseViewStatus.value = BaseViewState(isNoInternectConnectionFoud = true)
        } else {
            mViewStatus.value = AllSummaryStatus(noSerachFoundMessage = "", isShowProcessIndicatior = true)
            mAirTicketRepository.getAllPassengers(passengerIDS.split(",")).observeForever {
                it?.let {

                    val resposeAirPriceSearch = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.ResposeAirPriceSearch) as ResposeAirPriceSearch

                    val requestModel = RequestAirPrebookingSearchParams()
                    requestModel.resultID = resposeAirPriceSearch.data?.results?.get(0)?.resultID
                    requestModel.searchId = resposeAirPriceSearch.data?.searchId
                    requestModel.passengers = it
                    mAirTicketRepository.callAirPreBookingAPI(piN_NO, requestModel).observeForever {
                        mViewStatus.value = AllSummaryStatus(noSerachFoundMessage = "", isShowProcessIndicatior = false)
                        val okNetworkAndStatusCode = isOkNetworkAndStatusCode(it)
                        if (okNetworkAndStatusCode) {

                            mViewStatus.value = it?.let { it1 -> AllSummaryStatus("", false, it1) }
                        }

                    }
                }

            }
        }
    }

    private fun isOkNetworkAndStatusCode(it: ResAirPreBooking?): Boolean {
        it?.let {
            if (it.throwable != null) {
                baseViewStatus.value = it.throwable!!.message.let { it1 ->
                    BaseViewState(errorMessage = it1.toString())
                }

                mViewStatus.value = AllSummaryStatus(noSerachFoundMessage = "No Air Found!!", isShowProcessIndicatior = false);

                return false
            } else if (it.status == 313) {
                baseViewStatus.value = it.message?.let { it1 -> BaseViewState(errorMessage = it1) }
                return false
            } else if (it.status == 365) {
                baseViewStatus.value = it.message?.let { it1 -> BaseViewState(errorMessage = it1) }
                return false
            } else if (it.status == 307) {
                it.message.let {
                    mViewStatus.value = AllSummaryStatus(it.toString(), false)
                }
                return false
            } else if (it.status != 200) {
                baseViewStatus.value = it.message?.let { it1 -> BaseViewState(errorMessage = it1) }
                return false
            }
            return true
        }
        return false

    }

}

