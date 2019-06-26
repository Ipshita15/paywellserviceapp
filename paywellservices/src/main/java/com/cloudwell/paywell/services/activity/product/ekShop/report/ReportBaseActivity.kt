package com.cloudwell.paywell.services.activity.product.ekShop.report

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.ProductEecommerceBaseActivity
import com.cloudwell.paywell.services.activity.product.ekShop.model.Data
import com.cloudwell.paywell.services.activity.product.ekShop.report.ui.report.AKShopRepo
import com.cloudwell.paywell.services.activity.product.ekShop.report.ui.report.ReportViewModel
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.report_activity.*
import java.text.SimpleDateFormat
import java.util.*

class ReportBaseActivity : ProductEecommerceBaseActivity(), ReportViewModel.IReportView {
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


        etStartDate.setOnClickListener {

            handleStartDate()
        }

        etStartDate.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                handleStartDate()
            }
        })

        etEndDate.setOnClickListener {

            handleEndDate()
        }

        etEndDate.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                handleEndDate()
            }
        })

        btSearchReport.setOnClickListener {


            viewModel.search(isInternetConnection, etStartDate.text.toString(), etEndDate.text.toString(), etOrderCode.text.toString())
        }


    }

    private fun handleEndDate() {

        showEndDate()
    }

    private fun showEndDate() {
        val calendar = Calendar.getInstance()


        val year = calendar.get(Calendar.YEAR)
        val thismonth = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)

                    val mMonth = month + 1
                    val androidSystemdate = "${year}-${mMonth}-${day}"
                    val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(androidSystemdate) as Date
                    val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)
                    etEndDate.setText(humanReadAbleDate)
                }, year, thismonth, dayOfMonth)

//        datePickerDialog.datePicker.setMaxDate(System.currentTimeMillis())
        datePickerDialog.show()

    }

    private fun handleStartDate() {

        showDateOfBirthDatePicker()
    }


    private fun showDateOfBirthDatePicker() {
        val calendar = Calendar.getInstance()


        val year = calendar.get(Calendar.YEAR)
        val thismonth = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)

                    val mMonth = month + 1
                    val androidSystemdate = "${year}-${mMonth}-${day}"
                    val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(androidSystemdate) as Date
                    val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)
                    etStartDate.setText(humanReadAbleDate)


                }, year, thismonth, dayOfMonth)

//        datePickerDialog.datePicker.setMaxDate(System.currentTimeMillis())
        datePickerDialog.show()

    }


}
