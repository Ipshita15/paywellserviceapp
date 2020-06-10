package com.cloudwell.paywell.services.activity.eticket.busticketNew.cancelList

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cancelList.adapter.CancelListAdapter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cancelList.adapter.CancelListAdapter.OnClick
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.RequestTicketInformationForCancel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.ResponseTicketInformationCancel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.TicketInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.dialog.BusTicketSuccess
import com.cloudwell.paywell.services.activity.eticket.busticketNew.menu.BusTicketMenuActivity
import com.cloudwell.paywell.services.app.AppHandler
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_list)
        setToolbar(getString(R.string.title_ticket_cancel_list), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))
        val ticketInfo1 = intent.extras["model"] as ResponseTicketInformationCancel
        val password = intent.extras["password"] as String

        busTransactionLogRV.setLayoutManager(LinearLayoutManager(this))
        adapter = ticketInfo1.ticketInfo?.let {
            CancelListAdapter(it, object : OnClick {
                override fun onClick(ticketInfo: TicketInfo) {
                    callCancelAPI(ticketInfo, password)
                }
            })
        }
        busTransactionLogRV.setAdapter(adapter)
        val userName = AppHandler.getmInstance(this).userName
        val skey = ApiUtils.KEY_SKEY
    }

    private fun callCancelAPI(ticketInfo: TicketInfo, password: String) {

        showProgressDialog()

        val userName = mAppHandler.userName


        val m = RequestTicketInformationForCancel()
        m.trxId = ticketInfo.trxId
        m.ticketNo = ticketInfo.ticketNo
        m.username = userName
        m.password = password

        ApiUtils.getAPIServiceV2().cancelTicket(m).enqueue(object : Callback<ResponseTicketInformationCancel?> {
            override fun onResponse(call: Call<ResponseTicketInformationCancel?>, response: Response<ResponseTicketInformationCancel?>) {
                dismissProgressDialog()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 200) {
                        val dialog = body?.message?.let {
                            BusTicketSuccess(it, object : BusTicketSuccess.OnClick {
                                override fun onClick() {
                                    val intent = Intent(this@CancelListActivity, BusTicketMenuActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)
                                    finish()
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


