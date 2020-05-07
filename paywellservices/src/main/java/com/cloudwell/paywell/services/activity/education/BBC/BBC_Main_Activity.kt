package com.cloudwell.paywell.services.activity.education.BBC

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.R.layout.dialog_trx_limit
import com.cloudwell.paywell.services.R.string.log_limit_title_msg
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.education.BBC.model.*
import com.cloudwell.paywell.services.activity.refill.nagad.NagadRefillLogInquiryActivity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bbc_main.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BBC_Main_Activity : BaseActivity(), View.OnClickListener , CompoundButton.OnCheckedChangeListener {

    private var mAppHandler: AppHandler? = null
    //private var radioButton_five: RadioButton? = null, private  var radioButton_ten:RadioButton? = null, private  var radioButton_twenty:RadioButton? = null, private  var radioButton_fifty:RadioButton? = null, private  var radioButton_hundred:RadioButton? = null, private  var radioButton_twoHundred:RadioButton? = null
    private var selectedLimit = ""
    private var radioButton_five: RadioButton? = null
    private var radioButton_ten: RadioButton? = null
    private var radioButton_twenty: RadioButton? = null
    private var radioButton_fifty: RadioButton? = null
    private var radioButton_hundred: RadioButton? = null
    private var radioButton_twoHundred: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bbc_main)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(getString(R.string.subscription))
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#5aac40")));
        }

        mAppHandler = AppHandler.getmInstance(applicationContext)
        initilize()

        bbcBtnCourse.setOnClickListener(this)
        bbcBtnstatus.setOnClickListener(this)
        bbcBtnTranscation.setOnClickListener(this)

    }

    private fun initilize() {
        if (mAppHandler!!.appLanguage.equals("en", ignoreCase = true)) {
            bbcBtnCourse.setTypeface(AppController.getInstance().oxygenLightFont)
            bbcBtnstatus.setTypeface(AppController.getInstance().oxygenLightFont)
            bbcBtnTranscation.setTypeface(AppController.getInstance().oxygenLightFont)

        } else {
            bbcBtnCourse.setTypeface(AppController.getInstance().aponaLohitFont)
            bbcBtnstatus.setTypeface(AppController.getInstance().aponaLohitFont)
            bbcBtnTranscation.setTypeface(AppController.getInstance().aponaLohitFont)

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bbcBtnCourse -> {
                startActivity(Intent(applicationContext, CourseListActivity::class.java))
            }
            R.id.bbcBtnstatus -> {
                showEnquiryPrompt()

            }
            R.id.bbcBtnTranscation -> {
                showLimitPrompt()

            }
        }
    }



    fun Context.toast(context: Context = applicationContext, message: String, toastDuration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, toastDuration).show()
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
        FilterArray[0] = LengthFilter(11)
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
                    getRegistationStatus(enqNoET.text.toString())
                }
                dialogInterface.dismiss()
            }
        }
        val alert = builder.create()
        alert.show()
    }


    private fun getRegistationStatus(mobile : String) {
        showProgressDialog()
        //toast(message = "hello asce", toastDuration = Toast.LENGTH_LONG)
        val registation = RegistationInfo()
        registation.mobile = mobile
        registation.username = mAppHandler?.userName.toString()

        ApiUtils.getAPIServiceV2().getBBCregistationInfo(registation).enqueue(object : Callback<StatusCheckResponsePojo>{
            override fun onFailure(call: Call<StatusCheckResponsePojo>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))

            }

            override fun onResponse(call: Call<StatusCheckResponsePojo>, response: Response<StatusCheckResponsePojo>) {
                    dismissProgressDialog()
                if (response.isSuccessful){
                    val status : StatusCheckResponsePojo = response.body()!!
                    if (status.status == 200){

                        val toJson = Gson().toJson(status)
                        val intent = Intent(applicationContext, BBCstatusCheckActivity::class.java)
                        intent.putExtra("data", toJson)
                        startActivity(intent)


                    }else{
                        showErrorMessagev1(status.message)
                    }

                   }else{
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }

            }

        })
    }


    // Transaction LOG
    private fun showLimitPrompt() {
        // custom dialog
        val dialog = AppCompatDialog(this)
        dialog.setTitle(log_limit_title_msg)
        dialog.setContentView(dialog_trx_limit)
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
        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this).rid)

        val transcationLog = BbcTranscationLog()
        transcationLog.limit = ""+limit
        transcationLog.service = "BBC"
        transcationLog.userName = mAppHandler?.userName.toString()

        ApiUtils.getAPIServiceV2().getBBCTransactionLog(transcationLog).enqueue(object : Callback<TransactionResponsePOjo>{
            override fun onFailure(call: Call<TransactionResponsePOjo>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

            override fun onResponse(call: Call<TransactionResponsePOjo>, response: Response<TransactionResponsePOjo>) {
                dismissProgressDialog()
               if (response.isSuccessful){
                   val trPojo : TransactionResponsePOjo = response.body()!!
                   val status :Int? =  trPojo.status //js.getInt("status")
                   val msg : String =   trPojo.message.toString() //js.getString("message")
                   if (status == 200){

                       val toJson = Gson().toJson(trPojo)
                       val intent = Intent(applicationContext, TranscationListActivity::class.java)
                       intent.putExtra("data", toJson)
                       startActivity(intent)

                   }else{
                       showErrorMessagev1(msg)
                   }


               }else{
                   showErrorMessagev1(getString(R.string.try_again_msg))
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



