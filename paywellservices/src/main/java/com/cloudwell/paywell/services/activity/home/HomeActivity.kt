package com.cloudwell.paywell.services.activity.home

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.cloudwell.paywell.services.BuildConfig
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.AppLoadingActivity
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.home.model.RequestAppsAuth
import com.cloudwell.paywell.services.activity.home.model.ResposeAppsAuth
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.ForgetPinNumberDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.MobileNumberInputDialog
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.AndroidIDUtility
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        getSupportActionBar()?.hide()

//        AppHandler.getmInstance(applicationContext).setisSuccessfulPassRemissionFlowFlow(true)
//        AppHandler.getmInstance(applicationContext).setAppStatus("pendingLogin")



        if (AppHandler.getmInstance(applicationContext).isSuccessfulPassAuthenticationFlow()) {
            val i = Intent(this@HomeActivity, AppLoadingActivity::class.java)
            startActivity(i)
            finish()

        }else if(AppHandler.getmInstance(applicationContext).isSuccessfulPassRegistionFlow()){
            val i = Intent(this@HomeActivity, AppLoadingActivity::class.java)
            startActivity(i)
            finish()
        } else {

            // check device is support

            val androidID = AppHandler.getmInstance(applicationContext).androidID
            Logger.v("Android ID: "+androidID)
            if (BuildConfig.DEBUG){
                tvAndroidID.visibility = View.VISIBLE
                tvAndroidID.text = ""+androidID
            }else{
                tvAndroidID.visibility = View.GONE
            }

            if (androidID == "") {
                val androidID1 = AndroidIDUtility.getAndroidID(applicationContext)
                if (androidID1 != null){
                    if (androidID1.equals("")){
                        // device not support
                        callPreview(false, getString(R.string.device_not_support))

                    }else{
                        AppHandler.getmInstance(getApplicationContext()).setAndroidID(androidID1)
                    }
                }

            }

            btRegistration.setOnClickListener {
                val intent = Intent(applicationContext, EntryMainActivity::class.java)
                startActivity(intent)
            }


            btLogin.setOnClickListener {

                val mobileNumberInputDialog = MobileNumberInputDialog(object : MobileNumberInputDialog.OnClickHandler {
                    override fun onSubmit(mobileNumber: String, pin: String) {
                        if (!mobileNumber.equals("")) {
                            val userName = mobileNumber
                            val pin = pin


                            val androidId: String = AppHandler.getmInstance(applicationContext).androidID
                            AppHandler.getmInstance(applicationContext).setAndroidID(androidId)
                            AppHandler.getmInstance(applicationContext).setMobileNumber(userName)


                            if (!isInternetConnection) {
                                showSnackMessageWithTextMessage(getString(R.string.no_notification_msg))

                            } else if (androidId.equals("")) {
                                // device not support message
                            } else {
                                callGetTokenAPI(userName, pin, androidId);
                            }

                        } else {
                            Toast.makeText(applicationContext, "Please input valid RID or Mobile number", Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun onForgetPinNumber() {

                        val mobileNumberInputDialog = ForgetPinNumberDialog(object : ForgetPinNumberDialog.OnClickHandler {

                            override fun onForgetPinNumber(moibleNumber: String) {

                                callForgetPasswordRequest("", "", "")


                            }
                        })

                        mobileNumberInputDialog.show(supportFragmentManager, "mobileNumberInputDialog");
                    }




                })
                mobileNumberInputDialog.show(supportFragmentManager, "mobileNumberInputDialog");

            }


            showLanguageIcon()

            ivLanSwitch.setOnClickListener{
                switchLanguage()

                val refresh = Intent(this, HomeActivity::class.java)
                startActivity(refresh)

            }

            ivGetHelpCallCenter.setOnClickListener {
                callPreviewAirticket(false)
            }
        }


    }

    private fun callForgetPasswordRequest(s: String, s1: String, s2: String) {
//        showProgressDialog()
//
//        val m = RequestGenerateOTP()
//        m.username = "sdf"
//
//        ApiUtils.getAPIServiceV2().generateOTP(m).enqueue(object : Callback<ReposeGenerateOTP> {
//            override fun onResponse(call: Call<ReposeGenerateOTP>, response: Response<ReposeGenerateOTP>) {
//                dismissProgressDialog()
//                if (response.isSuccessful) {
//
//                    response.body().let {
//                        if (it?.apiStatus ?: 0 == 200) {
//                            if (it?.responseDetails!!.status == 200){
//                                Toast.makeText(applicationContext, it.responseDetails?.statusName, Toast.LENGTH_LONG).show()
//                            } else{
//                                Toast.makeText(applicationContext, it.responseDetails?.statusName, Toast.LENGTH_LONG).show()
//
//                            }
//                        }else{
//                            Toast.makeText(applicationContext, it?.apiStatusName, Toast.LENGTH_LONG).show()
//
//                        }
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<ReposeGenerateOTP>, t: Throwable) {
//                dismissProgressDialog();
//                com.orhanobut.logger.Logger.e("" + t.message)
//            }
//        })

    }

    private fun showLanguageIcon() {

        val mAppHandler = AppHandler.getmInstance(applicationContext)
        if (mAppHandler.appLanguage == "unknown") {
            ivLanSwitch.setImageResource(R.drawable.ic_bangla_lan)
        } else if (mAppHandler.appLanguage == "en") {
            ivLanSwitch.setImageResource(R.drawable.ic_eng_lan)
        } else if (mAppHandler.appLanguage == "bn") {
            ivLanSwitch.setImageResource(R.drawable.ic_bangla_lan)
        }
    }


    private fun callGetTokenAPI(userName: String, password: String, androidId: String) {

        showProgressDialog()

        val base = userName + ":" + password;
        val authHeader = "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP);


        val channel = "android"
        val currentTimestamp = System.currentTimeMillis()


        val privateKey = AppHandler.getmInstance(applicationContext).getRSAKays().get(1);


        var isDebug = 0
        if (BuildConfig.DEBUG) {
            isDebug = 0
        }else{
            isDebug = 1
        }

        val authRequestModel = RequestAppsAuth(isDebug, androidId, privateKey, channel, "" + currentTimestamp)

        ApiUtils.getAPIServiceV2().getAppsAuthToken(authHeader, authRequestModel).enqueue(object : Callback<ResposeAppsAuth> {
            override fun onResponse(call: Call<ResposeAppsAuth>, response: Response<ResposeAppsAuth>) {
                dismissProgressDialog()
                if (response.isSuccessful) {
                    val m = response.body()

                    m.let {
                        if (m?.checkOTP ?: 0 == 1L) {

                            AppHandler.getmInstance(applicationContext).setSealedData(m?.sealedData)
                            AppHandler.getmInstance(applicationContext).setEnvlope(m?.envlope)
                            AppHandler.getmInstance(applicationContext).setAppsSecurityToken(m?.token?.securityToken)
                            AppHandler.getmInstance(applicationContext).setAppsTokenExpTime(m?.token?.tokenExpTime)
                            AppHandler.getmInstance(applicationContext).setUserName(userName)

                            startActivity(Intent(this@HomeActivity, OtpActivity::class.java))

                        } else {


                        }
                    }


                }
            }

            override fun onFailure(call: Call<ResposeAppsAuth>, t: Throwable) {
                dismissProgressDialog();
                com.orhanobut.logger.Logger.e("" + t.message)

            }
        })

    }

}
