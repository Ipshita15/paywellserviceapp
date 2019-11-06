package com.cloudwell.paywell.services.activity.refill.nagad


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity

import kotlinx.android.synthetic.main.activity_nagad_main.*

import com.google.android.material.snackbar.Snackbar

import androidx.appcompat.app.AppCompatDialog
import com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log.RefillLog
import com.cloudwell.paywell.services.activity.utility.AllUrl
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.google.gson.Gson
import kotlinx.android.synthetic.main.dialog_trx_limit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NagadMainActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        if (isChecked) {
            if (buttonView?.getId() == R.id.radio_five) {
                selectedLimit = "5"
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.getId() == R.id.radio_ten) {
                selectedLimit = "10"
                radioButton_five?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.getId() == R.id.radio_twenty) {
                selectedLimit = "20"
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.getId() == R.id.radio_fifty) {
                selectedLimit = "50"
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.getId() == R.id.radio_hundred) {
                selectedLimit = "100"
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.getId() == R.id.radio_twoHundred) {
                selectedLimit = "200"
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
            }
        }
    }

    private var selectedLimit = ""

    private lateinit var radioButton_five: RadioButton
    private lateinit var radioButton_ten:RadioButton
    private lateinit var radioButton_twenty:RadioButton
    private lateinit var radioButton_fifty:RadioButton
    private lateinit var radioButton_hundred:RadioButton
    private lateinit var radioButton_twoHundred:RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nagad_main)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.home_nagad_balance_title)
        }


        nagadBalanceClaim.setOnClickListener(this)
        nagadBalanceRefill.setOnClickListener(this)
        nagadRefillLog.setOnClickListener(this)




    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nagadBalanceClaim ->{
                val intent = Intent(applicationContext, NagadBalanceClaimActivity::class.java)
                startActivity(intent)
            }
            R.id.nagadBalanceRefill ->{
                val intent = Intent(this, BalanceRefillActivity::class.java)
                startActivity(intent)
            }
            R.id.nagadRefillLog->{

                showLimitPrompt()

            }



        }


    }
    private fun showLimitPrompt() {
// custom dialog
        val dialog = AppCompatDialog(this)
        dialog.setTitle(R.string.log_limit_title_msg)
        dialog.setContentView(R.layout.dialog_trx_limit)

        val btn_okay = dialog.buttonOk
        val btn_cancel = dialog.cancelBtn

        radioButton_five =   dialog.radio_five
        radioButton_ten =   dialog.radio_ten
        radioButton_twenty= dialog.radio_twenty
        radioButton_fifty = dialog.radio_fifty
        radioButton_hundred = dialog.radio_hundred
        radioButton_twoHundred = dialog.radio_twoHundred

        radioButton_five.setOnCheckedChangeListener(this)
        radioButton_ten.setOnCheckedChangeListener(this)
        radioButton_twenty.setOnCheckedChangeListener(this)
        radioButton_fifty.setOnCheckedChangeListener(this)
        radioButton_hundred.setOnCheckedChangeListener(this)
        radioButton_twoHundred.setOnCheckedChangeListener(this)

        assert(btn_okay != null)
        btn_okay!!.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            if (selectedLimit.isEmpty()) {
                selectedLimit = "5"
            }

            if (ConnectionDetector(applicationContext).isConnectingToInternet) {

                askForPin(selectedLimit);

            } else {
                val snackbar = Snackbar.make(nagadConstrainLayout, getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.getView()
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        })
        assert(btn_cancel != null)
        btn_cancel!!.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun askForPin(selectedLimit: String) {

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

                if (ConnectionDetector(applicationContext).isConnectingToInternet) {
                    callAPI(pinNoET.text.toString(), selectedLimit)
                } else {
                    showNoInternetConnectionFound()
                }
            } else {
                showServerErrorMessage(getString(R.string.pin_no_error_msg))
            }
        }
        val alert = builder.create()
        alert.show()



    }

    private fun callAPI(pin: String, selectedLimit: String) {

        val hostUrlBkapi = AllUrl.HOST_URL_lastSuccessfulTrx
        val sec_token = AllUrl.sec_token
//        val imeiNo = AppHandler.getmInstance(applicationContext).imeiNo
        val imeiNo = "867305035545487"
        val format = "json"
        val gateway_id = "5"
        val limit = selectedLimit

        showProgressDialog()

        ApiUtils.getAPIService().refillLogInquiry(hostUrlBkapi, sec_token, imeiNo.toString(), pin, format,gateway_id,limit).enqueue(object : Callback<RefillLog> {
            override fun onFailure(call: Call<RefillLog>, t: Throwable) {
                Toast.makeText(applicationContext, "Server error!!!", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<RefillLog>, response: Response<RefillLog>) {
                dismissProgressDialog()

                val body = response.body()

                if(body?.status == 407){
                    showSnackMessageWithTextMessage(""+body.message)
                }else if(body?.status == 200){

                    val toJson = Gson().toJson(body)
                    val intent = Intent(applicationContext, RefillLogInquiryActivity::class.java)
                    intent.putExtra("data", toJson)
                    startActivity(intent)

                }



            }
        })


    }
}




