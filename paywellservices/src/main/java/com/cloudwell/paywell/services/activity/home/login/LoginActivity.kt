package com.cloudwell.paywell.services.activity.home.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Base64
import android.widget.Toast
import com.cloudwell.paywell.services.BuildConfig
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.AppLoadingActivity
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.faq.PreLoginFAQActivity
import com.cloudwell.paywell.services.activity.home.OtpActivity
import com.cloudwell.paywell.services.activity.home.model.RequestAppsAuth
import com.cloudwell.paywell.services.activity.home.model.ResposeAppsAuth
import com.cloudwell.paywell.services.activity.home.model.forgetPin.ReposeForgetPIn
import com.cloudwell.paywell.services.activity.home.model.forgetPin.RequestForgetPin
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.ForgetPinNumberDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.NoInternetConnectionMsgDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.OTPVerificationMsgDialog
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.DateUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class LoginActivity : BaseActivity() {
    var userPin: String = ""
    var userNeedToChangePassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setToolbar(getString(R.string.title_login))

        initilation()
    }

    private fun initilation() {
        etAccountID.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeButtonColorStatus()

            }
        })

        etPinNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeButtonColorStatus()
            }
        })

        btSingIn.setOnClickListener {
            hiddenSoftKeyboard()

            val accountIDString = etAccountID.text.toString().trim()
            val pinNumberString = etPinNumber.text.toString().trim()
            doLogin(accountIDString, pinNumberString)
        }


        tvForgotPIn.setOnClickListener {
            val mobileNumberInputDialog = ForgetPinNumberDialog(object : ForgetPinNumberDialog.OnClickHandler {
                override fun onForgetPinNumber(moibleNumber: String) {
                    requestResetPassord(moibleNumber)
                }
            })
            mobileNumberInputDialog.show(supportFragmentManager, "mobileNumberInputDialog")
        }


        val userName = AppHandler.getmInstance(applicationContext).userName
        userName.let {
            if (it.equals("unknown")) {

            } else {
                etAccountID.setText(it)
                etAccountID.setFocusable(false)
                etAccountID.setTextIsSelectable(false)
                etAccountID.setInputType(InputType.TYPE_NULL)
                etPinNumber.requestFocus()
            }
        }

        imagCallButton.setOnClickListener {
            val i = Intent(applicationContext, PreLoginFAQActivity::class.java)
            startActivity(i)
        }

        tvGetHelpLoginButton.setOnClickListener {
            val i = Intent(applicationContext, PreLoginFAQActivity::class.java)
            startActivity(i)
        }
    }

    private fun changeButtonColorStatus() {
        val accountIDString = etAccountID.text.toString().trim()
        val pinNumberString = etPinNumber.text.toString().trim()
        if (!accountIDString.isEmpty() && !pinNumberString.isEmpty()) {
            btSingIn.setBackgroundResource(R.drawable.rounded_button_sign_in_enable)
        } else {
            btSingIn.setBackgroundResource(R.drawable.rounded_button_sign_in_disable)
        }
    }

    private fun doLogin(mobileNumber: String, pin: String) {

        if (!mobileNumber.equals("")) {

            val firebaseId = AppHandler.getmInstance(applicationContext).firebaseId
            if (firebaseId.equals("unknown")) {
                Toast.makeText(getApplicationContext(), R.string.try_again_msg, Toast.LENGTH_LONG).show();
                getFCMTokenAndSave();
                return
            }

            val userName = mobileNumber
            val pin = pin

            val androidId: String = AppHandler.getmInstance(applicationContext).androidID
            AppHandler.getmInstance(applicationContext).setAndroidID(androidId)
            AppHandler.getmInstance(applicationContext).setMobileNumber(userName)

            requestAPIToken(androidId, userName, pin, firebaseId)

        } else {
            Toast.makeText(applicationContext, "Please input valid RID or Mobile number", Toast.LENGTH_LONG).show()
        }


//        val mobileNumberInputDialog = MobileNumberInputDialog(object : MobileNumberInputDialog.OnClickHandler {
//            override fun onSubmit(mobileNumber: String, pin: String) {
//                if (!mobileNumber.equals("")) {
//
//                    val firebaseId = AppHandler.getmInstance(applicationContext).firebaseId
//                    if (firebaseId.equals("unknown")){
//                        Toast.makeText(getApplicationContext(), R.string.try_again_msg, Toast.LENGTH_LONG).show();
//                        getFCMTokenAndSave();
//                        return
//                    }
//
//                    val userName = mobileNumber
//                    val pin = pin
//
//                    val androidId: String = AppHandler.getmInstance(applicationContext).androidID
//                    AppHandler.getmInstance(applicationContext).setAndroidID(androidId)
//                    AppHandler.getmInstance(applicationContext).setMobileNumber(userName)
//
//                    requestAPIToken(androidId, userName, pin, firebaseId)
//
//                } else {
//                    Toast.makeText(applicationContext, "Please input valid RID or Mobile number", Toast.LENGTH_LONG).show()
//                }
//
//            }
//
//            override fun onForgetPinNumber() {
//
//                val mobileNumberInputDialog = ForgetPinNumberDialog(object : ForgetPinNumberDialog.OnClickHandler {
//
//                    override fun onForgetPinNumber(moibleNumber: String) {
//
//                        requestResetPassord(moibleNumber)
//                    }
//                })
//
//                mobileNumberInputDialog.show(supportFragmentManager, "mobileNumberInputDialog");
//            }
//
//
//        })
//        mobileNumberInputDialog.show(supportFragmentManager, "mobileNumberInputDialog")
    }

    fun getFCMTokenAndSave() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Logger.w("getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token
                    AppHandler.getmInstance(applicationContext).setFirebaseId(token)
                })


    }


    private fun requestAPIToken(androidId: String, userName: String, pin: String, firebaseId: String) {
        if (androidId.equals("")) {
            callPreview(false, getString(R.string.device_not_support))
        } else if (isInternetConnection) {
            userPin = pin
            callGetTokenAPI(userName, pin, androidId, firebaseId);
        } else {
            val noInternetConnectionMsgDialog = NoInternetConnectionMsgDialog(object : NoInternetConnectionMsgDialog.OnClickHandler {
                override fun onRetry() {
                    requestAPIToken(androidId, userName, pin, firebaseId)
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

    private fun callGetTokenAPI(userName: String, password: String, androidId: String, firebaseId: String) {

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


        val authRequestModel = RequestAppsAuth(isDebug, androidId, privateKey, channel, "" + currentTimestamp, firebaseId)

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

                            val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                            intent.putExtra("OTPMessaage", m?.OTPMessaage)
                            intent.putExtra("userNeedToChangePassword", userNeedToChangePassword)
                            intent.putExtra("pin", password)
                            startActivity(intent)

                        } else {

                            val intent = Intent(this@LoginActivity, AppLoadingActivity::class.java)
                            intent.putExtra("userNeedToChangePassword", userNeedToChangePassword)
                            intent.putExtra("pin", password)
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

}