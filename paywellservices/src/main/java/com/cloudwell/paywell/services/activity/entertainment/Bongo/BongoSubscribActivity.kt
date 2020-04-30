package com.cloudwell.paywell.services.activity.entertainment.Bongo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.entertainment.Bongo.adapter.BongoPackegeAdapter
import com.cloudwell.paywell.services.activity.entertainment.Bongo.model.*
import com.cloudwell.paywell.services.adapter.MainSliderAdapter
import com.cloudwell.paywell.services.adapter.PicassoImageLoadingWithoutRound
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_bongo_subscrib.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.com.bannerslider.Slider


class BongoSubscribActivity : BaseActivity() {
    private var mAppHandler: AppHandler? = null
    private var viewPager: Slider? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bongo_subscrib)

        viewPager = findViewById(R.id.bongoo_sllider)

        hideToolbar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#b31f2b"))
        };
        mAppHandler = AppHandler.getmInstance(applicationContext)

        bongo_pk_back.setOnClickListener(View.OnClickListener {
            finish()
        })

        if(isInternetConnection){
            getSliderImage()

            getBongoPackgeList()

        }else{
            showErrorCallBackMessagev1(getString(R.string.no_internet_connection_please_check_your_internet_connection))
        }


    }

    private fun getSliderImage() {

        val pojo = BongoPkgListReqPojo()
        pojo.language = mAppHandler?.appLanguage
        ApiUtils.getAPIServiceV2().getBannerList(pojo).enqueue(object : Callback<BongoBannerResponse>{
            override fun onFailure(call: Call<BongoBannerResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BongoBannerResponse>, response: Response<BongoBannerResponse>) {

                if (response.isSuccessful){
                    val banner : BongoBannerResponse = response.body()!!
                    if (banner.status == 200){
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
        val pojo = BongoPkgListReqPojo()
        pojo.language = mAppHandler?.appLanguage

        ApiUtils.getAPIServiceV2().getBongoPackgeList(pojo).enqueue(object : Callback<BongoResponsePojo> {
            override fun onFailure(call: Call<BongoResponsePojo>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<BongoResponsePojo>, response: Response<BongoResponsePojo>) {
                dismissProgressDialog()

                if(response.isSuccessful){

                    val packagesresponse : BongoResponsePojo = response.body()!!
                    if (packagesresponse.status == 200){

                        val packages:  ArrayList<PackagesItem?>? = packagesresponse.packages
                        Log.e("packages", packages?.size.toString())
                        var adapter : BongoPackegeAdapter = BongoPackegeAdapter(this@BongoSubscribActivity, packages)
                        bongo_packege_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                        bongo_packege_recyclerview.adapter = adapter

                    }else{
                        showErrorCallBackMessagev1(packagesresponse.message)
                    }
                }else{
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg))
                }


            }

        })
    }
}
