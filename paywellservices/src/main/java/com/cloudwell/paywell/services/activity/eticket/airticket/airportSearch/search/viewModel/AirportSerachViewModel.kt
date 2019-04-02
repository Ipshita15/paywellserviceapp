package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.viewModel

import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.AirportSeachStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.ResGetAirports
import com.cloudwell.paywell.services.app.AppHandler

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 25/2/19.
 */
class AirportSerachViewModel : AirTicketBaseViewMode() {

    val mViewStatus = SingleLiveEvent<AirportSeachStatus>()

    lateinit var resGetAirports: ResGetAirports

    var allAirportHashMap = SingleLiveEvent<MutableMap<String, List<Airport>>>()


    val UPDATE_SOFTWARE_INTERVAL = (24 * 60 * 60).toLong()// 1 day


    fun getData(internetConnection: Boolean, isIndian: Boolean, appHandler: AppHandler) {

        if (!internetConnection) {
            baseViewStatus.value = BaseViewState(isNoInternectConnectionFoud = true)
        } else {

            mViewStatus.value = AirportSeachStatus(noSerachFoundMessage = "", isShowProcessIndicatior = true)

            var serachParameter = ""
            if (isIndian) {
                serachParameter = "IN"
            }

            val checkAirportListUpdateChecker = checkAirportListUpdateChecker(appHandler, internetConnection)
            if (checkAirportListUpdateChecker == true) {
                getAirportListForRemoteAPI(serachParameter, appHandler)

            } else {

                mAirTicketRepository.getAllAirportForLocal(serachParameter).observeForever {

                    mViewStatus.value = AirportSeachStatus(noSerachFoundMessage = "", isShowProcessIndicatior = false)

                    val resGetAirports1 = ResGetAirports(null)
                    resGetAirports1.airports = it!!.toMutableList()

                    handleRespose(resGetAirports1, null)

                }
            }

        }
    }

    private fun getAirportListForRemoteAPI(serachParameter: String, appHandler: AppHandler) {
        mAirTicketRepository.getAirports(serachParameter).observeForever {
            val checkNetworkAndStatusCode = isOkNetworkAndStatusCode(it)
            if (checkNetworkAndStatusCode) {
                mViewStatus.value = AirportSeachStatus(noSerachFoundMessage = "", isShowProcessIndicatior = false)

                val recentSearches = mAirTicketRepository.getRecentSearches()
                val resGetAirpots = it

                mAirTicketRepository.insertAirportData(resGetAirpots!!.airports)

                handleRespose(resGetAirpots, null)

                appHandler.setAirportListUpdateCheck(System.currentTimeMillis() / 1000)

            } else {

            }
        }
    }


    private fun checkAirportListUpdateChecker(appHandler: AppHandler, isInternetConnection: Boolean): Boolean {
        if (System.currentTimeMillis() / 1000 >= appHandler.airportListUpdateCheck + UPDATE_SOFTWARE_INTERVAL) {
            if (isInternetConnection) {
                return true;

            } else {
                return false;
            }
        } else {
            appHandler.setAirportListUpdateCheck(System.currentTimeMillis() / 1000)
            return false;
        }
    }

    private fun handleRespose(it: ResGetAirports?, recentList: List<Airport>?) {

        it.let {
            resGetAirports = it!!
        }

        val tempAirportHashMap = kotlin.collections.mutableMapOf<String, List<Airport>>()

        it.let {
            val airports = it?.airports;

            val countries = mutableSetOf<String>()

            // add recent search
            var recentAirpot = mutableListOf<Airport>()
            if (recentList != null) {
                countries.add("Recent Searches")

                recentAirpot = recentList.toMutableList()
                recentAirpot.reverse()

                tempAirportHashMap.put("Recent Searches", recentList.toMutableList())
            }


            it?.airports?.forEach {
                countries.add(it.country)
            }

            countries.forEach {
                val name = it;
                var tempData = mutableListOf<Airport>()

                if (name.equals("Recent Searches")) {

                    tempData = recentAirpot

                } else {
                    airports?.forEach {
                        if (name.equals(it.country)) {
                            tempData.add(it)
                        }
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

    fun addRecentSearch(airport: Airport) {
        mAirTicketRepository.addRecentAirport(airport)

    }


}