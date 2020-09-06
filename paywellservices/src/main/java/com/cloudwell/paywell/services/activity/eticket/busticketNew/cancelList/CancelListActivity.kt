package com.cloudwell.paywell.services.activity.eticket.busticketNew.cancelList

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cancelList.adapter.CancelListAdapter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cancelList.adapter.CancelListAdapter.OnClick
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.BusTicketCancelOtpActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.RequestTicketInformationForCancel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.ResponseTicketInformationCancel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.TicketInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.dialog.BusTicketSuccess
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestRenerateOtpForCancelTicket
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_cancel_list.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CancelListActivity : BusTricketBaseActivity() {
    var busTransactionRV: RecyclerView? = null
    var allDataArrayList: ArrayList<*> = ArrayList<Any?>()
    var adapter: CancelListAdapter? = null
    var limit = 0
    private var date = ""
    lateinit var password: String
    lateinit var requestTicketInformationForCancel: RequestTicketInformationForCancel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_list)
        setToolbar(getString(R.string.title_ticket_cancel_list), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))

        password = intent.extras["password"] as String
        requestTicketInformationForCancel = intent.getParcelableExtra<RequestTicketInformationForCancel>("RequestTicketInformationForCancel")


    }

    override fun onResume() {
        super.onResume()
        val userName = mAppHandler.userName
        callToBookingListAPI()
    }


    fun callToBookingListAPI() {
        showProgressDialog()

        ApiUtils.getAPIServiceV2().ticketInformationForCancel(requestTicketInformationForCancel).enqueue(object : Callback<ResponseTicketInformationCancel?> {
            override fun onResponse(call: Call<ResponseTicketInformationCancel?>, response: Response<ResponseTicketInformationCancel?>) {
                dismissProgressDialog()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 200) {

                        setupAdapter(body)

                    } else {
                        // body?.message?.let { showBusTicketErrorDialog(it) }
                        finish()
                    }
                } else {
                    showBusTicketErrorDialog(getString(R.string.network_error), true)
                }
            }

            override fun onFailure(call: Call<ResponseTicketInformationCancel?>, t: Throwable) {
                toast(getString(R.string.network_error))
                dismissProgressDialog()

            }
        })

    }

    private fun setupAdapter(body: ResponseTicketInformationCancel) {
        if (body.ticketInfo?.size ?: 0 == 0) {
            finish()
        } else {
            busTransactionLogRV.setLayoutManager(LinearLayoutManager(applicationContext))
            adapter = body.ticketInfo?.let {
                CancelListAdapter(it, object : OnClick {
                    override fun onClick(ticketInfo: TicketInfo) {
                        //callCancelAPI(ticketInfo, password)

                        generateOtpForCancelTicket(ticketInfo, password)
                    }
                })
            }
            busTransactionLogRV.setAdapter(adapter)
        }

    }


    private fun generateOtpForCancelTicket(ticketInfo: TicketInfo, password: String) {

        showProgressDialog()

        val userName = mAppHandler.userName

        val m = RequestRenerateOtpForCancelTicket()
        m.trxId = "" + ticketInfo.trxId
        m.ticketNo = "" + ticketInfo.ticketNo
        m.username = userName
        m.password = password

        ApiUtils.getAPIServiceV2().generateOtpForCancelTicket(m).enqueue(object : Callback<ResponseTicketInformationCancel?> {
            override fun onResponse(call: Call<ResponseTicketInformationCancel?>, response: Response<ResponseTicketInformationCancel?>) {
                dismissProgressDialog()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 205) {
                        val dialog = body?.message?.let {
                            BusTicketSuccess(it, object : BusTicketSuccess.OnClick {
                                override fun onClick() {
                                    val intent = Intent(this@CancelListActivity, BusTicketCancelOtpActivity::class.java).also {
                                        it.putExtra("data", ticketInfo)
                                        it.putExtra("pin", password)
                                    }
                                    startActivity(intent)

                                }
                            })
                        }
                        dialog?.show(supportFragmentManager, "dialog");

                    } else {
                        body?.message?.let { showBusTicketErrorDialog(it) }
                    }
                } else {
                    showBusTicketErrorDialog(getString(R.string.network_error))
                }

            }

            override fun onFailure(call: Call<ResponseTicketInformationCancel?>, t: Throwable) {
                toast(getString(R.string.network_error))
                dismissProgressDialog()

            }
        })

    }
}


