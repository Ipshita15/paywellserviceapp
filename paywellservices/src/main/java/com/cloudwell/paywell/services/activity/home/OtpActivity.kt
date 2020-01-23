package com.cloudwell.paywell.services.activity.home

import android.content.IntentFilter
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AppThemeBaseActivity
import com.cloudwell.paywell.services.activity.home.model.RequestOtpCheck
import com.cloudwell.paywell.services.activity.home.model.ResposeAppsAuth
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.AppHelper
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.otp_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class OtpActivity : AppThemeBaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OtpReceivedInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_dialog)

        setToolbar("OTP Verification")

        btbtSubmitMobileNumber.setOnClickListener {

            val otp = etMobileOrRID.text

            callCheckOTP("" + otp)

        }

        etMobileOrRID.requestFocus();




        btCancel.setOnClickListener {
            finish()
        }
    }

    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mSmsBroadcastReceiver: SmsBroadcastReceiver


    override fun onOtpReceived(otp: String?) {
        Toast.makeText(applicationContext, "" + otp, Toast.LENGTH_LONG).show()
        val parseCode = parseCode(otp)
        etMobileOrRID.setText(parseCode)

        callCheckOTP("" + parseCode)


    }


    override fun onOtpTimeout() {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {


    }

    override fun onConnected(p0: Bundle?) {


    }

    override fun onConnectionSuspended(p0: Int) {
    }


    override fun onStart() {
        super.onStart()
        try {
            if (mGoogleApiClient == null) {

                mSmsBroadcastReceiver = SmsBroadcastReceiver()


                mGoogleApiClient = GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addApi(Auth.CREDENTIALS_API)
                        .enableAutoManage(this, this)
                        .build()
                mSmsBroadcastReceiver.setOnOtpListeners(this)
                val intentFilter = IntentFilter()
                intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
               registerReceiver(mSmsBroadcastReceiver, intentFilter)

                startSMSListener()


            }
        } catch (e: Exception) {


        }


    }


    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.stopAutoManage(this)
            mGoogleApiClient!!.disconnect()
            mGoogleApiClient = null
        }
        unregisterReceiver(mSmsBroadcastReceiver)

    }


    fun startSMSListener() {
        val mClient = SmsRetriever.getClient(this)
        val mTask = mClient.startSmsRetriever()
        mTask.addOnSuccessListener {

            Toast.makeText(this, "SMS Retriever starts", Toast.LENGTH_LONG).show()
        }
        mTask.addOnFailureListener { Toast.makeText(this, "Error", Toast.LENGTH_LONG).show() }
    }


    public interface OnClickHandler {

        public fun otpAutoCall(mobileNumber: String)
        public fun otpManualCall(mobileNumber: String)
    }

    fun parseCode(message: String?): String? {
        val p: Pattern = Pattern.compile("\\b\\d{4}\\b")
        val m: Matcher = p.matcher(message)
        var code = ""
        while (m.find()) {
            code = m.group(0)
        }
        return code
    }

    private fun callCheckOTP(otp: String) {
        showProgressDialog()

        val androidId: String = AppHelper.getAndroidID(contentResolver);


        val m = RequestOtpCheck()
        m.format = "json"
        m.otp = otp
        m.username = androidId


        val envlope = AppHandler.getmInstance(applicationContext).envlope
        val sealedData = AppHandler.getmInstance(applicationContext).sealedData

        val p = getPrivateKey()


        val cipher = Cipher.getInstance("RSA")
        cipher?.init(Cipher.DECRYPT_MODE,  p)
        val rowEnvlope = Base64.decode(envlope.toByteArray(), Base64.DEFAULT)
        val rowEnvlopeDecrytionKey = cipher.doFinal(rowEnvlope)
//        val envlpeDecryptionKey= Base64.encodeToString(rowEnvlopeDecrytionKey, Base64.DEFAULT)



        /* Decrypt the message, given derived encContentValues and initialization vector. */

        val rowSealData = Base64.decode(sealedData.toByteArray(), Base64.DEFAULT)



        val cipherRC4 = Cipher.getInstance("RC4") // Transformation of the algorithm
        val secretKeySpec = SecretKeySpec(rowEnvlopeDecrytionKey, "RC4")
        cipherRC4.init(Cipher.DECRYPT_MODE,secretKeySpec)
//
        val sealedDataDecryption = cipher.doFinal(rowSealData);
        val key = Base64.encodeToString(sealedDataDecryption, Base64.DEFAULT)
        com.orhanobut.logger.Logger.v("")




        ApiUtils.getAPIServiceV2().checkOTP(m).enqueue(object : Callback<ResposeAppsAuth> {
            override fun onResponse(call: Call<ResposeAppsAuth>, response: Response<ResposeAppsAuth>) {
                dismissProgressDialog()
                if (response.isSuccessful) {


                }
            }

            override fun onFailure(call: Call<ResposeAppsAuth>, t: Throwable) {
                dismissProgressDialog();
                com.orhanobut.logger.Logger.e("" + t.message)
            }
        })

    }

    private fun getPrivateKey(): PrivateKey? {
        val privateString = AppHandler.getmInstance(applicationContext).rsaKays.get(0);
        val decodePrivateKey = Base64.decode(privateString, Base64.DEFAULT)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        val p = kf.generatePrivate(PKCS8EncodedKeySpec(decodePrivateKey))
        return p
    }


}
