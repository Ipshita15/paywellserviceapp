package com.cloudwell.paywell.services.activity.home

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.AppLoadingActivity
import com.cloudwell.paywell.services.activity.base.AppThemeBaseActivity
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.home.model.RequestOtpCheck
import com.cloudwell.paywell.services.activity.home.model.ResposeOptCheck
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.OTPVerificationMsgDialog
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.AppHelper
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.otp_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class OtpActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OtpReceivedInterface {

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
        m.username = AppHandler.getmInstance(applicationContext).userName


        val envlopeString = AppHandler.getmInstance(applicationContext).envlope

        val p = getPrivateKey()
        val rowEnvlopeDecrytionKey = getRowEnvlopeDecrytionKey(p, envlopeString)
        val envlpeDecryptionKey = Base64.encodeToString(rowEnvlopeDecrytionKey, Base64.DEFAULT)


        /* Decrypt the message, given derived encContentValues and initialization vector. */
        val sealedDataString = AppHandler.getmInstance(applicationContext).sealedData
        val sealDataDecode = Base64.decode(sealedDataString.toByteArray(Charsets.UTF_8), Base64.DEFAULT)

        val secretKeySpec = SecretKeySpec(rowEnvlopeDecrytionKey, "RC4")
        val cipherRC4 = Cipher.getInstance("RC4") // Transformation of the algorithm
        cipherRC4.init(Cipher.DECRYPT_MODE, secretKeySpec)
        val rowDecryptionKey = cipherRC4.doFinal(sealDataDecode)
        val sealDecryptionKey = Base64.encodeToString(rowDecryptionKey, Base64.NO_WRAP)
        val sealDecryptionKeyDecodeFormate = String(Base64.decode(sealDecryptionKey, android.util.Base64.NO_WRAP))


        val appsSecurityToken = AppHandler.getmInstance(applicationContext).appsSecurityToken
        val encodeAppsSecurityToken = Base64.encodeToString(appsSecurityToken.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)


        val toJson = Gson().toJson(m)

        val hmac: String? = calculateHMAC(toJson, sealDecryptionKeyDecodeFormate)

        val authDataString = "$encodeAppsSecurityToken:$hmac"


        val authHeader = "Bearer " + Base64.encodeToString(authDataString.toByteArray(Charsets.UTF_8), Base64.NO_WRAP);



        ApiUtils.getAPIServiceV2().checkOTP(authHeader, m).enqueue(object : Callback<ResposeOptCheck> {
            override fun onResponse(call: Call<ResposeOptCheck>, response: Response<ResposeOptCheck>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    response.body().let {
                        if (it?.apiStatus ?: 0 == 200) {


                            val oTPVerificationMsgDialog = OTPVerificationMsgDialog(object : OTPVerificationMsgDialog.OnClickHandler {
                                override fun onSubmit() {
                                    AppHandler.getmInstance(applicationContext).setSuccessfulPassAuthenticationFlow(true)
                                    val i = Intent(this@OtpActivity, AppLoadingActivity::class.java)
                                    startActivity(i)
                                    finish()
                                }

                            } , it?.responseDetails!!.statusName)
                            oTPVerificationMsgDialog.show(supportFragmentManager, "oTPVerificationMsgDialog");


                        }else{

                        }
                    }


                }
            }

            override fun onFailure(call: Call<ResposeOptCheck>, t: Throwable) {
                dismissProgressDialog();
                com.orhanobut.logger.Logger.e("" + t.message)
            }
        })

    }

    private fun getRowEnvlopeDecrytionKey(p: PrivateKey?, envlopeString: String): ByteArray? {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher?.init(Cipher.DECRYPT_MODE, p)
        val envlopeDecode = Base64.decode(envlopeString.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        val rowEnvlopeDecrytionKey = cipher.doFinal(envlopeDecode)
        return rowEnvlopeDecrytionKey
    }

    private fun getPrivateKey(): PrivateKey? {
        val privateString = AppHandler.getmInstance(applicationContext).rsaKays.get(0)
        val decodePrivateKey = Base64.decode(privateString, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(decodePrivateKey)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        val p = kf.generatePrivate(keySpec)
        return p
    }

    @Throws(SignatureException::class, NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun calculateHMAC(data: String, key: String): String? {
        val HMAC_SHA512 = "HmacSHA256"
        val secretKeySpec = SecretKeySpec(key.toByteArray(), HMAC_SHA512)
        val mac: Mac = Mac.getInstance(HMAC_SHA512)
        mac.init(secretKeySpec)
        return toHexString(mac.doFinal(data.toByteArray()))
    }


    private fun toHexString(bytes: ByteArray): String? {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }

}
