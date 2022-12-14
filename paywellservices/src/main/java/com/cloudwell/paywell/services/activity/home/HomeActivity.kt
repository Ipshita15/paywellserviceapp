package com.cloudwell.paywell.services.activity.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.AppLoadingActivity
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.faq.PreLoginFAQActivity
import com.cloudwell.paywell.services.activity.home.login.LoginActivity
import com.cloudwell.paywell.services.activity.home.model.RequestGenerateOTPReg
import com.cloudwell.paywell.services.activity.home.model.refreshToken.RequestRefreshToken
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.NewAppsVideoPreviewDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.NoInternetConnectionMsgDialog
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.AndroidIDUtility
import com.cloudwell.paywell.services.utils.AppsStatusConstant
import com.cloudwell.paywell.services.utils.RootUtil
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : BaseActivity() {

    var userNeedToChangePassword: Boolean = false
    var userPin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        getSupportActionBar()?.hide()

        val userName = AppHandler.getmInstance(AppController.getContext()).userName
        val androidId = AppHandler.getmInstance(AppController.getContext()).androidID

        val model = RequestRefreshToken()
        model.username = userName
        model.channel = "android"
        model.deviceId = androidId
        model.format = "json"

        initilizationView(intent)

//        test()


    }

    private fun test() {
        val r = RequestGenerateOTPReg()
        r.phoneNUmber = "0284"

        ApiUtils.getAPITest("https://6e92857bbdcec6faa3fac6198f4cf1c4.m.pipedream.net").test(r).enqueue(object : Callback<RequestBody> {
            override fun onResponse(call: Call<RequestBody>, response: Response<RequestBody>) {

                Log.e("", "")
            }

            override fun onFailure(call: Call<RequestBody>, t: Throwable) {
                Log.e("", "")
            }
        })

    }

    private fun initilizationView(intent: Intent) {
        val userNeedToChnagePasswordLocal = AppHandler.getmInstance(applicationContext).userNeedToChnagePassword
        val appStatus = AppHandler.getmInstance(applicationContext).appStatus

        if (userNeedToChnagePasswordLocal) {
            userNeedToChangePassword = true

        } else if (appStatus.equals(AppsStatusConstant.KEY_unknown)) {
            checkUserPreviousRegistionStatus();
        } else if (appStatus.equals(AppsStatusConstant.KEY_logout)) {
            val i = Intent(this@HomeActivity, LoginActivity::class.java)
            i.putExtra("pin", userPin)
            startActivity(i)
            finish()

        } else if (appStatus.equals(AppsStatusConstant.KEY_pending) || appStatus.equals(AppsStatusConstant.KEY_pinNotSetUser) || appStatus.equals(AppsStatusConstant.KEY_registered)) {
            val i = Intent(this@HomeActivity, AppLoadingActivity::class.java)
            i.putExtra("pin", userPin)
            startActivity(i)
            finish()
        }
        // check device is support
        val androidID = AppHandler.getmInstance(applicationContext).androidID
        Logger.v("Android ID: " + androidID)
        val isRooted = RootUtil.isDeviceRooted();
        Logger.v("Android Root: " + isRooted)

        if (androidID == "") {
            val androidID1 = AndroidIDUtility.getAndroidID(applicationContext)
            if (androidID1 != null) {
                if (androidID1.equals("")) {
                    // device not support
                    callPreview(false, getString(R.string.device_not_support))

                } else {
                    AppHandler.getmInstance(getApplicationContext()).setAndroidID(androidID1)
                }
            }

        }

        if (isRooted.equals(true)) {
            callPreview(false, getString(R.string.device_not_support_root))
        }

        btRegistration.setOnClickListener {
            val intent = Intent(applicationContext, EntryMainActivity::class.java)
            startActivity(intent)
        }

        btLogin.setOnClickListener {

            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)


        }


        showLanguageIcon()

        ivLanSwitch.setOnClickListener {
            switchLanguage()
            val refresh = Intent(this, HomeActivity::class.java)
            startActivity(refresh)
        }

        ivGetHelpCallCenter.setOnClickListener {
//            callPreviewAirticket(false)

            val i = Intent(applicationContext, PreLoginFAQActivity::class.java)
            startActivity(i)
        }
        tvGetHelp.setOnClickListener {
            val i = Intent(applicationContext, PreLoginFAQActivity::class.java)
            startActivity(i)
        }


        if (appStatus.equals(AppsStatusConstant.KEY_unregistered)) {
            if (!AppHandler.getmInstance(getApplicationContext()).isVideoPreviewShow()) {
                AppHandler.getmInstance(getApplicationContext()).setVideoPreviewShow(true)
                val newAppsVideoPreviewDialog = NewAppsVideoPreviewDialog()
                newAppsVideoPreviewDialog.show(supportFragmentManager, "newAppsVideoPreviewDialog")
            }
        }

    }

    private fun checkUserPreviousRegistionStatus() {
        if (isInternetConnection) {
            routToApploadingActivity();
        } else {
            val noInternetConnectionMsgDialog = NoInternetConnectionMsgDialog(object : NoInternetConnectionMsgDialog.OnClickHandler {
                override fun onRetry() {
                    checkUserPreviousRegistionStatus();
                }
            })
            noInternetConnectionMsgDialog.isCancelable = false
            noInternetConnectionMsgDialog.show(supportFragmentManager, "noInternetConnectionMsgDialog")
        }

    }

    private fun routToApploadingActivity() {
        val i = Intent(this@HomeActivity, AppLoadingActivity::class.java)
        i.putExtra("pin", userPin)
        startActivity(i)
        finish()

    }


    private fun showLanguageIcon() {
        val mAppHandler = AppHandler.getmInstance(applicationContext)
        if (mAppHandler.appLanguage == "unknown") {
            ivLanSwitch.setImageResource(R.drawable.ic_bangla)
        } else if (mAppHandler.appLanguage == "en") {
            ivLanSwitch.setImageResource(R.drawable.ic_bangla)
        } else if (mAppHandler.appLanguage == "bn") {
            ivLanSwitch.setImageResource(R.drawable.ic_english)
        }
    }

}
