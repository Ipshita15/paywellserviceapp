package com.cloudwell.paywell.services.activity.product.ekShop.report.ui.report

import android.arch.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.product.ekShop.model.ResEKReport
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-25.
 */
class AKShopRepo(val aPIService: APIService) {

    fun getReportList(startDate: String, endDate: String, order_code: String): MutableLiveData<ResEKReport> {
        val data = MutableLiveData<ResEKReport>()

        val mAppHandler = AppHandler.getmInstance(AppController.getContext())
        val rid = "cwntcl"
        val url = AppController.getContext().getString(R.string.ek_report)


        aPIService.getReport(url, rid, startDate, endDate, order_code).enqueue(object : Callback<ResEKReport> {
            override fun onFailure(call: Call<ResEKReport>, t: Throwable) {
                data.value = null

            }

            override fun onResponse(call: Call<ResEKReport>, response: Response<ResEKReport>) {
                data.value = response.body()

            }

        })
        return data;
    }


}