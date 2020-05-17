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
import com.cloudwell.paywell.services.constant.IconConstant
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.StringConstant
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

        bbcBtnTranscation.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, BongoTrxActivity::class.java))
        })


        addRecentUsedList()

    }



    private fun addRecentUsedList() {
        val recentUsedMenu = RecentUsedMenu(StringConstant.KEY_bongo, StringConstant.KEY_home_entertainment, IconConstant.KEY_bongo_icon, 0, 52)
        addItemToRecentListInDB(recentUsedMenu)
    }




}


