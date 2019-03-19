package com.cloudwell.paywell.services.activity.eticket.airticket.menu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketMainActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.BookingMainActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_air_tricket_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AirTicketMenuActivity : AirTricketBaseActivity(), View.OnClickListener {

    private val KEY_TAG = AirTicketMenuActivity::class.java.getName()

    lateinit var mConstraintLayout: ConstraintLayout

    private var mAppHandler: AppHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_air_tricket_menu)
        setToolbar(getString(R.string.home_eticket_air))
        btSerach.setOnClickListener(this)
        btBooking.setOnClickListener(this)

        mConstraintLayout = findViewById(R.id.constraintLayoutBookingList)
        mAppHandler = AppHandler.getmInstance(applicationContext)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btSerach -> {
                startActivity(Intent(application, AirTicketMainActivity::class.java))
            }

            R.id.btBooking -> {
                submitBookingListRequest()
            }
        }
    }

    private fun submitBookingListRequest() {
        showProgressDialog()

//        val username = mAppHandler!!.imeiNo
        val username = "cwntcl"
        val limit = 10
        val responseBodyCall = ApiUtils.getAPIService().callAirBookingListSearch(username, limit)

        responseBodyCall.enqueue(object : Callback<BookingList> {
            override fun onResponse(call: Call<BookingList>, response: Response<BookingList>) {
                dismissProgressDialog()

                if (response.body()!!.status.compareTo(200) == 0) {
                    val bundle = Bundle()
                    AppStorageBox.put(applicationContext, AppStorageBox.Key.AIRTICKET_BOOKING_RESPONSE, response.body())

                    val intent = Intent(application, BookingMainActivity::class.java)
//                    intent.putExtras(bundle)
                    intent.putParcelableArrayListExtra("list", response.body())
//                    intent.putExtra("Pokemon", response.body());
                    startActivity(intent)
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
            onBackPressed()
        }
        val alert = builder.create()
        alert.show()

    }

}
