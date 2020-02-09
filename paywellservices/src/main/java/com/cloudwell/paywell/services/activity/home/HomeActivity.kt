package com.cloudwell.paywell.services.activity.home

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import com.cloudwell.paywell.services.BuildConfig
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.AppLoadingActivity
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.home.model.RequestAppsAuth
import com.cloudwell.paywell.services.activity.home.model.ResposeAppsAuth
import com.cloudwell.paywell.services.activity.home.model.forgetPin.ReposeForgetPIn
import com.cloudwell.paywell.services.activity.home.model.forgetPin.RequestForgetPin
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.ForgetPinNumberDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.MobileNumberInputDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.NoInternetConnectionMsgDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.OTPVerificationMsgDialog
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.AndroidIDUtility
import com.cloudwell.paywell.services.utils.AppsStatusConstant
import com.cloudwell.paywell.services.utils.DateUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : BaseActivity() {

     var userNeedToChangePassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        getSupportActionBar()?.hide()

//        AppHandler.getmInstance(applicationContext).setisSuccessfulPassRemissionFlowFlow(true)
//        AppHandler.getmInstance(applicationContext).setAppStatus("pendingLogin")


//        AppHandler.getmInstance(applicationContext).appStatus = AppsStatusConstant.KEY_pending



        initilizationView(intent)


    }

    private fun initilizationView(intent: Intent) {
        var userNeedToChnagePasswordLocal = AppHandler.getmInstance(applicationContext).userNeedToChnagePassword

        val appStatus = AppHandler.getmInstance(applicationContext).appStatus


        if (userNeedToChnagePasswordLocal) {
            userNeedToChangePassword = true

        }
        else if (appStatus.equals(AppsStatusConstant.KEY_unknown)){

            checkUserPreviousRegistionStatus();

        }
        else if (appStatus.equals(AppsStatusConstant.KEY_pending) || appStatus.equals(AppsStatusConstant.KEY_pinNotSetUser) || appStatus.equals(AppsStatusConstant.KEY_registered)) {
            val i = Intent(this@HomeActivity, AppLoadingActivity::class.java)
            startActivity(i)
            finish()
        }
        // check device is support
        val androidID = AppHandler.getmInstance(applicationContext).androidID
        Logger.v("Android ID: " + androidID)

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


                        requestAPIToken(androidId, userName, pin)

                    } else {
                        Toast.makeText(applicationContext, "Please input valid RID or Mobile number", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onForgetPinNumber() {

                    val mobileNumberInputDialog = ForgetPinNumberDialog(object : ForgetPinNumberDialog.OnClickHandler {

                        override fun onForgetPinNumber(moibleNumber: String) {

                            requestResetPassord(moibleNumber)
                        }
                    })

                    mobileNumberInputDialog.show(supportFragmentManager, "mobileNumberInputDialog");
                }


            })
            mobileNumberInputDialog.show(supportFragmentManager, "mobileNumberInputDialog");

        }


        showLanguageIcon()

        ivLanSwitch.setOnClickListener {
            switchLanguage()

        }

        ivGetHelpCallCenter.setOnClickListener {
            callPreviewAirticket(false)
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
        startActivity(i)
        finish()

    }

    private fun requestAPIToken(androidId: String, userName: String, pin: String) {
        if (androidId.equals("")) {
            callPreview(false, getString(R.string.device_not_support))
        } else if (isInternetConnection) {
            callGetTokenAPI(userName, pin, androidId);
        } else {
            val noInternetConnectionMsgDialog = NoInternetConnectionMsgDialog(object : NoInternetConnectionMsgDialog.OnClickHandler {
                override fun onRetry() {
                    requestAPIToken(androidId, userName, pin)
                }
            })
            noInternetConnectionMsgDialog.show(supportFragmentManager, "noInternetConnectionMsgDialog")
        }
    }

    private fun requestResetPassord(moibleNumber: String) {
        if (isInternetConnection) {
            callForgetPasswordRequest(moibleNumber)
        } else {
            val noInternetConnectionMsgDialog = NoInternetConnectionMsgDialog(object : NoInternetConnectionMsgDialog.OnClickHandler {
                override fun onRetry() {
                    requestResetPassord(moibleNumber)
                }
            })
            noInternetConnectionMsgDialog.show(supportFragmentManager, "noInternetConnectionMsgDialog")
        }
    }

    private fun callForgetPasswordRequest(mobileNumber: String) {
        showProgressDialog()

        val m = RequestForgetPin()
        m.username = mobileNumber
        m.deviceId = AppHandler.getmInstance(applicationContext).androidID
        m.format = "json"
        m.timestamp = "" + DateUtils.getCurrentTimestamp();

        ApiUtils.getAPIServiceV2().resetPassword(m).enqueue(object : Callback<ReposeForgetPIn> {
            override fun onResponse(call: Call<ReposeForgetPIn>, response: Response<ReposeForgetPIn>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    response.body().let {
                        if (it?.apiStatus ?: 0 == 200) {
                            if (it?.responseDetails!!.status == 200) {

                                AppHandler.getmInstance(applicationContext).setUserNeedToChangePassword(true)
                                userNeedToChangePassword = true


                                val oTPVerificationMsgDialog = OTPVerificationMsgDialog(object : OTPVerificationMsgDialog.OnClickHandler {
                                    override fun onSubmit() {

                                    }
                                }, it.responseDetails!!.statusName)
                                oTPVerificationMsgDialog.show(supportFragmentManager, "oTPVerificationMsgDialog");

                            } else {
                                response.body()?.apiStatusName?.let { it1 -> showErrorMessagev1(it1) }
                            }
                        } else {
                            response.body()?.apiStatusName?.let { it1 -> showErrorMessagev1(it1) }
                        }
                    }

                }
            }

            override fun onFailure(call: Call<ReposeForgetPIn>, t: Throwable) {
                dismissProgressDialog()
                showTryAgainDialog()


            }
        })

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
        } else {
            isDebug = 1
        }

        val authRequestModel = RequestAppsAuth(isDebug, androidId, privateKey, channel, "" + currentTimestamp)

        ApiUtils.getAPIServiceV2().getAppsAuthToken(authHeader, authRequestModel).enqueue(object : Callback<ResposeAppsAuth> {
            override fun onResponse(call: Call<ResposeAppsAuth>, response: Response<ResposeAppsAuth>) {

                dismissProgressDialog()

                val m = response.body()

                m.let {
                    if (m?.status == 200) {

                        AppHandler.getmInstance(applicationContext).setSealedData(m?.sealedData)
                        AppHandler.getmInstance(applicationContext).setEnvlope(m?.envlope)
                        AppHandler.getmInstance(applicationContext).setAppsSecurityToken(m?.token?.securityToken)
                        AppHandler.getmInstance(applicationContext).setAppsTokenExpTime(m?.token?.tokenExpTime)
                        AppHandler.getmInstance(applicationContext).setUserName(userName)


                        if (m.checkOTP == 1) {

                            val intent = Intent(this@HomeActivity, OtpActivity::class.java)
                            intent.putExtra("OTPMessaage", m?.OTPMessaage)
                            intent.putExtra("userNeedToChangePassword", userNeedToChangePassword)
                            startActivity(intent)

                        } else {

                            val intent = Intent(this@HomeActivity, AppLoadingActivity::class.java)
                            intent.putExtra("userNeedToChangePassword", userNeedToChangePassword)
                            startActivity(intent)
                            finish()

                        }
                    } else {
                        it?.message?.let { it1 -> showErrorMessagev1(it1) }
                    }

                }


            }

            override fun onFailure(call: Call<ResposeAppsAuth>, t: Throwable) {
                dismissProgressDialog();
                showTryAgainDialog()

            }
        })

    }

}
