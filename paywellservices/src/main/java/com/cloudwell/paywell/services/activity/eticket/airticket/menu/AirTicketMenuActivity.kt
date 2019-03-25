package com.cloudwell.paywell.services.activity.eticket.airticket.menu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.RadioButton
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketMainActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.BookingStatusActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.BookingCancelActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.TranstationLogActivity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import kotlinx.android.synthetic.main.activity_air_tricket_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AirTicketMenuActivity : AirTricketBaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private val KEY_TAG = AirTicketMenuActivity::class.java.getName()
    private val BOOKING_TAG = "BOOKING"
    private val TRX_TAG = "TRX_LOG"

    lateinit var mConstraintLayout: ConstraintLayout

    private var mAppHandler: AppHandler? = null
    internal var radioButton_five: RadioButton? = null
    internal var radioButton_ten: RadioButton? = null
    internal var radioButton_twenty: RadioButton? = null
    internal var radioButton_fifty: RadioButton? = null
    internal var radioButton_hundred: RadioButton? = null
    internal var radioButton_twoHundred: RadioButton? = null
    var selectedLimit = 5
    lateinit var cd: ConnectionDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_air_tricket_menu)
        setToolbar(getString(R.string.home_eticket_air))
        btSerach.setOnClickListener(this)
        btCencel.setOnClickListener(this)
        btTransationLog.setOnClickListener(this)

        btBooking.setOnClickListener(this)

        cd = ConnectionDetector(AppController.getContext())
        mConstraintLayout = findViewById(R.id.constraintLayoutBookingList)
        mAppHandler = AppHandler.getmInstance(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        selectedLimit = 5
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btSerach -> {
                startActivity(Intent(applicationContext, AirTicketMainActivity::class.java))
            }

            R.id.btCencel -> {
                startActivity(Intent(applicationContext, BookingCancelActivity::class.java))
            }

            R.id.btTransationLog -> {
                showLimitPrompt()
//                startActivity(Intent(applicationContext, TransactionLogActivity::class.java))
            }

            R.id.btBooking -> {
                submitBookingListRequest(200, BOOKING_TAG)
            }
        }
    }

    private fun submitBookingListRequest(limit: Int, tag: String) {
        showProgressDialog()

//        val username = mAppHandler!!.imeiNo
        val username = "cwntcl"

        Log.e("logTag", username + " " + limit)

        val responseBodyCall = ApiUtils.getAPIService().callAirBookingListSearch(username, limit)

        responseBodyCall.enqueue(object : Callback<BookingList> {
            override fun onResponse(call: Call<BookingList>, response: Response<BookingList>) {
                dismissProgressDialog()

                if (response.body()!!.status.compareTo(200) == 0) {
                    if (tag.equals(TRX_TAG)) {
                        AppStorageBox.put(applicationContext, AppStorageBox.Key.AIRTICKET_BOOKING_RESPONSE, response.body())

                        val intent = Intent(application, TranstationLogActivity::class.java)
                        intent.putExtra("tag", tag)
                        startActivity(intent)
                    } else {
                        AppStorageBox.put(applicationContext, AppStorageBox.Key.AIRTICKET_BOOKING_RESPONSE, response.body())

                        val intent = Intent(application, BookingStatusActivity::class.java)
                        intent.putExtra("tag", tag)
                        startActivity(intent)
                    }


                } else {
                    showReposeUI(response.body()!!)
                }
            }

            override fun onFailure(call: Call<BookingList>, t: Throwable) {
                dismissProgressDialog()
                Log.d(KEY_TAG, "onFailure:")
                val snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        })
    }

    private fun showReposeUI(response: BookingList) {
        val builder = AlertDialog.Builder(this@AirTicketMenuActivity)
        builder.setTitle("Result")
        builder.setMessage(response.getMessage())
        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            dialogInterface.dismiss()
//            onBackPressed()
        }
        val alert = builder.create()
        alert.show()

    }

    private fun showLimitPrompt() {
        val dialog = AppCompatDialog(this)
        dialog.setTitle(R.string.log_limit_title_msg)
        dialog.setContentView(R.layout.dialog_trx_limit)

        val btn_okay = dialog.findViewById<Button>(R.id.buttonOk)
        val btn_cancel = dialog.findViewById<Button>(R.id.cancelBtn)

        radioButton_five = dialog.findViewById(R.id.radio_five)
        radioButton_ten = dialog.findViewById(R.id.radio_ten)
        radioButton_twenty = dialog.findViewById(R.id.radio_twenty)
        radioButton_fifty = dialog.findViewById(R.id.radio_fifty)
        radioButton_hundred = dialog.findViewById(R.id.radio_hundred)
        radioButton_twoHundred = dialog.findViewById(R.id.radio_twoHundred)

        radioButton_five?.setOnCheckedChangeListener(this)
        radioButton_ten?.setOnCheckedChangeListener(this)
        radioButton_twenty?.setOnCheckedChangeListener(this)
        radioButton_fifty?.setOnCheckedChangeListener(this)
        radioButton_hundred?.setOnCheckedChangeListener(this)
        radioButton_twoHundred?.setOnCheckedChangeListener(this)

        assert(btn_okay != null)
        btn_okay!!.setOnClickListener {
            dialog.dismiss()
            if (cd.isConnectingToInternet()) {
                submitBookingListRequest(selectedLimit, TRX_TAG)
            } else {
                val snackbar = Snackbar.make(mConstraintLayout, resources.getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }
        assert(btn_cancel != null)
        btn_cancel!!.setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(true)
        dialog.show()
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            if (buttonView.id == R.id.radio_five) {
                selectedLimit = 5
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_ten) {
                selectedLimit = 10
                radioButton_five?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_twenty) {
                selectedLimit = 20
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_fifty) {
                selectedLimit = 50
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_hundred) {
                selectedLimit = 100
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_twoHundred) {
                selectedLimit = 200
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
            }
        }
    }
}
