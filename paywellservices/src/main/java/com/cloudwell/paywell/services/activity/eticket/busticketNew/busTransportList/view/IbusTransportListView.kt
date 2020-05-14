package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view

import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.base.BaseView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatCheckBookAPI
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.RequestScheduledata
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata.ScheduleDataItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm.ResposeTicketConfirm

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
interface IbusTransportListView : BaseView {
    fun showNoTripFoundUI()
    fun showErrorMessage(message: String)
    fun showSeatCheckAndBookingRepose(it: ResSeatCheckBookAPI)
    fun showShowConfirmDialog(it: ResposeTicketConfirm)
    fun setBoardingPoint(allBoothNameInfo: MutableSet<String>)
    fun saveRequestScheduledata(p: RequestScheduledata)
    fun saveExtraCharge(double: Double)
    fun showFilterList(filterTypeDepartingTime: List<ScheduleDataItem>)
}