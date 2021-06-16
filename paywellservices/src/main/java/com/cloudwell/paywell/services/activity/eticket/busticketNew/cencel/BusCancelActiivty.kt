package com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel

import android.content.Intent
import android.os.Bundle
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
                val userName = mAppHandler.userName
                callToBookingListAPI( etTicketId.text.toString(), userName)
            }

        }
    }


    fun callToBookingListAPI(trxId: String, userName: String) {

        showProgressDialog()

        val m = RequestTicketInformationForCancel()
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