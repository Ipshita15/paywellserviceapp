package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.all_summaray_bottom_sheet.*
import kotlinx.android.synthetic.main.contant_summary_contant.*
import su.j2e.rvjoiner.JoinableAdapter
import su.j2e.rvjoiner.JoinableLayout
import su.j2e.rvjoiner.RvJoiner


class AllSummaryActivity : AirTricketBaseActivity() {


    private lateinit var mViewModel: AllSummaryActivityViewModel

    lateinit var passengerIDS: String

    var userPinNumber = ""

    lateinit var rvJoiner: RvJoiner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_summaray_new)
        setToolbar(getString(R.string.title_all_summary))

        passengerIDS = AppStorageBox.get(applicationContext, AppStorageBox.Key.SELETED_PASSENGER_IDS) as String


        initilizationReviewBottomSheet()
        initViewModel()

    }

    override fun onResume() {
        super.onResume()

        rvJoiner = RvJoiner(false)//auto update ON, stable ids ON
        initializationViewNew()
        mViewModel.init(passengerIDS)

    }

    private fun initializationViewNew() {
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


            text = text + "Departure Time " + (i + 1) + ": " + arrTimeSplit!!.get(0) + " " + departureTimeSplit!!.get(1) + "\n"
            text = text + "Arrival Time " + (i + 1) + ": " + departureTimeSplit!!.get(0) + " " + arrTimeSplit.get(1) + "\n\n"
        }

        text = text.substring(0, text.length - 2)

        val recycler_view = findViewById<View>(R.id.recycler_view) as RecyclerView
        setLinearLayoutManager(recycler_view)


        val recyclerListAdapter = AirportListAdapter(this, airportList)
        rvJoiner.add(JoinableAdapter(recyclerListAdapter))


        rvJoiner.add(JoinableLayout(R.layout.contant_summary, object : JoinableLayout.Callback {
            @SuppressLint("SetTextI18n")
            override fun onInflateComplete(view: View?, parent: ViewGroup?) {
                val tvAirlineCode = view?.findViewById(R.id.tvAirlineCode) as TextView
                val tvAirlesscode = view?.findViewById(R.id.tvAirlesscode) as TextView
                val tvFlghtNumber = view?.findViewById(R.id.tvFlghtNumber) as TextView
                val tvBookingClass = view?.findViewById(R.id.tvBookingClass) as TextView
                val tvOperatorCarrier = view?.findViewById(R.id.tvOperatorCarrier) as TextView
                val tvCabinClass = view?.findViewById(R.id.tvCabinClass) as TextView
                val tveDpartureTime = view?.findViewById(R.id.tveDpartureTime) as TextView
                val tvBaggage = view?.findViewById(R.id.tvBaggage) as TextView


                tvAirlineCode.text = getString(R.string.airline_code) + " ${airline?.airlineCode}"
                tvAirlesscode.text = getString(R.string.airport_name) + " ${airline?.airlineName}"
                tvFlghtNumber.text = getString(R.string.flight_number) + " ${airline?.flightNumber}"
                tvBookingClass.text = getString(R.string.booking_class) + " ${airline?.bookingClass}"
                tvOperatorCarrier.text = getString(R.string.operating_carrier) + " ${airline?.operatingCarrier}"
                tvCabinClass.text = getString(R.string.cabin_class) + " ${airline?.cabinClass}"




                tvBaggage.text = getString(R.string.baggage) + resposeAirPriceSearch.data?.results?.get(0)?.segments?.get(0)?.baggage + " " + getString(com.cloudwell.paywell.services.R.string.kg_per_adult)


                tveDpartureTime.text = "" + text
            }

        }))


    }

    private fun setLinearLayoutManager(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)
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

    private fun initViewModel() {
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


    private fun handlePassengerList(it: MutableList<Passenger>) {


        val adapterForPassengersFinalList = AdapterForPassengersFinalList(this, it, object : AdapterForPassengersFinalList.OnClickListener {

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

        rvJoiner.add(JoinableAdapter(adapterForPassengersFinalList))
        recycler_view.adapter = rvJoiner.adapter

    }

    private fun handleViewStatus(status: AllSummaryStatus) {
        if (status.isShowProcessIndicatior) {
            showProgressDialog()
        }

        if (!status.isShowProcessIndicatior) {
            dismissProgressDialog()
        }

        if (!status.noSerachFoundMessage.equals("")) {
            showDialogMessage(status.noSerachFoundMessage)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
