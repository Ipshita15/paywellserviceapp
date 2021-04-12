package com.cloudwell.paywell.services.activity.healthInsurance

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
import kotlinx.android.synthetic.main.activity_bongo_subscrib.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.com.bannerslider.Slider


class PackageListActivity : HealthInsuranceBaseActivity() {
    private var viewPager: Slider? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_package)

        setToolbar(getString(R.string.package_list_digital_health))

        viewPager = findViewById(R.id.bongoo_sllider)


        mAppHandler = AppHandler.getmInstance(applicationContext)



        if (isInternetConnection) {
            getSliderImage()

            getBongoPackgeList()

        } else {
            showErrorCallBackMessagev1(getString(R.string.no_internet_connection_please_check_your_internet_connection))
        }


    }

    private fun getSliderImage() {

        val pojo = BongoPkgListReqPojo()
        pojo.language = mAppHandler?.appLanguage
        ApiUtils.getAPIServiceV2().getBannerList(pojo).enqueue(object : Callback<BongoBannerResponse> {
            override fun onFailure(call: Call<BongoBannerResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BongoBannerResponse>, response: Response<BongoBannerResponse>) {

                if (response.isSuccessful) {
                    val banner: BongoBannerResponse = response.body()!!
                    if (banner.status == 200) {
                        val imageLink: ArrayList<ImageLinkItem?>? = banner.imageLink
                        val stringArray = arrayOfNulls<String>(imageLink!!.size)
                        for (i in 0 until imageLink!!.size) {
                            stringArray[i] = imageLink.get(i)?.imageAddress
                            Log.e("url", imageLink.get(i)?.imageAddress.toString())
                        }
                        Log.e("url", stringArray.size.toString())
                        initializePreview(stringArray)
                    }

                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        viewPager!!.setInterval(2000)
    }

    override fun onPause() {
        super.onPause()
        viewPager!!.setInterval(0)
    }

    override fun onDestroy() {
        if (viewPager != null) {
            Slider.imageLoadingService = null
            viewPager = null
        }
        super.onDestroy()
    }


    private fun initializePreview(array: Array<String?>) {

        val imageUpdateVersionString = array.size.toString() + array
        Slider.init(PicassoImageLoadingWithoutRound(imageUpdateVersionString))
        viewPager!!.setAdapter(MainSliderAdapter(applicationContext, array))
        viewPager!!.setInterval(2000)
        viewPager!!.setLoopSlides(true)
    }


    private fun getBongoPackgeList() {
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


            }
        })
        bongo_packege_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        bongo_packege_recyclerview.adapter = adapter
    }
}
