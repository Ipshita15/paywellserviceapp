package com.cloudwell.paywell.services.activity.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AppThemeBaseActivity
import com.cloudwell.paywell.services.activity.home.model.RequestAppsAuth
import com.cloudwell.paywell.services.activity.home.model.ResposeAppsAuth
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.MobileNumberInputDialog
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppThemeBaseActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        getSupportActionBar()?.hide()

        btRegistration.setOnClickListener {
            val intent = Intent(applicationContext, EntryMainActivity::class.java)
            startActivity(intent)
        }





        btLogin.setOnClickListener {

            val mobileNumberInputDialog = MobileNumberInputDialog(object : MobileNumberInputDialog.OnClickHandler {
                override fun onSubmit(mobileNumber: String, pin: String ) {
                    if(!mobileNumber.equals("")){
                        val userName = mobileNumber
                        val pin = pin

                        if (isInternetConnection){
                            callGetTokenAPI(userName, pin);
                        }else{
                            showSnackMessageWithTextMessage(getString(R.string.no_notification_msg))

                        }

                    }else{
                        Toast.makeText(applicationContext, "Please input valid RID or Mobile number", Toast.LENGTH_LONG).show()
                    }

                }

            })
            mobileNumberInputDialog.show(supportFragmentManager, "mobileNumberInputDialog");

        }






//        val otpSentMsgDialog = OTPSentMsgDialog(object : OTPSentMsgDialog.OnClickHandler {
//            override fun onSubmit(mobileNumber: String) {
//
//
//            }
//
//        })
//        otpSentMsgDialog.show(supportFragmentManager, "otpSentMessageDialog");


//        val otpVerificationMsgDialog = OTPVerificationMsgDialog(object : OTPVerificationMsgDialog.OnClickHandler {
//            override fun onSubmit(mobileNumber: String) {
//
//
//            }
//
//        })
//        otpVerificationMsgDialog.show(supportFragmentManager, "otpVerificationMessageDialog");
//
//
//    }

//        val otpErrorMsgDialog = OTPErrorMsgDialog(object : OTPErrorMsgDialog.OnClickHandler {
//            override fun onSubmit(mobileNumber: String) {
//
//
//            }
//
//        })
//        otpErrorMsgDialog.show(supportFragmentManager, "otpErrorMessageDialog");



    }

    private fun callGetTokenAPI(userName: String, password: String) {

        showProgressDialog()

        val base = userName + ":" + password;
        val authHeader = "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP);

        val androidId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val channel = "android"
        val currentTimestamp = System.currentTimeMillis()


        val authRequestModel = RequestAppsAuth(0, androidId, "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlHZk1BMEdDU3FHU0liM0RRRUJBUVVBQTRHTkFEQ0JpUUtCZ1FDdk9UVVo4a085NU80V0t4TW9MM3IyQ0NKZApwRFd1cEIyNTZVYkQvN3VwVS8wYkNJZUhaVE84WUlkMUZWZXpsR3hXRWdTQStHSSsydk52c2lab2JmVjVQZHRoClN2ZldjK0NzR1Q4WTZJbkJuUFpZTVh3dk0wb3VwNWtDSTc2Z1hmcUNMSWYwQnFVWGl1VFNTRzNxNHZrWlhGT2gKcEg5L1BvMmZ3U1ozRHNiS2Z3SURBUUFCCi0tLS0tRU5EIFBVQkxJQyBLRVktLS0tLQ==", channel, ""+currentTimestamp)

            ApiUtils.getAPIServiceV2().getAppsAuthToken(authHeader, authRequestModel).enqueue(object : Callback<ResposeAppsAuth> {
                override fun onResponse(call: Call<ResposeAppsAuth>, response: Response<ResposeAppsAuth>) {
                    dismissProgressDialog()
                    if (response.isSuccessful) {
                        startActivity(Intent(this@HomeActivity, OtpActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<ResposeAppsAuth>, t: Throwable) {
                    dismissProgressDialog();
                    com.orhanobut.logger.Logger.e("" + t.message)

                }
            })

        }







}
