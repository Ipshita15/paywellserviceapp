package com.cloudwell.paywell.services.activity.entertainment.Bongo

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.entertainment.Bongo.model.BongoEnquiryRqstPojo
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_bongo_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BongoMainActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null

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


    }

    private fun getBongoEnquiryData() {
        showProgressDialog()

        var pojo = BongoEnquiryRqstPojo()
        pojo.limit = "5"
        pojo.referenceIdOrMobile = "01612250477"
        pojo.username = mAppHandler?.userName

        ApiUtils.getAPIServiceV2().getBongoEnquiryData(pojo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                dismissProgressDialog()
            }

        })
    }



}


