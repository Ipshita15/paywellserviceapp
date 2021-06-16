package com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.AppLoadingActivity
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.RequestTicketInformationForCancel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.ResponseTicketInformationCancel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.TicketInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.dialog.BusTicketSuccess
import com.cloudwell.paywell.services.activity.home.OtpReceivedInterface
import com.cloudwell.paywell.services.activity.home.SmsBroadcastReceiver
import com.cloudwell.paywell.services.activity.home.model.RequestOtpCheck
import com.cloudwell.paywell.services.activity.home.model.ResposeOptCheck
import com.cloudwell.paywell.services.activity.settings.ChangePinActivity
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.ErrorMsgDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.OTPVerificationMsgDialog
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.AppsStatusConstant
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.otp_dialog_bus_cancel.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class BusTicketCancelOtpActivity : BusTricketBaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OtpReceivedInterface {

    var userNeedToChangePassword = false;
    var OTPMessaage = ""
    var pin = ""
    var mTicketInfo = TicketInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_dialog_bus_cancel)

        setToolbar("OTP Verification")

        pin = intent.getStringExtra("pin")
        mTicketInfo = intent.getParcelableExtra<TicketInfo>("data")

        tvOtpMessage.text = OTPMessaage


        btNextOtp.setOnClickListener {

            val otp = etMobileOrRID.text

            mTicketInfo.otp = otp.toString()
            callCancelAPI(mTicketInfo, pin)

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

        val m = RequestOtpCheck()
        m.format = "json"
        m.otp = otp
        m.username = AppHandler.getmInstance(applicationContext).userName

        ApiUtils.getAPIServiceV2().checkOTP(m).enqueue(object : Callback<ResposeOptCheck> {
            override fun onResponse(call: Call<ResposeOptCheck>, response: Response<ResposeOptCheck>) {
                dismissProgressDialog()

                response.body().let {
                    if (it?.apiStatus ?: 0 == 200) {

                        if (it?.responseDetails?.status == 200) {


                            val oTPVerificationMsgDialog = OTPVerificationMsgDialog(object : OTPVerificationMsgDialog.OnClickHandler {
                                override fun onSubmit() {


                                    if (userNeedToChangePassword) {
                                        val i = Intent(this@BusTicketCancelOtpActivity, ChangePinActivity::class.java)
                                        i.putExtra("isFirstTime", true);
                                        i.putExtra("pin", pin)
                                        startActivity(i)
                                        finish()

                                    } else {
                                        AppHandler.getmInstance(applicationContext).appStatus = AppsStatusConstant.KEY_login
                                        val i = Intent(this@BusTicketCancelOtpActivity, AppLoadingActivity::class.java)
                                        i.putExtra("pin", pin)
                                        startActivity(i)
                                        finish()
                                    }


                                }

                            }, it?.responseDetails!!.statusName)
                            oTPVerificationMsgDialog.show(supportFragmentManager, "oTPVerificationMsgDialog");
                        } else {
                            showErrorMessagev1(it?.responseDetails?.statusName)
                        }

                    } else {
                        val errorMsgDialog = it?.apiStatusName?.let { it1 ->

                            ErrorMsgDialog(it1)

                        }
                        errorMsgDialog?.show(supportFragmentManager, "oTPVerificationMsgDialog")
                    }
                }
            }

            override fun onFailure(call: Call<ResposeOptCheck>, t: Throwable) {
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


    private fun callCancelAPI(ticketInfo: TicketInfo, password: String) {

        showProgressDialog()
        var mAppHandler = AppHandler.getmInstance(applicationContext)

        val userName = mAppHandler.userName

        val m = RequestTicketInformationForCancel()
        m.trxId = ticketInfo.trxId
        m.ticketNo = ticketInfo.ticketNo
        m.username = userName
        m.password = password
        m.otp = ticketInfo.otp

        ApiUtils.getAPIServiceV2().cancelTicket(m).enqueue(object : Callback<ResponseTicketInformationCancel?> {
            override fun onResponse(call: Call<ResponseTicketInformationCancel?>, response: Response<ResponseTicketInformationCancel?>) {
                dismissProgressDialog()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 200) {
                        val dialog = body?.message?.let {
                            BusTicketSuccess(it, object : BusTicketSuccess.OnClick {
                                override fun onClick() {
                                    finish()
                                }
                            })
                        }
                        dialog?.show(supportFragmentManager, "dialog");


                    } else {
                        body?.message?.let { showBusTicketErrorDialog(it, false) }

                    }
                } else {
                    showBusTicketErrorDialog(getString(R.string.network_error))
                }

            }

            override fun onFailure(call: Call<ResponseTicketInformationCancel?>, t: Throwable) {
                toast(getString(R.string.network_error))
                dismissProgressDialog()

            }
        })

    }

}
