package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.adapter.AdapterForPassengersFinalList
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel.AllSummaryActivityViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.FlightDetails1Status
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.AddPassengerActivity
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.all_summaray_bottom_sheet.*


class AllSummaryActivity : AirTricketBaseActivity() {


    var totalJourneyinMiliSecound = 0L

    private lateinit var mViewModel: AllSummaryActivityViewModel

    lateinit var passengerIDS: String


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_summaray)

        setToolbar(getString(R.string.title_all_summary))


        passengerIDS = AppStorageBox.get(applicationContext, AppStorageBox.Key.SELETED_PASSENGER_IDS) as String


        initializationView()
        initilizationReviewBottomSheet()




        initViewModel(passengerIDS)
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

    private fun initViewModel(passengerIDS: String) {
        mViewModel = ViewModelProviders.of(this).get(AllSummaryActivityViewModel::class.java)

        mViewModel.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })

        mViewModel.mListMutableLiveDPassengers.observe(this, Observer {
            it?.let { it1 -> handlePassengerList(it1) }
        })

    }

    override fun onResume() {
        super.onResume()
        mViewModel.init(passengerIDS)

    }

    private fun handlePassengerList(it: MutableList<Passenger>) {
        val recyclerView = findViewById(com.cloudwell.paywell.services.R.id.recycleviewForPassenger) as RecyclerView
        recyclerView.setHasFixedSize(true)

        val columns = 1;

        val glm = GridLayoutManager(applicationContext, columns)
        recyclerView.layoutManager = glm


        val recyclerListAdapter = AdapterForPassengersFinalList(this, it, object : AdapterForPassengersFinalList.OnClickListener {

            override fun onDeleted(model: Passenger, position: Int) {

                // showDeletedPassenger(model, position)
            }

            override fun onEditClick(model: Passenger, position: Int) {
                AppStorageBox.put(applicationContext, AppStorageBox.Key.AIRTRICKET_EDIT_PASSENGER, model)
                val intent = Intent(applicationContext, AddPassengerActivity::class.java)
                intent.putExtra("isEditFlag", true)
                startActivity(intent)

            }

        })


        recyclerView.layoutManager = glm
        recyclerView.adapter = recyclerListAdapter;
        recyclerView.isNestedScrollingEnabled = false;

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
