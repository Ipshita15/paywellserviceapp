package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view

import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.base.BaseView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
interface IbusTransportListView : BaseView {
    fun showNoTripFoundUI()
    fun setAdapter(it1: List<TripScheduleInfoAndBusSchedule>)
}