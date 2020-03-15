package com.cloudwell.paywell.services.activity.refill.nagad.nagad_v2.webView

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.UtilityBaseActivity
import com.cloudwell.paywell.services.activity.refill.nagad.model.ResTranstionINquiry
import com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log.BalanceClaimModel
import com.cloudwell.paywell.services.activity.refill.nagad.nagad_v2.webView.NagadWebResponse
import com.cloudwell.paywell.services.activity.refill.nagad.nagad_v2.webView.Nagadv2requestPojo
import com.cloudwell.paywell.services.activity.refill.nagad.nagad_v2.webView.WebViewActivity
import com.cloudwell.paywell.services.activity.utility.AllUrl
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import kotlinx.android.synthetic.main.activity_nagad_balance_claim.*
import kotlinx.android.synthetic.main.nagad_balance_claim_response_dialog.view.*
import kotlinx.android.synthetic.main.pallibidyut_billpay_response_dialog.view.billViewLL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NagadBalanceClaimActivityv2 : UtilityBaseActivity() {
    private var mAppHandler: AppHandler? = null
     val INTENT_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nagad_balance_claim2)
        setToolbar(getString(R.string.home_title_nagad_balance_claim))
        mAppHandler = AppHandler.getmInstance(applicationContext)

        submitButton.setOnClickListener {
            checkValidation()
        }

    }

    private fun checkValidation() {
        //val mobileNumber = pbBillNumberET.text.toString()
        val amountString = pbBillAmountET.text.toString()

        if (amountString.length < 1) {
            pbBillAmountET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"))
            return
        } else if (amountString.toDouble() < 1) {
            pbBillAmountET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_limit_error_msg) + "</font>"))
            return
        }

        askForPin(amountString)
    }

    private fun askForPin(amount: String) {
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

                    callBalanceClimApi_v2(pinNoET.text.toString(),  amount)    //for nagad v2 webview

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

    private fun callBalanceClimApi_v2(pin: String, amount: String) {
        showProgressDialog()
        val pojo =  Nagadv2requestPojo()
        pojo.password = pin
        pojo.username = mAppHandler?.userName.toString()
        pojo.amount = amount


        ApiUtils.getAPIServiceV2().gestNahadg(pojo).enqueue(object : Callback<NagadWebResponse>{
            override fun onFailure(call: Call<NagadWebResponse>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

            override fun onResponse(call: Call<NagadWebResponse>, response: Response<NagadWebResponse>) {
                dismissProgressDialog()
                if(response.isSuccessful){

                    if(response.body()?.apiStatus == 200){
                       val details  = response.body()?.responseDetails
                        if (details?.status == 200){

                            val intent = Intent(applicationContext, WebViewActivity::class.java)
                            intent.putExtra("url", details.redirectLink)
                            startActivityForResult(intent, INTENT_REQUEST)

                        }else{
                            showErrorMessagev1(details?.statusName.toString())
                        }

                    }else{
                        showErrorMessagev1(response.body()?.apiStatusName.toString())
                    }


                }else{
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }
            }

        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INTENT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                finish()
            }
        }

    }


    private fun showBillResponseDialog(activity: Activity, palliBidyutBillPayResponse: ResTranstionINquiry) {

        val alertDialog = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.nagad_balance_claim_response_dialog, null)
        val linearLayout = view.billViewLL
        linearLayout.textView.text = palliBidyutBillPayResponse.message


        alertDialog.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()

                if (palliBidyutBillPayResponse.status.toInt() == 200) {
                    finish();
                }
            }


        })
        alertDialog.setView(view)
        alertDialog.setCancelable(false)
        alertDialog.create().show()

    }


}
