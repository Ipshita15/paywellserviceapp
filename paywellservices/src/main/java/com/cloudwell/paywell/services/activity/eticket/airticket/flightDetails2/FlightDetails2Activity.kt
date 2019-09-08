package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.RequestAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.AllSummaryActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.BaggageAndPoliciesActiivty
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment.FlightFareDialogFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.adapter.AdapterForPassengers
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.viewmodel.FlightDetails2ViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.AddPassengerActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerList.PassengerListActivity
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.utils.CalculationHelper
import com.cloudwell.paywell.services.utils.RecyclerItemClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.contant_flight_details_2.*
import kotlinx.android.synthetic.main.review_bottom_sheet.*


class FlightDetails2Activity : AirTricketBaseActivity() {


    private lateinit var viewMode: FlightDetails2ViewModel
    lateinit var adapterForPassengers: AdapterForPassengers
    lateinit var resposeAirPriceSearch: ResposeAirPriceSearch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details_2)
        setToolbar(getString(R.string.title_booking_and_review))

        initializationView()
        initilizationReviewBottomSheet()
        initViewModel()


    }

    private fun initilizationReviewBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(reviewBottonSheet)


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

        val AIRLINE_CODE = AppStorageBox.get(applicationContext, AppStorageBox.Key.AIRLINE_CODE) as String
        val totalFareDetati = resposeAirPriceSearch.data?.results?.get(0)?.fares?.let { CalculationHelper.getTotalFareDetati(it, AIRLINE_CODE) }

        tvTotalPrice.text = totalFareDetati

        tvTotalPrice.setOnClickListener {

            val dialogFragment = FlightFareDialogFragment()
            val get = resposeAirPriceSearch.data?.results?.get(0)?.fares?.get(0)

            val args = Bundle()
            args.putParcelable("object", get)

            dialogFragment.arguments = args
            dialogFragment.show(supportFragmentManager, "dialog")
        }


        viewReview.setOnClickListener {

            handleLReviewButton()
        }


    }

    private fun handleLReviewButton() {
        var totalSeletedCounter = 0
        var totalPassenger = 0
        var passengerString = ""

        val requestAirSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.REQUEST_AIR_SERACH) as RequestAirSearch


        totalPassenger = (requestAirSearch.adultQuantity + requestAirSearch.childQuantity + requestAirSearch.infantQuantity).toInt();


        viewMode.mListMutableLiveDPassengers.value?.forEach {
            val model = it
            val passengerSleted = model.isPassengerSleted
            if (passengerSleted) {
                passengerString = "$passengerString +${it.id},"
                totalSeletedCounter = totalSeletedCounter + 1
            }

            if (!validationCheckPassportMandatory(model)) {
                return
            }

            // check is passport mandatory and and passport and visa image have or not
        }


        if (totalSeletedCounter == totalPassenger) {
            AppStorageBox.put(applicationContext, AppStorageBox.Key.SELETED_PASSENGER_IDS, passengerString)
            startActivity(Intent(applicationContext, AllSummaryActivity::class.java))
        } else {
            showWarringForMissMax(requestAirSearch)
        }
    }

    private fun validationCheckPassportMandatory(model: Passenger): Boolean {
        if (resposeAirPriceSearch.data?.results?.get(0)?.passportMadatory == true && !model.isDefault) {
            if (model.passportImagePath.equals("")) {
                showDialogMesssageWithEditBoutton("You selected passenger has no passport image, Please edit your passenger information.", model)
                return false
            }
            if (model.visa_content.equals("")) {
                showDialogMesssageWithEditBoutton("You selected passenger has no VISA image, Please edit your passenger information.", model)
                return false
            }
            if (model.passportExpiryDate.equals("")) {
                showDialogMesssageWithEditBoutton("You selected passenger has no passport expiry date, Please edit your passenger information.", model)
                return false
            }
            if (model.passportNumber.equals("")) {
                showDialogMesssageWithEditBoutton("You selected passenger has no passport number, Please edit your passenger information.", model)
                return false
            }
            if (model.passportNationality.equals("")) {
                showDialogMesssageWithEditBoutton("You selected passenger has no passport nationality, Please edit your passenger information.", model)
                return false
            }
        }
        return true
    }

    fun checkValidation(): Boolean {
        var totalSeletedCounter = 0
        var totalPassenger = 0
        var passengerString = ""

        val requestAirSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.REQUEST_AIR_SERACH) as RequestAirSearch


        totalPassenger = (requestAirSearch.adultQuantity + requestAirSearch.childQuantity + requestAirSearch.infantQuantity).toInt();


        viewMode.mListMutableLiveDPassengers.value?.forEach {
            val passengerSleted = it.isPassengerSleted
            if (passengerSleted) {
                passengerString = "$passengerString +${it.id},"
                totalSeletedCounter = totalSeletedCounter + 1;
            }
        }

        if (totalSeletedCounter < totalPassenger) {
            return true
        } else {
            return false
        }

    }

    private fun showWarringForMissMax(requestAirSearch: RequestAirSearch) {


        var adultQuantityMessage = ""
        if (requestAirSearch.adultQuantity != 0L) {
            adultQuantityMessage = "${requestAirSearch.adultQuantity} adult"
        }


        var childQuantityMessage = ""
        if (requestAirSearch.childQuantity != 0L) {
            childQuantityMessage = ", ${requestAirSearch.childQuantity} child"
        }


        var infantQuantityMessage = ""
        if (requestAirSearch.infantQuantity != 0L) {
            infantQuantityMessage = ", ${requestAirSearch.infantQuantity} infant"
        }


        var message = "This price is only available for ${adultQuantityMessage} ${childQuantityMessage} ${infantQuantityMessage}.\nPlease provide all passenger information "

        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), DialogInterface.OnClickListener { dialog, id ->


                })
        val alert = builder.create()
        alert.show()

    }

    override fun onResume() {
        super.onResume()
        viewMode.getAllPassengers()
    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(FlightDetails2ViewModel::class.java)

        viewMode.baseViewStatus.observe(this, androidx.lifecycle.Observer {
            handleViewCommonStatus(it)
        })

        viewMode.mListMutableLiveDPassengers.observe(this, androidx.lifecycle.Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })


    }

    private fun handleViewStatus(it: List<Passenger>) {

        val recyclerView = findViewById(com.cloudwell.paywell.services.R.id.recyclerViewPassenger) as RecyclerView
        recyclerView.setHasFixedSize(true)

        val columns = 2

        val glm = GridLayoutManager(applicationContext, columns)
        recyclerView.layoutManager = glm


        adapterForPassengers = AdapterForPassengers(this, it)
        recyclerView.layoutManager = glm
        recyclerView.adapter = adapterForPassengers;
        recyclerView.isNestedScrollingEnabled = false;

        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(applicationContext, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // do whatever
                        val get = viewMode.mListMutableLiveDPassengers.value?.get(position)
                        if (get?.isDefault!!) {

                            val get1 = AppStorageBox.get(applicationContext, AppStorageBox.Key.COUNTER_PASSENGER)
                            if (get1 == null) {
                                val intent = Intent(applicationContext, AddPassengerActivity::class.java)
                                intent.putExtra("isValidation", checkValidation())
                                startActivity(intent)
                            } else {
                                val intent = Intent(applicationContext, PassengerListActivity::class.java)
                                intent.putExtra("isValidation", checkValidation())
                                startActivity(intent)
                            }
                        } else {


                            if (get.isPassengerSleted) {
                                get.isPassengerSleted = false

                                viewMode.updatePassenger(get)
                                viewMode.mListMutableLiveDPassengers.value?.set(position, get)
                                adapterForPassengers.notifyDataSetChanged()

                                return


                            } else {

                                val checkValidation = checkValidation()
                                if (!checkValidation) {
                                    val requestAirSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.REQUEST_AIR_SERACH) as RequestAirSearch
                                    showWarringForMissMax(requestAirSearch)
                                    return
                                }

                            }

                            //validationCheckPassportMandatory
                            if (!validationCheckPassportMandatory(get)) {
                                return
                            }


                            get.isPassengerSleted = true

                            viewMode.updatePassenger(get)
                            viewMode.mListMutableLiveDPassengers.value?.set(position, get)
                            adapterForPassengers.notifyDataSetChanged()

                        }

                    }

                    override fun onLongItemClick(view: View, position: Int) {
                        // do whatever
                    }
                })
        )


    }

    private fun showDialogMesssageWithEditBoutton(message: String, model: Passenger) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.edit), DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()

                    AppStorageBox.put(applicationContext, AppStorageBox.Key.AIRTRICKET_EDIT_PASSENGER, model)
                    val intent = Intent(applicationContext, AddPassengerActivity::class.java)
                    intent.putExtra("isEditFlag", true)
                    startActivity(intent)

                })


        builder.show()
    }


    private fun initializationView() {
        try {
            resposeAirPriceSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.ResposeAirPriceSearch) as ResposeAirPriceSearch

            var allAirportCodeString = ""

            resposeAirPriceSearch.data?.results?.get(0)?.segments?.forEach {
                val airportCode = it.origin.airport.airportCode
                val airportCode1 = it.destination.airport.airportCode

                allAirportCodeString = allAirportCodeString + "" + airportCode + " - " + airportCode1 + "\n"
            }

            tvNameOfDate.text = allAirportCodeString.trim()


        } catch (e: Exception) {

        }


        tvPoliciesAndBaggageAllowance.setOnClickListener {

            val get = resposeAirPriceSearch.data?.results?.get(0)?.fares

            AppStorageBox.put(applicationContext, AppStorageBox.Key.FARE_DATA, get)

            val s = Intent(this.applicationContext, BaggageAndPoliciesActiivty::class.java)
            startActivity(s)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
