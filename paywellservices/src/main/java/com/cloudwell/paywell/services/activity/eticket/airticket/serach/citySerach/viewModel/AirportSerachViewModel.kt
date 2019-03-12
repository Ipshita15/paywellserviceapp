package com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.viewModel

import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.AirportSeachStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.ResGetAirports

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 25/2/19.
 */
class AirportSerachViewModel : AirTicketBaseViewMode() {

    val mViewStatus = SingleLiveEvent<AirportSeachStatus>()

    lateinit var resGetAirports: ResGetAirports

    var allAirportHashMap = SingleLiveEvent<MutableMap<String, List<Airport>>>()


    fun getData(internetConnection: Boolean) {

        if (!internetConnection) {
            baseViewStatus.value = BaseViewState(isNoInternectConnectionFoud = true)
        } else {
            mAirTicketRepository.getAllCity("BD,IN").observeForever {
                val checkNetworkAndStatusCode = isOkNetworkAndStatusCode(it)
                if (checkNetworkAndStatusCode) {
                    handleRespose(it)
                } else {

                }
            }
        }
    }

    private fun handleRespose(it: ResGetAirports?) {

        it.let {
            resGetAirports = it!!
        }

        val tempAirportHashMap = kotlin.collections.mutableMapOf<String, List<Airport>>()

        it.let {
            val airports = it?.airports;

            val countries = mutableSetOf<String>()
            it?.airports?.forEach {
                countries.add(it.country)
            }

            countries.forEach {
                val name = it;
                val tempData = mutableListOf<Airport>()

                airports?.forEach {
                    if (name.equals(it.country)) {
                        tempData.add(it)
                    }
                }
                tempAirportHashMap.put(name, tempData)
            }
        }

        allAirportHashMap.value = tempAirportHashMap
    }

    private fun isOkNetworkAndStatusCode(it: ResGetAirports?): Boolean {
        it?.let {
            if (it.throwable != null) {
                baseViewStatus.value = it.throwable!!.message.let { it1 ->
                    BaseViewState(errorMessage = it1.toString())
                }

                mViewStatus.value = AirportSeachStatus(noSerachFoundMessage = "No Air Found!!", isShowProcessIndicatior = false);

                return false
            } else if (it.status == 313) {
                baseViewStatus.value = it.message?.let { it1 -> BaseViewState(errorMessage = it1) }
                return false
            } else if (it.status == 307) {
                it.message.let {
                    mViewStatus.value = AirportSeachStatus(it.toString(), false)
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