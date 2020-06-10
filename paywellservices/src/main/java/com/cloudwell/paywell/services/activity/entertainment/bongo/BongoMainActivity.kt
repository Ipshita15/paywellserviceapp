package com.cloudwell.paywell.services.activity.entertainment.bongo

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.BongoTRXrequestModel
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.BongoTRXresponse
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.constant.IconConstant
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.StringConstant
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bongo_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BongoMainActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener {

    private var mAppHandler: AppHandler? = null
    private var selectedLimit = ""
    private var radioButton_five: RadioButton? = null
    private var radioButton_ten: RadioButton? = null
    private var radioButton_twenty: RadioButton? = null
    private var radioButton_fifty: RadioButton? = null
    private var radioButton_hundred: RadioButton? = null
    private var radioButton_twoHundred: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bongo_main)

        hideToolbar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#b31f2b"))
        };

        mAppHandler = AppHandler.getmInstance(applicationContext)

        bongo_main_back.setOnClickListener(View.OnClickListener {
            finish()
        })

        bongoBtnpackage.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, BongoSubscribActivity::class.java))
        })

        bongo_count.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, BongoCounterActivity::class.java))
        })

        bbcBtnTranscation.setOnClickListener(View.OnClickListener {
           // startActivity(Intent(this, BongoTrxActivity::class.java))
            showLimitPrompt()
        })


        addRecentUsedList()

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

        val transcationLog = BongoTRXrequestModel()
        transcationLog.limit = ""+limit
        transcationLog.service = "Bongo"
        transcationLog.userName = mAppHandler?.userName.toString()

        ApiUtils.getAPIServiceV2().getBongoTransactionLog(transcationLog).enqueue(object : Callback<BongoTRXresponse> {
            override fun onFailure(call: Call<BongoTRXresponse>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

            override fun onResponse(call: Call<BongoTRXresponse>, response: Response<BongoTRXresponse>) {
                dismissProgressDialog()
                if (response.isSuccessful){
                    val trPojo : BongoTRXresponse = response.body()!!
                    val status :Int? =  trPojo.status //js.getInt("status")
                    val msg : String =   trPojo.message.toString() //js.getString("message")
                    if (status == 200){
                        val toJson = Gson().toJson(trPojo)
                        val intent = Intent(applicationContext, BongoTrxActivity::class.java)
                        intent.putExtra(getString(R.string.bongo_trx_tag), toJson)
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


