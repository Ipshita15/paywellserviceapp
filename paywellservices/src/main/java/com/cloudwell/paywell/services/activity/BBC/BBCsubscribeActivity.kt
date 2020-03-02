package com.cloudwell.paywell.services.activity.BBC

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.BBC.model.BbcSubscriptionPojo
import com.cloudwell.paywell.services.activity.BBC.model.CoursesItem
import com.cloudwell.paywell.services.activity.BBC.model.Data
import com.cloudwell.paywell.services.activity.BBC.model.RegistationInfo
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_b_b_csubscribe.*
import kotlinx.android.synthetic.main.activity_missing_main.view.*
import kotlinx.android.synthetic.main.activity_topup_main.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BBCsubscribeActivity : BaseActivity() {
    private var mAppHandler: AppHandler? = null
   private var mCourse: CoursesItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_b_csubscribe)
        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle("BBC Course List")
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
        val pinnumber  = pin?.text.toString().trim()
        val username  = userNmae?.text.toString().trim()
        val mobile  = userMobile?.text.toString().trim()

        if(pinnumber.isEmpty()){
            showErrorMessagev1("Please give Pin")
        }else if (username.isEmpty()){
            showErrorMessagev1("Please give username")
        }else if (mobile.length<11){
            showErrorMessagev1("Please give mobile number correctly")
        }else{
            if (!isInternetConnection) {
                AppHandler.showDialog(supportFragmentManager)
            } else {
                letsSubscribe(pinnumber, username, mobile)
            }
        }


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



                    }else{
                        showErrorMessagev1(msg)
                    }

                }else{
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }

            }

        })

    }


}
