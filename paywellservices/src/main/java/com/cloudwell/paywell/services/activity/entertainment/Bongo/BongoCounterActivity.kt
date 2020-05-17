package com.cloudwell.paywell.services.activity.entertainment.Bongo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.entertainment.Bongo.model.BongoSubscriptionPojo
import com.cloudwell.paywell.services.activity.entertainment.Bongo.model.CountResponse
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_bongo_counter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BongoCounterActivity : BaseActivity() {
    private var mAppHandler: AppHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bongo_counter)

        hideToolbar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#b31f2b"))
        };

        mAppHandler = AppHandler.getmInstance(applicationContext)

        bongo_count_back.setOnClickListener(View.OnClickListener {
            finish()
        })

        if (isInternetConnection){
            getBongoSubscriptionCount()
        }else{
            showErrorCallBackMessagev1(getString(R.string.no_internet_connection_please_check_your_internet_connection))
        }

    }

    private fun getBongoSubscriptionCount() {
        showProgressDialog()

        var pojo = BongoSubscriptionPojo()
        pojo.pin_no = "12345"
        pojo.username = mAppHandler?.userName

        ApiUtils.getAPIServiceV2().getBongoSubscriptionCount(pojo).enqueue(object : Callback<CountResponse> {
            override fun onFailure(call: Call<CountResponse>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<CountResponse>, response: Response<CountResponse>) {
                dismissProgressDialog()
                if(response.isSuccessful){
                   val countresponse : CountResponse = response.body()!!
                    if(countresponse.status == 200){
                        counter_txt.text = getString(R.string.bongo_counter)+" - "+ countresponse.count

                    }else{
                        showErrorCallBackMessagev1(countresponse.message)
                    }
                }else{
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg))
                }

            }

        })
    }
}
