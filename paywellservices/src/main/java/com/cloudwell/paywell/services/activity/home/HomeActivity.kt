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
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppThemeBaseActivity() {


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


    //    public static String decrypt(String strToDecrypt, String secret) {
//        try {
//
//
//            key = myKey.getBytes("UTF-8");
//            sha = MessageDigest.getInstance("SHA-1");
//            key = sha.digest(key);
//            key = Arrays.copyOf(key, 16);
//            secretKey = new SecretKeySpec(key, "AES");
//
//
//
//
//
//
//            key = strToDecrypt.substring(0, 16).getBytes();
//            secretKey = new SecretKeySpec(key, "AES");
//            String lastData = strToDecrypt.substring(17, strToDecrypt.length() - 1);
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            return new String(cipher.doFinal(Base64.decode(lastData.getBytes(), Base64.DEFAULT)));
//        } catch (Exception e) {
//            System.out.println("Error while decrypting: " + e.toString());
//        }
//        return null;
//    }





}
