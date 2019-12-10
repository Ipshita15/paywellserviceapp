package com.cloudwell.paywell.services.activity.home

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AppThemeBaseActivity
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.MobileNumberInputDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.OTPErrorMsgDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.OTPInputDialog
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppThemeBaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OtpReceivedInterface {
    override fun onOtpReceived(otp: String?) {

        Toast.makeText(applicationContext, ""+otp, Toast.LENGTH_LONG).show()

    }

    override fun onOtpTimeout() {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {


    }

    override fun onConnected(p0: Bundle?) {


    }

    override fun onConnectionSuspended(p0: Int) {
    }

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mSmsBroadcastReceiver: SmsBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        getSupportActionBar()?.hide()

        btRegistration.setOnClickListener {
            val intent = Intent(applicationContext, EntryMainActivity::class.java)
            startActivity(intent)
        }



        mSmsBroadcastReceiver = SmsBroadcastReceiver()

        //set google api client for hint request
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build()

        mSmsBroadcastReceiver.setOnOtpListeners(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        applicationContext.registerReceiver(mSmsBroadcastReceiver, intentFilter)



        btLogin.setOnClickListener {

            val mobileNumberInputDialog = MobileNumberInputDialog(object : MobileNumberInputDialog.OnClickHandler {
                override fun onSubmit(mobileNumber: String) {

                    startSMSListener();

                }

            })
            mobileNumberInputDialog.show(supportFragmentManager, "mobileNumberInputDialog");

        }


        val oTPInputDialog = OTPInputDialog(object : OTPInputDialog.OnClickHandler {
            override fun onSubmit(mobileNumber: String) {


            }

        })
        oTPInputDialog.show(supportFragmentManager, "mobileNumberInputDialog");



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


    fun startSMSListener() {
        val mClient = SmsRetriever.getClient(this)
        val mTask = mClient.startSmsRetriever()
        mTask.addOnSuccessListener {

            Toast.makeText(this@HomeActivity, "SMS Retriever starts", Toast.LENGTH_LONG).show()
        }
        mTask.addOnFailureListener { Toast.makeText(this@HomeActivity, "Error", Toast.LENGTH_LONG).show() }
    }
}
