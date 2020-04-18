package com.cloudwell.paywell.services.activity.education.Bongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.education.Bongo.model.*
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BongoMainActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bongo_main)


        mAppHandler = AppHandler.getmInstance(applicationContext)

        getBongoPackgeList()

        getBongoActivePackgeList()

        getBongoSubscriptionCount()

        getBongoEnquiryData()

    }

    private fun getBongoPackgeList() {
        showProgressDialog()

        val pojo = BongoPkgListReqPojo()
        pojo.language ="en"

        ApiUtils.getAPIServiceV2().getBongoPackgeList(pojo).enqueue(object : Callback<BongoResponsePojo> {
            override fun onFailure(call: Call<BongoResponsePojo>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<BongoResponsePojo>, response: Response<BongoResponsePojo>) {
                dismissProgressDialog()
            }

        })
    }

    private fun getBongoActivePackgeList() {
        showProgressDialog()

        var bongoActivePkgPojo = BongoActivePkgPojo()
        bongoActivePkgPojo.code = "17"
        bongoActivePkgPojo.locale = "en"
        bongoActivePkgPojo.mobile = "01612250477"
        bongoActivePkgPojo.pin_no = "12345"
        bongoActivePkgPojo.username = mAppHandler?.userName


        ApiUtils.getAPIServiceV2().getBongoActivePackgeList(bongoActivePkgPojo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                dismissProgressDialog()
            }

        })
    }

    private fun getBongoSubscriptionCount() {
        showProgressDialog()

        var pojo = BongoSubscriptionPojo()
        pojo.pin_no = "12345"
        pojo.username = mAppHandler?.userName

        ApiUtils.getAPIServiceV2().getBongoSubscriptionCount(pojo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                dismissProgressDialog()
            }

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


