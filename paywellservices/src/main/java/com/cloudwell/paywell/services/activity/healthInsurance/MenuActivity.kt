package com.cloudwell.paywell.services.activity.healthInsurance

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import com.cloudwell.paywell.services.activity.healthInsurance.model.HealthInqueryRequest
import com.cloudwell.paywell.services.activity.healthInsurance.model.ClaimResponse
import com.cloudwell.paywell.services.activity.healthInsurance.model.TrxRequest
import com.cloudwell.paywell.services.activity.healthInsurance.model.TrxResponse
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.constant.IconConstant
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.StringConstant
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_health_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : HealthInsuranceBaseActivity(), CompoundButton.OnCheckedChangeListener {

    private var selectedLimit = ""
    private var radioButton_five: RadioButton? = null
    private var radioButton_ten: RadioButton? = null
    private var radioButton_twenty: RadioButton? = null
    private var radioButton_fifty: RadioButton? = null
    private var radioButton_hundred: RadioButton? = null
    private var radioButton_twoHundred: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_menu)

        setToolbar(getString(R.string.didital_hospital))


//        bongo_main_back.setOnClickListener(View.OnClickListener {
//            finish()
//        })
//
        Insurancepackage.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, PackageListActivity::class.java))
        })
        //
        health_Transcation.setOnClickListener(View.OnClickListener {
            showLimitPrompt()
           // startActivity(Intent(this, TranscationLogActivity::class.java))
        })
        health_claim.setOnClickListener(View.OnClickListener {
            showEnquiryPrompt()
           // startActivity(Intent(this, TranscationLogActivity::class.java))
        })



//        bongo_count.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(this, BongoCounterActivity::class.java))
//        })
//
//        bbcBtnTranscation.setOnClickListener(View.OnClickListener {
//           // startActivity(Intent(this, BongoTrxActivity::class.java))
//            showLimitPrompt()
//        })
//
//
//        addRecentUsedList()

    }


    private fun showEnquiryPrompt() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.phone_no_title_msg)
        val enqNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        enqNoET.layoutParams = lp
        enqNoET.inputType = InputType.TYPE_CLASS_NUMBER
        enqNoET.gravity = Gravity.CENTER_HORIZONTAL
        val FilterArray = arrayOfNulls<InputFilter>(1)
        FilterArray[0] = InputFilter.LengthFilter(11)
        enqNoET.filters = FilterArray
        builder.setView(enqNoET)
        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            if (enqNoET.text.toString().length < 11) {
                showErrorMessagev1(getString(R.string.phone_no_error_msg))
                showEnquiryPrompt()
            } else {
                if (!isInternetConnection) {
                    AppHandler.showDialog(supportFragmentManager)
                } else {
                    getHealthClaim(enqNoET.text.toString())
                }
                dialogInterface.dismiss()
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private fun getHealthClaim(phone: String) {
        showProgressDialog()

        val claim =  HealthInqueryRequest()
        claim.username = mAppHandler.userName
        claim.phone = phone

        ApiUtils.getAPIServiceV2().healthInquiry(claim).enqueue(object : Callback<ClaimResponse>{
            override fun onResponse(call: Call<ClaimResponse>, response: Response<ClaimResponse>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    val trxPjo : ClaimResponse = response.body()!!

                    val status: Int? = trxPjo.statusCode
                    val msg: String = trxPjo.message.toString()
                    if (status == 200) {

                        val toJson = Gson().toJson(trxPjo)
                        val intent = Intent(applicationContext, InqueryActivity::class.java)
                        intent.putExtra(getString(R.string.health_claim_tag), toJson)
                        startActivity(intent)


                    } else {
                        showErrorCallBackMessagev1(msg)
                    }


                } else {
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg))
                }



            }

            override fun onFailure(call: Call<ClaimResponse>, t: Throwable) {
                dismissProgressDialog()
            }
        })


    }

    private fun addRecentUsedList() {
        val recentUsedMenu = RecentUsedMenu(StringConstant.KEY_bongo, StringConstant.KEY_home_entertainment, IconConstant.KEY_bongo_icon, 0, 52)
        addItemToRecentListInDB(recentUsedMenu)
    }


    // Transaction LOG
    private fun showLimitPrompt() {
        // custom dialog
        val dialog = AppCompatDialog(this)
        dialog.setTitle(R.string.log_limit_title_msg)
        dialog.setContentView(R.layout.dialog_trx_limit)
        val btn_okay = dialog.findViewById<Button>(R.id.buttonOk)
        val btn_cancel = dialog.findViewById<Button>(R.id.cancelBtn)
        radioButton_five = dialog.findViewById(R.id.radio_five)
        radioButton_ten = dialog.findViewById(R.id.radio_ten)
        radioButton_twenty = dialog.findViewById(R.id.radio_twenty)
        radioButton_fifty = dialog.findViewById(R.id.radio_fifty)
        radioButton_hundred = dialog.findViewById(R.id.radio_hundred)
        radioButton_twoHundred = dialog.findViewById(R.id.radio_twoHundred)
        radioButton_five!!.setOnCheckedChangeListener(this)
        radioButton_ten!!.setOnCheckedChangeListener(this)
        radioButton_twenty!!.setOnCheckedChangeListener(this)
        radioButton_fifty!!.setOnCheckedChangeListener(this)
        radioButton_hundred!!.setOnCheckedChangeListener(this)
        radioButton_twoHundred!!.setOnCheckedChangeListener(this)
        assert(btn_okay != null)
        btn_okay!!.setOnClickListener {
            dialog.dismiss()
            if (selectedLimit.isEmpty()) {
                selectedLimit = "5"
            }
            val limit = selectedLimit.toInt()
            if (!isInternetConnection) {
                AppHandler.showDialog(supportFragmentManager)
            } else {
                getTransactionLog(limit)
            }
        }
        assert(btn_cancel != null)
        btn_cancel!!.setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun getTransactionLog(limit: Int) {
        showProgressDialog()

        val transcationLog = TrxRequest()
        transcationLog.limit = "" + limit
        transcationLog.username = mAppHandler.userName

        ApiUtils.getAPIServiceV2().trnscationLog(transcationLog).enqueue(object : Callback<TrxResponse> {
            override fun onFailure(call: Call<TrxResponse>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

            override fun onResponse(call: Call<TrxResponse>, response: Response<TrxResponse>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    val trxPjo : TrxResponse = response.body()!!

                    val status: Int? = trxPjo.statusCode
                    val msg: String = trxPjo.message.toString()
                    if (status == 200) {

                        val toJson = Gson().toJson(trxPjo)
                        val intent = Intent(applicationContext, TranscationLogActivity::class.java)
                        intent.putExtra(getString(R.string.health_trx_tag), toJson)
                        startActivity(intent)


                    } else {
                        showErrorCallBackMessagev1(msg)
                    }


                } else {
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg))
                }
            }


        })

    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            if (buttonView?.id == R.id.radio_five) {
                selectedLimit = "5"

                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.id == R.id.radio_ten) {
                selectedLimit = "10"
                radioButton_five?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.id == R.id.radio_twenty) {
                selectedLimit = "20"
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.id == R.id.radio_fifty) {
                selectedLimit = "50"
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.id == R.id.radio_hundred) {
                selectedLimit = "100"
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView?.id == R.id.radio_twoHundred) {
                selectedLimit = "200"
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
            }
        }
    }


}


