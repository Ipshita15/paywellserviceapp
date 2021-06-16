package com.cloudwell.paywell.services.activity.eticket.busticketNew.search.viewModel

import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTicketRepository.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.BusLunCityRequest
import com.cloudwell.paywell.services.activity.eticket.busticketNew.search.view.ICitySerach
import com.cloudwell.paywell.services.activity.eticket.busticketNew.viewMode.BusTicketBaseViewMode

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
class CitySearchViewModel : BusTicketBaseViewMode() {

    var view: ICitySerach? = null


    fun setIbusTransportListView(iCitySerach: ICitySerach) {
        this.view = iCitySerach
    }



    fun getbusAndLaunchCities(requestBusSearch: BusLunCityRequest) {
        view?.showProgress()


        BusTicketRepository().getbusAndLaunchCities(requestBusSearch).observeForever {
            view?.hiddenProgress()
            if (it== null){

            }else{
                view?.generateCitylist(it)
            }

        }

    }



}