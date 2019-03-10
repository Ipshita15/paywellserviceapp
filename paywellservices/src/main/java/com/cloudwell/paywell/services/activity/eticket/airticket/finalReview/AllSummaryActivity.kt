package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel.AllSummaryActivityViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.FlightDetails1Status
import kotlinx.android.synthetic.main.all_summaray_bottom_sheet.*


class AllSummaryActivity : AirTricketBaseActivity() {


    var totalJourneyinMiliSecound = 0L

    private lateinit var mViewModel: AllSummaryActivityViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_summaray)

        setToolbar(getString(R.string.title_all_summary))


        initializationView()
        initilizationReviewBottomSheet()


        initViewModel()
//
//        val parcelable = getIntent().getExtras().getParcelable<Result>("object") as Result


        // displayData(results.toList())

    }

    private fun initilizationReviewBottomSheet() {

        val bottomSheetBehavior = BottomSheetBehavior.from(allSummaryBottonSheet)


        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(AllSummaryActivityViewModel::class.java)

        mViewModel.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })

    }

    private fun handleViewStatus(status: FlightDetails1Status?) {


    }


    private fun initializationView() {


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
