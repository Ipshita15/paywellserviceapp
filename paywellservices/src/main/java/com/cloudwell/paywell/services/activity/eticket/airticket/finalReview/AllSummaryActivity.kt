package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
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
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.fragment.BookingFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.fragment.BookingSuccessDialog
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResAirPreBooking
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel.AllSummaryActivityViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel.view.AllSummaryStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.AddPassengerActivity
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.all_summaray_bottom_sheet.*


class AllSummaryActivity : AirTricketBaseActivity() {


    var totalJourneyinMiliSecound = 0L

    private lateinit var mViewModel: AllSummaryActivityViewModel

    lateinit var passengerIDS: String

    var userPinNumber = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_summaray)

        setToolbar(getString(R.string.title_all_summary))


//        passengerIDS = AppStorageBox.get(applicationContext, AppStorageBox.Key.SELETED_PASSENGER_IDS) as String

        passengerIDS = "1"

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


    }

    private fun handleConfirm() {

        askForPin()


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

        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)

            if (pinNoET.text.toString().length != 0) {
                dialogInterface.dismiss()
                userPinNumber = pinNoET.text.toString()

                if (isInternetConnection) {
                    // new TopUpAsync().execute();
                    airBookingAPI(userPinNumber)
                } else {
                    showNoInternetConnectionFound()
                }
            } else {
                showSnackMessageWithTextMessage(getString(R.string.pin_no_error_msg))
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private fun airBookingAPI(piN_NO: String) {

        mViewModel.callAirPreBookingAPI(piN_NO, passengerIDS, isInternetConnection)

    }

    private fun initViewModel(passengerIDS: String) {
        mViewModel = ViewModelProviders.of(this).get(AllSummaryActivityViewModel::class.java)

        mViewModel.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
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

    private fun handleViewStatus(status: AllSummaryStatus) {
        if (status.isShowProcessIndicatior) {
            showProgressDialog()
        }

        if (!status.isShowProcessIndicatior) {
            dismissProgressDialog()
        }

        if (!status.noSerachFoundMessage.equals("")) {
            showSnackMessageWithTextMessage(status.noSerachFoundMessage)
        }


        if (status.resAirPreBooking != null && status.isBooking) {

            AppStorageBox.put(applicationContext, AppStorageBox.Key.AIR_BOOKKING, status.resAirPreBooking)

            val dialogFragment = BookingSuccessDialog()
            dialogFragment.show(supportFragmentManager, "dialog")
        }


        if (status.resAirPreBooking != null) {

            val dialogFragment = BookingFragment()
            dialogFragment.bookingDialogListener = object : BookingFragment.BookingDialogListener {
                override fun onBooking(resAirPreBooking: ResAirPreBooking) {

                    AppStorageBox.put(applicationContext, AppStorageBox.Key.AIR_PRE_BOOKKING, status.resAirPreBooking)
                    mViewModel.callAirBookingAPI(piN_NO = userPinNumber, passengerIDS = passengerIDS, internetConnection1 = isInternetConnection)

                }

            }
            AppStorageBox.put(applicationContext, AppStorageBox.Key.AIR_PRE_BOOKKING, status.resAirPreBooking)


            dialogFragment.show(supportFragmentManager, "dialog")

        }


    }


    private fun initializationView() {


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
