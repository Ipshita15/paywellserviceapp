package com.cloudwell.paywell.services.activity.product.ekShop.report.ui.report

import android.arch.lifecycle.ViewModel
import com.cloudwell.paywell.services.activity.product.ekShop.model.Data


class ReportViewModel(
        val aKShopRepo: AKShopRepo
) : ViewModel() {
    var iView: IReportView? = null

    fun getReportList(internetConnection: Boolean, startDate: String, endDate: String, order_code: String) {
        if (!internetConnection) {
            iView?.noInternetConnectionFOund()
        } else {
            aKShopRepo.getReportList(startDate, endDate, order_code).observeForever {
                if (it == null) {
                    iView?.showMessage("Please try again!!")
                } else {
                    if (it.status == 200) {
                        iView?.setupAdapter(it.data)
                    } else {
                        iView?.showMessage(it.message)
                    }
                }
            }
        }
    }

    fun setView(iView: IReportView) {
        this.iView = iView
    }

    fun search(internetConnection: Boolean, startDate: String, endDate: String, orderCode: String) {
        if (startDate.equals("")) {
            this.iView?.showStartDateError()

        } else if (endDate.equals("")) {
            this.iView?.showEndDateError()
        } else {
            getReportList(internetConnection, startDate, endDate, orderCode)
        }
    }

    interface IReportView {
        fun showProgressBar()
        fun hiddenProgressBar()
        fun showNoReportFound()
        fun noInternetConnectionFOund()
        fun setupAdapter(data: List<Data>)
        fun showMessage(message: String)
        fun showStartDateError()
        fun showEndDateError()
    }
}
