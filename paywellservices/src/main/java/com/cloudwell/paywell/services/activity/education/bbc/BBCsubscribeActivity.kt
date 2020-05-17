package com.cloudwell.paywell.services.activity.BBC

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.education.bbc.model.BbcSubscriptionPojo
import com.cloudwell.paywell.services.activity.education.bbc.model.CoursesItem
import com.cloudwell.paywell.services.activity.education.bbc.model.Data
import com.cloudwell.paywell.services.activity.education.PaywellPinDialog
import com.cloudwell.paywell.services.activity.education.Registation_Result
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_b_b_csubscribe.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BBCsubscribeActivity : BaseActivity() {
    private var mAppHandler: AppHandler? = null
   private var mCourse: CoursesItem? = null
    private var pinNumber : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_b_csubscribe)
        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(getString(R.string.bbc_registation_actionbar))
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#5aac40")));
        }
        mAppHandler = AppHandler.getmInstance(applicationContext)

        val course : String = intent.getStringExtra(getString(R.string.selectedCourse))
        mCourse = Gson().fromJson(course, CoursesItem::class.java)

        subscribeBtn.setOnClickListener(View.OnClickListener {
            getUserData()
        })

    }

    private fun getUserData() {
        val username  = userNmae?.text.toString().trim()
        val mobile  = userMobile?.text.toString().trim()

      if (username.isEmpty()){
            showErrorMessagev1("Please give username")
        }else if (mobile.length<11){
            showErrorMessagev1("Please give mobile number correctly")
        }else{
            if (!isInternetConnection) {
                AppHandler.showDialog(supportFragmentManager)
            } else {

                var msg : String = mCourse?.courseName+" "+mCourse?.amount
                askforPin(msg, username, mobile)
            }
        }


    }

    private fun askforPin(message: String, username: String, mobile: String){

        val askingPinDialog = PaywellPinDialog(message, object : PaywellPinDialog.IonClickInterface {
            override fun onclick(pin: String) {

                letsSubscribe(pin, username, mobile)
            }
        })
        askingPinDialog.show(supportFragmentManager, "Pin Dialog")
    }

    private fun letsSubscribe(pin: String, username: String, mobile: String) {
            showProgressDialog()
               val data = Data()
        data.course = mCourse?.courseNo.toString()
        data.mobile = mobile
        data.name = username

        val pojo = BbcSubscriptionPojo()
        pojo.data = data
        pojo.pinNo = pin
        pojo.username = mAppHandler?.userName!!

        ApiUtils.getAPIServiceV2().getBBCsubscribe(pojo).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                dismissProgressDialog()
                if (response.isSuccessful){
                   val js =  JSONObject(response.body()?.string())
                    val status = js.getInt("status")
                    val msg = js.getString("message")
                    if (status == 200){

                        //TODO what to do after success
                        showDialog()

                    }else{

                        showErrorMessagev1(msg)
                    }

                }else{
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg))
                }

            }

        })

    }

    private fun showDialog() {
        var msg = Registation_Result(object : Registation_Result.IonClickInterface {
            override fun onclick() {
                finish()
            }


        })
        msg.show(getSupportFragmentManager(), "oTPVerificationMsgDialog");
    }


}
