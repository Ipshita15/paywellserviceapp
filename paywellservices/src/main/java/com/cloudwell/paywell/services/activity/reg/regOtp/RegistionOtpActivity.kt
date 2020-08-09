package com.cloudwell.paywell.services.activity.reg.regOtp

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.home.OtpReceivedInterface
import com.cloudwell.paywell.services.activity.home.SmsBroadcastReceiver
import com.cloudwell.paywell.services.activity.home.model.ReposeGenerateOTPReg
import com.cloudwell.paywell.services.activity.home.model.RequestGenerateOTPReg
import com.cloudwell.paywell.services.activity.reg.EntryFirstActivity
import com.cloudwell.paywell.services.activity.reg.model.ReposeVerifyPhoneNumberForRegistration
import com.cloudwell.paywell.services.activity.reg.model.RequestRegOTPCheck
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.ErrorMsgDialog
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.dhruv.timerbutton.ButtonAnimationListener
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.otp_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class RegistionOtpActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OtpReceivedInterface {

    var userNeedToChangePassword = false;
    var OTPMessaage = ""
    var pin = ""
    var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_otp_dialog)
        setToolbar(getString(R.string.title_otp_verfication))

        phoneNumber = intent.getStringExtra("phone_number")

        tvOtpMessage.text = OTPMessaage

        btNextOtp.setOnClickListener {

            val otp = etMobileOrRID.text

            callCheckOTP("" + otp)

        }

        etMobileOrRID.requestFocus();

        btCancel.setOnClickListener {
            finish()
        }


        timer_button.setDuration(60000L);


        timer_button.setButtonAnimationListener(object : OnClickHandler, ButtonAnimationListener {
            override fun otpAutoCall(mobileNumber: String) {

            }

            override fun otpManualCall(mobileNumber: String) {

            }

            override fun onAnimationEnd() {


            }

            override fun onAnimationStart() {
                calledResendOtp();
            }

            override fun onAnimationReset() {
                Logger.v("")

            }


        })


    }

    private fun calledResendOtp() {
        showProgressDialog()

        val m = RequestGenerateOTPReg()
        m.phoneNUmber = phoneNumber

        ApiUtils.getAPIServicePHP7().generateOTPReg(m).enqueue(object : Callback<ReposeGenerateOTPReg> {
            override fun onResponse(call: Call<ReposeGenerateOTPReg>, response: Response<ReposeGenerateOTPReg>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    response.body().let {
                        if (it?.status ?: 0 == 205) {
                            Toast.makeText(applicationContext, it?.message, Toast.LENGTH_LONG).show()
                        } else {
                            val errorMsgDialog = it?.message.let { it1 -> it1?.let { it2 -> ErrorMsgDialog(it2) } }
                            errorMsgDialog?.show(supportFragmentManager, "oTPVerificationMsgDialog")
                        }
                    }

                }
            }

            override fun onFailure(call: Call<ReposeGenerateOTPReg>, t: Throwable) {
                dismissProgressDialog()
                showTryAgainDialog()
            }
        })


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

        try {
            if (mSmsBroadcastReceiver != null) {
                unregisterReceiver(mSmsBroadcastReceiver)
            }
        } catch (e: Exception) {

        }


    }


    fun startSMSListener() {
        val mClient = SmsRetriever.getClient(this)
        val mTask = mClient.startSmsRetriever()
        mTask.addOnSuccessListener {

//            Toast.makeText(this, "SMS Retriever starts", Toast.LENGTH_LONG).show()
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

        val m = RequestRegOTPCheck()
        m.otp = otp
        m.phoneNUmber = phoneNumber

        ApiUtils.getAPIServicePHP7().verifyOTPForRegistration(m).enqueue(object : Callback<ReposeVerifyPhoneNumberForRegistration> {
            override fun onResponse(call: Call<ReposeVerifyPhoneNumberForRegistration>, response: Response<ReposeVerifyPhoneNumberForRegistration>) {
                dismissProgressDialog()

                response.body().let {
                    if (it?.status ?: 0 == 206) {

                        startActivity(Intent(applicationContext, EntryFirstActivity::class.java));
                        finish()


                    } else {
                        val errorMsgDialog = it?.message?.let { it1 ->

                            ErrorMsgDialog(it1)

                        }
                        errorMsgDialog?.show(supportFragmentManager, "oTPVerificationMsgDialog")
                    }
                }
            }

            override fun onFailure(call: Call<ReposeVerifyPhoneNumberForRegistration>, t: Throwable) {
                dismissProgressDialog();
                showTryAgainDialog()
            }
        })

    }


    private fun toHexString(bytes: ByteArray): String? {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }

}
