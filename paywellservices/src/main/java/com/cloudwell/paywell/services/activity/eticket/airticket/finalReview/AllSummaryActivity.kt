package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.adapter.AdapterForPassengersFinalList
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.adapter.AirportListAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.fragment.BookingStatusFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.fragment.BookingSuccessDialog
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.fragment.RePriceStatusFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResAirPreBooking
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel.AllSummaryActivityViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel.view.AllSummaryStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.FlightSearchViewActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.menu.AirTicketMenuActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.AddPassengerActivity
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.all_summaray_bottom_sheet.*
import kotlinx.android.synthetic.main.contant_summary.*


class AllSummaryActivity : AirTricketBaseActivity() {


    private lateinit var mViewModel: AllSummaryActivityViewModel

    lateinit var passengerIDS: String

    var userPinNumber = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_summaray)
        setToolbar(getString(R.string.title_all_summary))

        passengerIDS = AppStorageBox.get(applicationContext, AppStorageBox.Key.SELETED_PASSENGER_IDS) as String

        initializationView()
        initilizationReviewBottomSheet()
        initViewModel(passengerIDS)


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

        ivConfirm.setOnClickListener {
            handleConfirm()
        }

        tvConfirm.setOnClickListener {
            handleConfirm()
        }


        tvCancel.setOnClickListener {
            handleCancel()
        }

        ivCancle.setOnClickListener {

        }


    }

    private fun handleCancel() {

        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val intent = Intent(this, AirTicketMenuActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)

                }

                DialogInterface.BUTTON_NEGATIVE -> {
                }
            }//Yes button clicked
            //No button clicked
        }

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure?").setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show()
    }

    private fun handleConfirm() {

        if (isInternetConnection) {
            // new TopUpAsync().execute();
            airPreBookingAPI()
        } else {
            showNoInternetConnectionFound()
        }

        // askForPin()


    }

    private fun askForPin() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.pin_no_title_msg)

        val pinNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        pinNoET.gravity = Gravity.CENTER_HORIZONTAL
        pinNoET.layoutParams = lp
        pinNoET.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_PASSWORD
        pinNoET.transformationMethod = PasswordTransformationMethod.getInstance()
        builder.setView(pinNoET)

        builder.setPositiveButton(com.cloudwell.paywell.services.R.string.okay_btn) { dialogInterface, id ->
            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)

            if (pinNoET.text.toString().length != 0) {
                dialogInterface.dismiss()
                userPinNumber = pinNoET.text.toString()

                mViewModel.callAirBookingAPI(piN_NO = userPinNumber, passengerIDS = passengerIDS, internetConnection1 = isInternetConnection)


            } else {
                showSnackMessageWithTextMessage(getString(com.cloudwell.paywell.services.R.string.pin_no_error_msg))
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private fun airPreBookingAPI() {

        mViewModel.callAirPreBookingAPI(passengerIDS, isInternetConnection)

    }

    private fun initViewModel(passengerIDS: String) {
        mViewModel = ViewModelProviders.of(this).get(AllSummaryActivityViewModel::class.java)

        mViewModel.baseViewStatus.observe(this, Observer {
            handleViewCommonStatus(it)
        })

        mViewModel.mViewStatus.observe(this, Observer {
            it?.let { it1 -> handleViewStatus(it1) }
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
        val recyclerView = findViewById(R.id.recycleviewForPassenger) as RecyclerView


        val glm = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = glm
        glm.isAutoMeasureEnabled = true

        recyclerView.layoutManager = glm


        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);


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

        recyclerView.adapter = recyclerListAdapter;


    }

    private fun handleViewStatus(status: AllSummaryStatus) {
        if (status.isShowProcessIndicatior) {
            showProgressDialog()
        }

        if (!status.isShowProcessIndicatior) {
            dismissProgressDialog()
        }

        if (!status.noSerachFoundMessage.equals("")) {
            showDialogMesssage(status.noSerachFoundMessage)
        }


        if (status.resBookingAPI != null) {

            AppStorageBox.put(applicationContext, AppStorageBox.Key.AIR_BOOKKING, status.resBookingAPI)

            val dialogFragment = BookingSuccessDialog()
            dialogFragment.show(supportFragmentManager, "dialog")
        } else {


            if (status.resAirPreBooking != null) {

                val dialogFragment = BookingStatusFragment()
                dialogFragment.bookingDialogListener = object : BookingStatusFragment.BookingDialogListener {
                    override fun onBooking(resAirPreBooking: ResAirPreBooking) {

                        AppStorageBox.put(applicationContext, AppStorageBox.Key.AIR_PRE_BOOKKING, status.resAirPreBooking)
                        askForPin()

                    }

                }
                AppStorageBox.put(applicationContext, AppStorageBox.Key.AIR_PRE_BOOKKING, status.resAirPreBooking)

                dialogFragment.show(supportFragmentManager, "dialog")

            } else if (!status.RePriceStatus.equals("")) {

                val rePriceStatusFragment = RePriceStatusFragment()
                RePriceStatusFragment.rePriceStatus = "" + status.RePriceStatus

                rePriceStatusFragment.bookingDialogListener = object : RePriceStatusFragment.BookingDialogListener {
                    override fun onClick() {
                        backToSearchPage()

                    }
                }

                rePriceStatusFragment.show(supportFragmentManager, "dialog")

            }

        }

        if (status.test.equals("done")) {
            mViewModel.callFileUploadAPI()
        }


    }

    private fun backToSearchPage() {
        val intent = Intent(this, FlightSearchViewActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent)


    }


    private fun initializationView() {
        val resposeAirPriceSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.ResposeAirPriceSearch) as ResposeAirPriceSearch
        val airline = resposeAirPriceSearch.data?.results?.get(0)?.segments?.get(0)?.airline


        val airportList = mutableListOf<Airport>()


        var text = ""

        val toList = resposeAirPriceSearch.data?.results?.get(0)?.segments?.toList()

        for ((i, value) in toList?.withIndex()!!) {
            airportList.add(value.origin.airport)
            airportList.add(value.destination.airport)

            val arrivalTime = value.destination.arrTime
            val departureTime = value.origin.depTime

            val arrTimeSplit = arrivalTime?.split("T")
            val departureTimeSplit = departureTime?.split("T")


            text = text + "Departure Time " + (i + 1) + ": " + arrTimeSplit!!.get(0) + " " + arrTimeSplit.get(1) + "\n"
            text = text + "Arrival Time " + (i + 1) + ": " + departureTimeSplit!!.get(0) + " " + departureTimeSplit!!.get(1) + "\n\n"
        }

        text = text.substring(0, text.length - 2)
        tveDpartureTime.text = "" + text




        recyclerViewAirports.setNestedScrollingEnabled(false)
        recyclerViewAirports.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerViewAirports.setLayoutManager(mLayoutManager)
        recyclerViewAirports.setItemAnimator(DefaultItemAnimator())


        val recyclerListAdapter = AirportListAdapter(applicationContext, airportList)
        recyclerViewAirports.adapter = recyclerListAdapter




        tvAirlineCode.text = getString(com.cloudwell.paywell.services.R.string.airline_code) + " ${airline?.airlineCode}"
        tvAirlesscode.text = getString(com.cloudwell.paywell.services.R.string.airport_name) + " ${airline?.airlineName}"
        tvFlghtNumber.text = getString(com.cloudwell.paywell.services.R.string.flight_number) + " ${airline?.flightNumber}"
        tvBookingClass.text = getString(com.cloudwell.paywell.services.R.string.booking_class) + " ${airline?.bookingClass}"
        tvOperatorCarrier.text = getString(com.cloudwell.paywell.services.R.string.operating_carrier) + " ${airline?.operatingCarrier}"
        tvCabinClass.text = getString(com.cloudwell.paywell.services.R.string.cabin_class) + " ${airline?.cabinClass}"

//        val segments = resposeAirPriceSearch.data?.results?.get(0)?.segments
//        if (segments!!.size!! > 1) {
//            val arrTime = segments?.get(0)?.destination?.arrTime
//            val depTime = segments?.get(segments.size - 1)?.origin?.depTime
//
//            val arrTimeSplit = arrTime?.split("T")
//            val depTimeSplit = depTime?.split("T")
//
//            val departDate = DateUtils.getFormatDate(depTimeSplit!![0])
//            val arrivalDate = DateUtils.getFormatDate(arrTimeSplit!![0])
//
//            tveDpartureTime.text = getString(R.string.depart_time_) + " " + departDate + ", " + depTimeSplit[1]
//            tvArrivalTime.text = getString(R.string.arrival_time) + " " + arrivalDate + ", " + arrTimeSplit[1]
//        } else {
//
//
//            val arrTime = segments?.get(0)?.destination?.arrTime
//            val depTime = segments?.get(0)?.origin?.depTime
//            val arrTimeSplit = arrTime?.split("T")
//            val depTimeSplit = depTime?.split("T")
//
//            val departDate = DateUtils.getFormatDate(depTimeSplit!![0])
//            val arrivalDate = DateUtils.getFormatDate(arrTimeSplit!![0])
//
//            tveDpartureTime.text = getString(R.string.depart_time_) + " " + departDate + ", " + depTimeSplit[1]
//            tvArrivalTime.text = getString(R.string.arrival_time) + " " + arrivalDate + ", " + arrTimeSplit[1]
//
//        }


        tvBaggage.text = getString(com.cloudwell.paywell.services.R.string.baggage) + resposeAirPriceSearch.data?.results?.get(0)?.segments?.get(0)?.baggage + " " + getString(com.cloudwell.paywell.services.R.string.kg_per_adult)


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
