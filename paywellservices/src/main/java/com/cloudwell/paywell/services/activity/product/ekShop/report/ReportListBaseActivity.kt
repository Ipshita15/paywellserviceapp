package com.cloudwell.paywell.services.activity.product.ekShop.report

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.ProductEecommerceBaseActivity
import com.cloudwell.paywell.services.activity.product.ekShop.model.Data
import com.cloudwell.paywell.services.activity.product.ekShop.report.ui.report.AKShopRepo
import com.cloudwell.paywell.services.activity.product.ekShop.report.ui.report.ReportViewModel
import com.cloudwell.paywell.services.retrofit.ApiUtils

class ReportListBaseActivity : ProductEecommerceBaseActivity(), ReportViewModel.IReportView {
    override fun showStartDateError() {

    }

    override fun showEndDateError() {

    }

    override fun showMessage(message: String) {


    }

    override fun setupAdapter(data: List<Data>) {


    }

    override fun noInternetConnectionFOund() {
        Log.v("", "")

    }

    override fun showProgressBar() {
        Log.v("", "")
    }

    override fun hiddenProgressBar() {
        Log.v("", "")
    }

    override fun showNoReportFound() {
        Log.v("", "")
    }

    private lateinit var viewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_activity)
        setToolbar(getString(R.string.home_title_ek_shope))

        val akShopRepo = AKShopRepo(ApiUtils.getAPIService())

        val factory = EkShopViewModeFactory(akShopRepo)
        viewModel = ViewModelProviders.of(this, factory).get(ReportViewModel::class.java)

        viewModel.setView(this)
        viewModel.getReportList(isInternetConnection, "2019-06-23", "2019-06-23", "BDPC8850")


    }


}
