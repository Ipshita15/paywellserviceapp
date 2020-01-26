package com.cloudwell.paywell.services.activity.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AppThemeBaseActivity
import com.cloudwell.paywell.services.activity.home.model.RequestAppsAuth
import com.cloudwell.paywell.services.activity.home.model.ResposeAppsAuth
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.MobileNumberInputDialog
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher


class HomeActivity : AppThemeBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        test()



        getSupportActionBar()?.hide()

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

                        if (isInternetConnection) {
                            callGetTokenAPI(userName, pin);
                        } else {
                            showSnackMessageWithTextMessage(getString(R.string.no_notification_msg))

                        }

                    } else {
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

    private fun test() {

        val privateString = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCPvEWyoAS3fjtvanB4YpzYFBvS3yGXiygtyA4br32nrizRNyGntEQjDF8yVc9p66tIFEwUMYPn5p3m0P9aSh2yJRmH2OAxFzPJ2nyQIMs+PFiyzF7vo5bPu8nf37+v/uqY7vTmOB2vV8GczHnbVOFIbsWR29HUT8A0D6J2zX+E1adBD6V5SAQdoq+6H3YjMOUvLt9gfHF0KkKrStL0zwNFoZNn4JS9JB4hg6VPZlqotvhJpy8DCLpVCn91jg0OQEXWX02PD5H9j8jBhmsOQaJlEGM5aTnwqw4UJ8twUY/ubopWKlYsS6tdbznW1lQjhYo+bDEE54BOKWdq95pcUT2NAgMBAAECggEAC9Wgl3h9au5FzoKhCAh2iYP+VnpwtZ2LjVlvb/AfFHNO1VsItlotUgVuwSI3la0FyUWCjhcVmT5vudVzcOexUj2jwH+m1ePnK7OFlghdM56cXvxcxLZfcHMxx/EQQ1llz3m9SEdOimVbV6GuVtTCR8h3E+9Zc3WtiZvP8KAy46jkIBl4faXHHNlvx0fwzJtleclwnxEygVUvls5FBtvdAm+LezzVaZ2LyYvDHJoqlg+46o/LS1idSBeiaIBFq4raonwKpUZijXGbFtrbVtJtbWzSFeWV2Rp/+ZMZBPavsfC77bRYcapk9SALj1fBj3QCLR/fbTfbmX6pHNkeoTfxMQKBgQDJh2amQxUSNvcxwJodTUEFG1IxsdrD5t9hkklLmecFnLdskhvlRoW5NJK/psNZnWeRi7Uvzog4oazzLcKRbrtJ2+mfQ1nRqwejZpcm3AxMJ99qqVrWCmXxPh2CM9rz9I/njzV3oa6u6/T2Wrho661Rs0NfqTCZt/dlcKb3OUfMzwKBgQC2lehaJ0oPkgGeM5qY87i+PgrePUChefNZ+1BKkznJvlUXVOY+bxPLomH4DJXCOC0Z92l2zRTgjV4wPEgPFVeGownWdu293Q56WbQkze0FyJDT/3eI94XF6vV6Yj3WNK2WIvtoydyf27OKrX1Uk/+doym9kgXOa/WJfzztCUu+4wKBgAzEf0RZS6RzxwVn5luk4VGpgXOUiP+QSOatlecsQO6iFxzRxOKprR8mrYVm00mCJ2WZLElzFD5CP+rII2ODWGo9fHeSlMYrx7gab8kOd9j7TbQ8Nn0I+5xlCwmMr3p1LAjHkeOaYq7CVCqnZLeA9uIOMV6GIYYbmZjbojhXcK0RAoGBAJ1tlcCd3bqdHm4Eeojko+bMYdyLHb3dA1kTWoBiftIXHREX78nnRj8vDJ+uYjXq7+BStglr/FM9MPgQEeWHdkctLl9Pfd9VyZTu4WdWcsaoz08rFyrumNY0p2HVcRHPq9gm43TPkD35Vc39lnGlhiGqPGQqkn0QEs5x+ds7R6cXAoGAc6srBeILcZzUz83dUVpCLAzDVi5nnqOqTpaPJD9vIPDQWqzoUI8LvrP5CIMC0bemcrcM75aR3CvauXzHbETXZ0FzryWDWYbaENLS19YgGbbnXVoPai6oPnDljYbmW+n/eGCKVS9BQ4VRVUt248UwFLmRE4y0GYwJnv3CyXQnJrc="

        val decodePrivateKey = Base64.decode(privateString, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(decodePrivateKey)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        val p = kf.generatePrivate(keySpec)


        val apiEnvoleData = "ij253m8HUmvrFgWpQPyVk9pR4TZ7NVQz2wB5ij6AIGJ6YcIY5KmBI5KD4G7aIQ3cFOGzTdbB0uze2g6mKnQnQ1o2fVSfh0PBQtvVq9SYAywpNyP1ceCrVj9vqH9kmBKOs5p61ED4xUUXyeFgY/oxL32rbBWFX1g5g1Gnb/6B6OyJsTFRQrm26DjRxweY3sAfaduaqjwOpa4ZXa5ip5gN+6WneoNjqMVhEMe1iDe0HuzRoB89oHN56kIKA5HHK644v3qHm7CW2jjcqe05I97lKijImpcnZgWnMbG9RrX6j98fwZBtbOCnVqm9ut/uaD7AwEvB6YPtI+a6RmFmL8fMfQ=="

        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher?.init(Cipher.DECRYPT_MODE,  p)


        val envlopeDecode = Base64.decode(apiEnvoleData.toByteArray(Charsets.UTF_16), Base64.DEFAULT)

        val rowEnvlopeDecrytionKey = cipher.doFinal(envlopeDecode)
        val envlpeDecryptionKey= Base64.encodeToString(rowEnvlopeDecrytionKey, Base64.DEFAULT)
        Log.e("data", "data")


    }

    private fun callGetTokenAPI(userName: String, password: String) {

        showProgressDialog()

        val base = userName + ":" + password;
        val authHeader = "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP);

        val androidId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val channel = "android"
        val currentTimestamp = System.currentTimeMillis()


        val privateKey = AppHandler.getmInstance(applicationContext).getRSAKays().get(1);


        val authRequestModel = RequestAppsAuth(0, androidId, privateKey, channel, "" + currentTimestamp)

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
