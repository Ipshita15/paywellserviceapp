package com.cloudwell.paywell.services.activity.refill.nagad

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.refill.nagad.fragment.MobileNumberQRCodeFragment
import com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log.RefillLog
import com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log.RefillLogRequestModel
import com.cloudwell.paywell.services.activity.refill.nagad.nagad_v2.webView.NagadBalanceClaimActivityv2
import com.cloudwell.paywell.services.activity.utility.AllUrl
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_nagad_main.*
import kotlinx.android.synthetic.main.dialog_trx_limit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NagadMainActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var selectedLimit = ""

    private lateinit var radioButton_five: RadioButton
    private lateinit var radioButton_ten: RadioButton
    private lateinit var radioButton_twenty: RadioButton
    private lateinit var radioButton_fifty: RadioButton
    private lateinit var radioButton_hundred: RadioButton
    private lateinit var radioButton_twoHundred: RadioButton

    private var mAppHandler: AppHandler? = null
    private var mCoordinateLayout: CoordinatorLayout? = null

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




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nagad_main)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.home_nagad_balance_title)
        }

        initView()
        nagadBalanceClaimv2.setOnClickListener(this)
        nagadBalanceClaim.setOnClickListener(this)
        nagadBalanceRefill.setOnClickListener(this)
        nagadQRCode.setOnClickListener(this)
        nagadRefillLog.setOnClickListener(this)

    }

    // for language font :

    private fun initView() {
        mAppHandler = AppHandler.getmInstance(applicationContext)
        mCoordinateLayout = findViewById<CoordinatorLayout>(R.id.coordinateLayout)
        ConnectionDetector(AppController.getContext())


        if (mAppHandler?.getAppLanguage().equals("en", ignoreCase = true)) {
            nagadBalanceClaim.typeface = AppController.getInstance().oxygenLightFont

            nagadBalanceRefill.typeface = AppController.getInstance().oxygenLightFont
            nagadRefillLog.typeface = AppController.getInstance().oxygenLightFont
            nagadQRCode.typeface = AppController.getInstance().oxygenLightFont
        } else {
            nagadBalanceClaim.typeface = AppController.getInstance().aponaLohitFont

            nagadBalanceRefill.typeface = AppController.getInstance().aponaLohitFont
            nagadRefillLog.typeface = AppController.getInstance().aponaLohitFont
            nagadQRCode.typeface = AppController.getInstance().aponaLohitFont
        }
    }

    // button onclick :

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nagadBalanceClaimv2 -> {
                val intent = Intent(applicationContext, NagadBalanceClaimActivityv2::class.java)
                startActivity(intent)
            }
            R.id.nagadBalanceClaim -> {

                val intent = Intent(applicationContext, NagadBalanceClaimActivity::class.java)
                startActivity(intent)
            }
            R.id.nagadBalanceRefill -> {
                val intent = Intent(this, BalanceRefillActivity::class.java)
                startActivity(intent)
            }
            R.id.nagadQRCode -> {

                val priceChangeFragment = MobileNumberQRCodeFragment()
                priceChangeFragment.show(supportFragmentManager, "dialog")
            }
            R.id.nagadRefillLog -> {
                showLimitPrompt()
            }
        }
    }

    private fun showLimitPrompt() {
// custom dialog
        val dialog = AppCompatDialog(this)
        dialog.setTitle(R.string.log_refill_limit_title_msg)
        dialog.setContentView(R.layout.dialog_trx_limit)

        val btn_okay = dialog.buttonOk
        val btn_cancel = dialog.cancelBtn

        radioButton_five = dialog.radio_five
        radioButton_ten = dialog.radio_ten
        radioButton_twenty = dialog.radio_twenty
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

                getTranscationLog(selectedLimit)

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

    // ask for pin :

//    private fun askForPin(selectedLimit: String) {
//
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle(R.string.pin_no_title_msg)
//
//        val pinNoET = EditText(this)
//        val lp = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT)
//        pinNoET.gravity = Gravity.CENTER_HORIZONTAL
//        pinNoET.layoutParams = lp
//        pinNoET.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_PASSWORD
//        pinNoET.transformationMethod = PasswordTransformationMethod.getInstance()
//        builder.setView(pinNoET)
//
//        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
//            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)
//
//            if (pinNoET.text.toString().length != 0) {
//                dialogInterface.dismiss()
//
//                if (ConnectionDetector(applicationContext).isConnectingToInternet) {
//                    callAPI(pinNoET.text.toString(), selectedLimit)
//                } else {
//                    showNoInternetConnectionFound()
//                }
//            } else {
//                showServerErrorMessage(getString(R.string.pin_no_error_msg))
//
//
//            }
//        }
//        val alert = builder.create()
//        alert.show()
//
//    }


    // calling API :

    private fun getTranscationLog(selectedLimit: String){
        showProgressDialog()
        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext).rid)
        val sec_token = AllUrl.sec_token
        val imeiNo = AppHandler.getmInstance(applicationContext).userName
        val gateway_id = "5"
        val limit = selectedLimit

        val model =  RefillLogRequestModel()
        model.imei = imeiNo
        model.sec_token = sec_token
        model.gateway_id = gateway_id
        model.limit = limit
        model.ref_id = uniqueKey

        ApiUtils.getAPIServiceV2().refillLogInquiry(model).enqueue(object : Callback<RefillLog> {
            override fun onFailure(call: Call<RefillLog>, t: Throwable) {
                Toast.makeText(applicationContext, "Server error!!!", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<RefillLog>, response: Response<RefillLog>) {
                dismissProgressDialog()

                val body = response.body()

                if (body?.status == 407) {
                    showSnackMessageWithTextMessage("" + body.message)
                } else if (body?.status == 200) {

                    // convert json object to gson string and send next activity:

                    val toJson = Gson().toJson(body)
                    val intent = Intent(applicationContext, NagadRefillLogInquiryActivity::class.java)
                    intent.putExtra("data", toJson)
                    startActivity(intent)
                } else {

                    showDialogMessage("" + body?.message)
                }
            }
        })
    }

}





