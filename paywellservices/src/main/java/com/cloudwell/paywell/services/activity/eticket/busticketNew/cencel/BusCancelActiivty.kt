package com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.CancellationStatusMessageFragment
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_cencel_booking_bus.*

import mehdi.sakout.fancybuttons.FancyButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BusCancelActiivty : BusTricketBaseActivity() {
    private var bookingCancelAdapter: ArrayAdapter<*>? = null
    private val cancelReasonList = ArrayList<String>()


    private var cd: ConnectionDetector? = null
    private var PIN_NO = "unknown"
    private var cancelMainLayout: ConstraintLayout? = null

    private var bookingCancelId = String()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cencel_booking_bus)

        setToolbar("Cancel Ticket", resources.getColor(R.color.bus_ticket_toolbar_title_text_color))


        cd = ConnectionDetector(applicationContext)
        mAppHandler = AppHandler.getmInstance(applicationContext)

        // etTicketId.setText(bookingCancelId)
        btSumbit.setOnClickListener {
            askForPin(etTicketId.getText().toString());
        }
    }


    private fun askForPin(bookingId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.pin_no_title_msg)
        val pinNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        pinNoET.gravity = Gravity.CENTER_HORIZONTAL
        pinNoET.layoutParams = lp
        pinNoET.inputType = InputType.TYPE_CLASS_NUMBER or
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        pinNoET.transformationMethod = PasswordTransformationMethod.getInstance()
        builder.setView(pinNoET)
        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)
            if (pinNoET.text.toString().length != 0) {
                dialogInterface.dismiss()
                PIN_NO = pinNoET.text.toString()
                if (cd!!.isConnectingToInternet) {
                    val userName = mAppHandler!!.userName
                    //submitCancelRequest(userName, PIN_NO, bookingId, cancelReason, "json")
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


}