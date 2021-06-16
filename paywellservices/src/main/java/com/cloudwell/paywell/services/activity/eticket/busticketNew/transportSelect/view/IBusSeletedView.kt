package com.cloudwell.paywell.services.activity.eticket.busticketNew.transportSelect.view

import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.base.BaseView

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-07-23.
 */
interface IBusSeletedView : BaseView {
    fun openNextActivity()
    fun showErrorMessage(status: String?)
}