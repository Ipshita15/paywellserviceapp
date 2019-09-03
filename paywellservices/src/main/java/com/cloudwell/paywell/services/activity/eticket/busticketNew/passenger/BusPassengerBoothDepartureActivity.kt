package com.cloudwell.paywell.services.activity.eticket.busticketNew.passenger


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment.BusTicketConfirmFragment
import com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment.BusTicketConfirmSuccessfulMessageFragment
import com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment.BusTicketStatusFragment
import com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment.MyClickListener
import com.cloudwell.paywell.services.activity.eticket.busticketNew.menu.BusTicketMenuActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.*
import com.cloudwell.paywell.services.constant.AllConstant
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_bus_booth_departure.*
import org.json.JSONObject


class BusPassengerBoothDepartureActivity : BusTricketBaseActivity(), IbusTransportListView {
    override fun setBoardingPoint(allBoothNameInfo: MutableSet<String>) {


    }

    override fun showShowConfirmDialog(it: ResPaymentBookingAPI) {
        val t = BusTicketConfirmSuccessfulMessageFragment()
        BusTicketConfirmSuccessfulMessageFragment.model = it
        t.show(supportFragmentManager, "dialog")
        t.setOnClickListener(object : BusTicketConfirmSuccessfulMessageFragment.MyClickListener {
            override fun onClick() {
                val intent = Intent(applicationContext, BusTicketMenuActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)

            }
        })

    }


    override fun showSeatCheckAndBookingRepose(it: ResSeatCheckBookAPI) {

        Logger.v("", "")
        val t = BusTicketConfirmFragment()
        it.ticketInfoSeatBookAndCheck?.seats = seatLevel
        BusTicketConfirmFragment.ticketInfo = it.ticketInfoSeatBookAndCheck!!
        t.show(supportFragmentManager, "dialog")
        t.setOnClickListener(object : MyClickListener {
            override fun onClick() {
                askForPin(it)
            }
        })


    }


    override fun showErrorMessage(message: String) {

        val t = BusTicketStatusFragment()
        BusTicketStatusFragment.message = message
        t.show(supportFragmentManager, "dialog")

    }

    override fun showNoTripFoundUI() {


    }

    override fun setAdapter(it1: List<TripScheduleInfoAndBusSchedule>) {

    }

    override fun showProgress() {
        showProgressDialog()

    }

    override fun hiddenProgress() {
        dismissProgressDialog()
    }

    private lateinit var busListAdapter: Any
    private val allBoothInfo = mutableListOf<BoothInfo>()
    private val allBoothNameInfo = mutableListOf<String>()


    private lateinit var boothList: Spinner

    lateinit var viewMode: BusTransportViewModel
    internal lateinit var model: TripScheduleInfoAndBusSchedule
    internal lateinit var requestBusSearch: RequestBusSearch
    internal var totalAPIValuePrices: String = ""
    var seatLevel = ""
    var seatId = ""
    var password = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_booth_departure)
        setToolbar(getString(R.string.title_bus_passenger_booth), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))


        val stringExtra = intent.getStringExtra("jsonData")
        val requestBusSearchJson = intent.getStringExtra("requestBusSearch")
        seatLevel = intent.getStringExtra("seatLevel")
        seatId = intent.getStringExtra("seatId")
        val totalPrices = intent.getStringExtra("totalPrices")
        totalAPIValuePrices = intent.getStringExtra("totalAPIValuePrices")



        requestBusSearch = Gson().fromJson(requestBusSearchJson, RequestBusSearch::class.java)
        model = Gson().fromJson(stringExtra, TripScheduleInfoAndBusSchedule::class.java)

        val boothDepartureInfo = model.busSchedule?.booth_departure_info


        busSeatsTV.text = seatLevel
        tvTotalPrice.text = "TK." + totalPrices


        val jsonObject = JSONObject(boothDepartureInfo)
        jsonObject.keys().forEach {
            val model = jsonObject.get(it) as JSONObject
            val boothName = model.getString("booth_name")
            val boothDepartureTime = model.getString("booth_departure_time")
            allBoothInfo.add(BoothInfo(it, boothName, boothDepartureTime))
            allBoothNameInfo.add(boothName)
        }


        boothList = findViewById<Spinner>(R.id.boothList)
        busListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allBoothNameInfo)
        this.boothList.setAdapter(busListAdapter as ArrayAdapter<String>)


        hiddenSoftKeyboard()

        initViewModel()


        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (!s.toString().equals("")) {
                    if (s.matches(AllConstant.emailPattern.toRegex()) && s.length > 0) {
                        textInputLayoutmobilEmail.error = ""

                    } else {
                        textInputLayoutmobilEmail.error = getString(R.string.invalid_email)
                    }
                } else {
                    textInputLayoutmobilEmail.error = ""
                }


            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // other stuffs
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // other stuffs
            }
        })


        btn_search.setOnClickListener {
            handleBookingContinueBooking()
        }


    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(BusTransportViewModel::class.java)
        viewMode.setIbusTransportListView(this)

    }

    private fun handleBookingContinueBooking() {
        val fullName = fullNameTV.text.toString().trim()
        val mobileNumber = mobileNumberTV.text.toString().trim()
        val age = ageTV.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (fullName.equals("")) {
            textInputLayoutFirstName.error = getString(R.string.invalid_full_name)
            return
        } else {
            textInputLayoutFirstName.error = null
        }
        if (mobileNumber.equals("")) {
            textInputLayoutmobileNumber.error = getString(R.string.Invalid_mobile_number)
            return
        } else {
            textInputLayoutmobileNumber.error = null
        }

        if (age.equals("")) {
            textInputLayoutAge.error = getString(R.string.invalid_age)
            return
        } else {
            textInputLayoutAge.error = null
        }

        if (address.equals("")) {
            textInputLayoutAddress.error = getString(R.string.invalid_address)
            return
        } else {
            textInputLayoutAddress.error = null
        }


        if (!email.equals("")) {
            if (email.matches(AllConstant.emailPattern.toRegex()) && email.length > 0) {
                textInputLayoutmobilEmail.error = ""

            } else {
                textInputLayoutmobilEmail.error = getString(R.string.invalid_email)
                return
            }
        }

        val boothInfo = allBoothInfo.get(boothList.selectedItemPosition)



        viewMode.bookingAPI(isInternetConnection, model, requestBusSearch, boothInfo, seatLevel, seatId, totalAPIValuePrices)

    }

    private fun askForPin(it: ResSeatCheckBookAPI) {
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
                val PIN_NO = pinNoET.text.toString()
                if (isInternetConnection) {


                    // get selected radio button from radioGroup
                    val selectedId = radioGroup.getCheckedRadioButtonId()

                    // find the radiobutton by returned id
                    val radioButton = findViewById<RadioButton>(selectedId) as RadioButton

                    viewMode.callConfirmPayment(isInternetConnection,
                            it.transId,
                            fullNameTV.text.toString(),
                            mobileNumberTV.getText().toString(),
                            etAddress.text.toString(),
                            etEmail.text.toString(),
                            ageTV.text.toString(),
                            radioButton.text.toString(),
                            PIN_NO
                    )


                } else {
                    val snackbar = Snackbar.make(rootLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                    snackbar.show()
                }
            } else {
                val snackbar = Snackbar.make(rootLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }
        builder.setNegativeButton(R.string.cancel_btn) { dialogInterface, i -> dialogInterface.dismiss() }
        val alert = builder.create()
        alert.show()
    }
}
