package com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cancelList.CancelListActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.RequestTicketInformationForCancel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.ResponseTicketInformationCancel
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import kotlinx.android.synthetic.main.activity_cencel_booking_bus.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusCancelActiivty : BusTricketBaseActivity() {

    private var cd: ConnectionDetector? = null
    private var PIN_NO = "unknown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cencel_booking_bus)

        setToolbar("Cancel Ticket", resources.getColor(R.color.bus_ticket_toolbar_title_text_color))


        cd = ConnectionDetector(applicationContext)
        mAppHandler = AppHandler.getmInstance(applicationContext)

        btSumbit.setOnClickListener {
            val transstionId = etTicketId.text.toString().trim()
            if (transstionId.equals("")) {
                toast("Please input a valid transition ID")
            } else {
                askForPin(etTicketId.text.toString())
            }

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
                    val userName = mAppHandler.userName
                    callToBookingListAPI(PIN_NO, bookingId, userName)
                } else {
                    showBusTicketErrorDialog(getString(R.string.connection_error_msg), false)

                }
            } else {

                showBusTicketErrorDialog(getString(R.string.pin_no_error_msg), false)


            }
        }
        builder.setNegativeButton(R.string.cancel_btn) { dialogInterface, i -> dialogInterface.dismiss() }
        val alert = builder.create()
        alert.show()
    }


    fun callToBookingListAPI(password: String, trxId: String, userName: String) {

        showProgressDialog()

        val m = RequestTicketInformationForCancel()
        m.password = password
        m.trxId = trxId
        m.username = userName

        ApiUtils.getAPIServiceV2().ticketInformationForCancel(m).enqueue(object : Callback<ResponseTicketInformationCancel?> {
            override fun onResponse(call: Call<ResponseTicketInformationCancel?>, response: Response<ResponseTicketInformationCancel?>) {

                dismissProgressDialog()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 200) {


                        startActivity(Intent(this@BusCancelActiivty, CancelListActivity::class.java).also {
                            it.putExtra("model", body)
                            it.putExtra("password", password)
                            it.putExtra("RequestTicketInformationForCancel", m)
                        })


                    } else {
                        body?.message?.let { showBusTicketErrorDialog(it) }
                    }
                } else {
                    showBusTicketErrorDialog(getString(R.string.network_error))
                }
                dismissProgressDialog()
            }

            override fun onFailure(call: Call<ResponseTicketInformationCancel?>, t: Throwable) {
                toast(getString(R.string.network_error))
                dismissProgressDialog()

            }
        })

    }

    private fun showNoDataFound() {


    }

}