package com.cloudwell.paywell.services.activity.eticket.airticket.reIssueTicket

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.CancellationStatusMessageFragment
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.constant.AllConstant
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.google.gson.JsonObject
import mehdi.sakout.fancybuttons.FancyButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActionRequestTicketActivity : AirTricketBaseActivity() {
    private var bookingCancelAdapter: ArrayAdapter<*>? = null
    private val cancelReasonList = ArrayList<String>()
    private var bookingIdET: EditText? = null
    private var bookingCancelReasonSPNR: Spinner? = null
    private var cancelBookingBtn: FancyButton? = null
    private var cd: ConnectionDetector? = null
    private var PIN_NO = "unknown"
    private var cancelMainLayout: ConstraintLayout? = null
    private var mAppHandler: AppHandler? = null
    private var bookingId = String()
    private var title = String()

//    var mCancellationFee: Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cencel_booking)

        if (supportActionBar != null) {

        }
        if (intent.getStringExtra(KEY_BOOKING_ID) != null) {
            bookingId = intent.getStringExtra(KEY_BOOKING_ID)
        }

        if (intent.getStringExtra(KEY_TITLE) != null) {
            title = intent.getStringExtra(KEY_TITLE)
            supportActionBar!!.title = title
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        cd = ConnectionDetector(applicationContext)
        mAppHandler = AppHandler.getmInstance(applicationContext)

        cancelMainLayout = findViewById(R.id.cancelMainLayout)
        bookingIdET = findViewById(R.id.bookingIdET)
        bookingCancelReasonSPNR = findViewById(R.id.cancelReasonSPNR)


        cancelBookingBtn = findViewById(R.id.cancelBookingBtn)
        cancelReasonList.add("Select your reason")
        cancelReasonList.add("Have another work")
        cancelReasonList.add("Travelling reason accomplished")
        cancelReasonList.add("Have to travel another city")
        cancelReasonList.add("Other")
        bookingCancelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cancelReasonList)
        bookingCancelReasonSPNR!!.adapter = bookingCancelAdapter


        bookingIdET!!.setText(model.bookingId)
        bookingIdET!!.setKeyListener(null)

        cancelBookingBtn!!.setText(title)
        cancelBookingBtn!!.setOnClickListener {
            if (!bookingIdET!!.text.toString().isEmpty() && bookingCancelReasonSPNR!!.selectedItem.toString() != "Select your reason") {

                askForPin(bookingIdET!!.text.toString(), bookingCancelReasonSPNR!!.selectedItem.toString(), title)

            } else {

                hideUserKeyboard()

                val snackbar = Snackbar.make(cancelMainLayout!!, "Please Enter all the fields first", Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }

    }

    private fun submitCancelTicketRequest(userName: String, pass: String, bookingId: String, cancelReason: String, apiFormat: String) {
        showProgressDialog();

        ApiUtils.getAPIService().cancelTicket(userName, pass, bookingId, cancelReason, apiFormat).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val message = jsonObject!!.get("message").asString
                        if (jsonObject.get("status").asInt == 200) {
                            showMsg(message)

                        } else {
                            showMsg(message)
                        }

                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@ActionRequestTicketActivity, "Network error!!!", Toast.LENGTH_SHORT).show()
                progressDialog.hide()
            }
        })
    }


    private fun askForPin(bookingId: String, cancelReason: String, title: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.pin_no_title_msg)

        val pinNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
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
                PIN_NO = pinNoET.text.toString()
                if (cd!!.isConnectingToInternet) {
                    val userName = mAppHandler!!.imeiNo

                    if (title.equals(AllConstant.Action_Ticket_Cancel)) {
                        submitCancelTicketRequest(userName, PIN_NO, bookingId, cancelReason, "json")
                    } else if (title.equals(AllConstant.Action_Reissue_or_Reschedule)) {
                        submitReissueAndReschedule(userName, PIN_NO, bookingId, cancelReason, model.searchId, model.resultId, "json")
                    }

                } else {
                    val snackbar = Snackbar.make(cancelMainLayout!!, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                    snackbar.show()
                }
            } else {
                val snackbar = Snackbar.make(cancelMainLayout!!, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG)
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

    private fun submitReissueAndReschedule(userName: String, pass: String, bookingId: String, cancelReason: String, searchId: String, resultID: String, apiFormat: String) {
        showProgressDialog()


        ApiUtils.getAPIService().reIssueTicket(userName, pass, bookingId, cancelReason, searchId, resultID, apiFormat).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dismissProgressDialog()

                if (response.isSuccessful) {

                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val message = jsonObject!!.get("message_details").asString
                        if (jsonObject.get("status").asInt == 200) {
                            showMsg(message)
                        } else {
                            showMsg(message)
                        }

                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@ActionRequestTicketActivity, "Network error!!!", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        })

    }

    private fun showMsg(msg: String) {

        val priceChangeFragment = CancellationStatusMessageFragment()
        CancellationStatusMessageFragment.message = msg

        priceChangeFragment.show(supportFragmentManager, "dialog")

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }


//    private fun callCancelMapping(userName: String, bookingId: String) {
//
//        showProgressDialog()
//
//        ApiUtils.getAPIService().getCancelMap(userName, bookingId).enqueue(object : Callback<ResCancellationMapping> {
//            override fun onResponse(call: Call<ResCancellationMapping>, response: Response<ResCancellationMapping>) {
//                dismissProgressDialog()
//                assert(response.body() != null)
//                if (response.body()!!.status == 200) {
//                    showUserCencelData(response.body())
//
//                } else {
//                    showSnackMessageWithTextMessage(response.body()!!.message)
//                }
//            }
//
//            override fun onFailure(call: Call<ResCancellationMapping>, t: Throwable) {
//                dismissProgressDialog()
//                showSnackMessageWithTextMessage(getString(R.string.please_try_again))
//
//            }
//
//
//        })
//    }
//
//    private fun showUserCencelData(r: ResCancellationMapping?) {
//
//        val priceChangeFragment = CancellationFeeFragment()
//        CancellationFeeFragment.resCencelMaping = r!!
//
//        priceChangeFragment.setOnClickHandlerTest(object : CancellationFeeFragment.OnClickHandler {
//            override fun onClickActionIssueTicket(cancellationFee: Double) {
//                mCancellationFee = cancellationFee
//                hiddenSoftKeyboard()
//
//
//                askForPin(bookingIdET!!.text.toString(), bookingCancelReasonSPNR!!.selectedItem.toString())
//
//
//            }
//        })
//
//        priceChangeFragment.show(supportFragmentManager, "dialog")
//
//
//    }

    companion object {
        var KEY_BOOKING_ID = "Booking_Id"
        var KEY_TITLE = "KEY_TITLE"
        lateinit var model: Datum
    }

}
