package com.cloudwell.paywell.services.activity.healthInsurance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.BongoBannerResponse
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.BongoPkgListReqPojo
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.ImageLinkItem
import com.cloudwell.paywell.services.activity.healthInsurance.adapter.PackegeAdapter
import com.cloudwell.paywell.services.activity.healthInsurance.model.MembershipPackagesItem
import com.cloudwell.paywell.services.activity.healthInsurance.model.RequestMembershipPackages
import com.cloudwell.paywell.services.activity.healthInsurance.model.RespseMemberShipPackage
import com.cloudwell.paywell.services.adapter.MainSliderAdapter
import com.cloudwell.paywell.services.adapter.PicassoImageLoadingWithoutRound
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bongo_subscrib.*
import kotlinx.android.synthetic.main.activity_bongo_subscrib.bongo_packege_recyclerview
import kotlinx.android.synthetic.main.activity_health_package.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.com.bannerslider.Slider


class PackageListActivity : HealthInsuranceBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_package)

        setToolbar(getString(R.string.package_list_digital_health))

        mAppHandler = AppHandler.getmInstance(applicationContext)


        if (isInternetConnection) {

            getHealthPackgeList()

        } else {
            showErrorCallBackMessagev1(getString(R.string.no_internet_connection_please_check_your_internet_connection))
        }


    }






    private fun getHealthPackgeList() {
        showProgressDialog()
        val pojo = RequestMembershipPackages()
        pojo.language = mAppHandler?.appLanguage

        ApiUtils.getAPIServiceV2().getMembershipPackages(pojo).enqueue(object : Callback<RespseMemberShipPackage> {
            override fun onFailure(call: Call<RespseMemberShipPackage>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<RespseMemberShipPackage>, response: Response<RespseMemberShipPackage>) {
                dismissProgressDialog()

                if (response.isSuccessful) {

                    val packagesresponse: RespseMemberShipPackage = response.body()!!
                    if (packagesresponse.statusCode == 200) {

                        setAdapter(packagesresponse)

                    } else {
                        showErrorCallBackMessagev1(packagesresponse.message)
                    }
                } else {
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg))
                }


            }

        })
    }

    private fun setAdapter(packagesresponse: RespseMemberShipPackage) {
        val packages = packagesresponse.membershipPackages
        Log.e("packages", packages?.size.toString())
        var adapter: PackegeAdapter = PackegeAdapter(applicationContext, packages, object : PackegeAdapter.OnclickInterface {
            override fun onclick(m: MembershipPackagesItem) {

                val gson = Gson()
                val packageJson = gson.toJson(m)
                Intent(this@PackageListActivity, PackageDetailsActivity::class.java).also {
                    it.putExtra(getString(R.string.selected_healthmart_package), packageJson)
                    startActivity(it)
                }

            }
        })
        health_packege_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        health_packege_recyclerview.adapter = adapter
    }
}
