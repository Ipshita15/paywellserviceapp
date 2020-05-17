package com.cloudwell.paywell.services.activity.entertainment.bongo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.entertainment.bongo.adapter.BongoTrxLogAdapter
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.BongoEnquiryRqstPojo
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.BongoTrxResponse
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.DataItem
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_bongo_trx.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BongoTrxActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bongo_trx)

        hideToolbar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#b31f2b"))
        };


        mAppHandler = AppHandler.getmInstance(applicationContext)

        if (isInternetConnection){

            getBongoEnquiryData()
        }else{
            showErrorCallBackMessagev1(getString(R.string.internet_connection_error_msg))
        }

        bongo_trx_back.setOnClickListener(View.OnClickListener {
            finish()
        })

    }

    private fun getBongoEnquiryData() {
        showProgressDialog()

        var pojo = BongoEnquiryRqstPojo()
        pojo.limit = "5"
        pojo.referenceIdOrMobile = "01612250477"
        pojo.username = mAppHandler?.userName

        ApiUtils.getAPIServiceV2().getBongoEnquiryData(pojo).enqueue(object : Callback<BongoTrxResponse> {
            override fun onFailure(call: Call<BongoTrxResponse>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<BongoTrxResponse>, response: Response<BongoTrxResponse>) {
                dismissProgressDialog()
                if (response.isSuccessful){
                    val trxtResponse : BongoTrxResponse =response.body()!!
                    if (trxtResponse.status == 200){

                        val data: ArrayList<DataItem?>? = trxtResponse.data
                        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
                        bongo_transaction_log.layoutManager = linearLayoutManager

                        val adapter = BongoTrxLogAdapter(applicationContext, data)
                        bongo_transaction_log.adapter = adapter


                    }else{

                        showErrorCallBackMessagev1(trxtResponse.message)
                    }

                }else{
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg))
                }
            }

        })
    }



}
