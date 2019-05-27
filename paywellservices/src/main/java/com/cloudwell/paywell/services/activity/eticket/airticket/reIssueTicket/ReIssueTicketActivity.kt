package com.cloudwell.paywell.services.activity.eticket.airticket.reIssueTicket

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.CancellationStatusMessageFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.reIssueTicket.adapter.AdapterForPassengersReIssue
import com.cloudwell.paywell.services.activity.eticket.airticket.reIssueTicket.editReissuePassengerActivity.EditReissuePassengerActivity
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_reissue_ticket.*
import kotlinx.android.synthetic.main.activity_search_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import su.j2e.rvjoiner.RvJoiner
import kotlinx.android.synthetic.main.flight_list_item_new.recyclerView as recyclerView1

class ReIssueTicketActivity : AirTricketBaseActivity() {

    private val rvJoiner = RvJoiner(true)//auto update ON, stable ids ON

    companion object {
        lateinit var item: Datum
        lateinit var passengers: MutableList<Passenger>
        fun newIntent(context: Context, item: Datum): Intent {
            val intent = Intent(context, ReIssueTicketActivity::class.java)
            this.item = item
            this.passengers = item.passengers as MutableList<Passenger>
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reissue_ticket)
        setToolbar(getString(R.string.title_reissue))


    }

    override fun onResume() {
        super.onResume()

        initializationViewNew()
    }

    private fun initializationViewNew() {

        passengers.toMutableList().let {

            val recyclerListAdapter = AdapterForPassengersReIssue(this, it, object : AdapterForPassengersReIssue.OnClickListener {
                override fun onEditClick(model: Passenger, position: Int) {

                    val intent = Intent(applicationContext, EditReissuePassengerActivity::class.java)
                    intent.putExtra("id", position)
                    startActivity(intent)
                }

                override fun onDeleted(model: Passenger, position: Int) {

                }

            })


            val glm = LinearLayoutManager(applicationContext)
            recycler_view.layoutManager = glm
            recycler_view.itemAnimator = DefaultItemAnimator()
            recycler_view.setHasFixedSize(true)
            recycler_view.isNestedScrollingEnabled = true
            recycler_view.adapter = recyclerListAdapter

        }


        btReissueRequest.setOnClickListener {

            handleRequestForReSchuder()

        }

    }

    private fun handleRequestForReSchuder() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reschedule Reason")

        val pinNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        pinNoET.gravity = Gravity.CENTER_HORIZONTAL
        pinNoET.layoutParams = lp
        builder.setView(pinNoET)

        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)

            if (pinNoET.text.toString().length != 0) {
                dialogInterface.dismiss()
                var resoun = pinNoET.text.toString()
                if (isInternetConnection) {

                    askForPin(resoun)

                } else {
                    val snackbar = Snackbar.make(linearLayout12, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                    snackbar.show()
                }
            } else {
                val snackbar = Snackbar.make(linearLayout12, "Enter reschedule reason", Snackbar.LENGTH_LONG)
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

    private fun askForPin(cancelReason: String) {
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

                    val mAppHandler = AppHandler.getmInstance(application)
                    val userName = mAppHandler.imeiNo



                    submitRescheduleAPI(userName, PIN_NO, item.bookingId, cancelReason, "json")


                } else {
                    val snackbar = Snackbar.make(linearLayout3!!, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                    snackbar.show()
                }
            } else {
                val snackbar = Snackbar.make(linearLayout3!!, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG)
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

    private fun submitRescheduleAPI(userName: String, pass: String, bookingId: String?, cancelReason: String, apiFormat: String) {
        showProgressDialog()


        ApiUtils.getAPIService().reIssueTicket(userName, pass, bookingId, cancelReason, passengers).enqueue(object : Callback<JsonObject> {
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
                Toast.makeText(this@ReIssueTicketActivity, "Network error!!!", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        })

    }

    private fun showMsg(msg: String) {

        val priceChangeFragment = CancellationStatusMessageFragment()
        CancellationStatusMessageFragment.message = msg

        priceChangeFragment.show(supportFragmentManager, "dialog")

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }
}
