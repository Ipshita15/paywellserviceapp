package com.cloudwell.paywell.services.activity.settings

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.AppLoadingActivity
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.home.model.changePin.RequestChangePin
import com.cloudwell.paywell.services.activity.home.model.forgetPin.ReposeForgetPIn
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.NoInternetConnectionMsgDialog
import com.cloudwell.paywell.services.analytics.AnalyticsManager
import com.cloudwell.paywell.services.analytics.AnalyticsParameters
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.AppsStatusConstant
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.cloudwell.paywell.services.utils.DateUtils
import kotlinx.android.synthetic.main.activity_change_pin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePinActivity : BaseActivity() {
    private var mCd: ConnectionDetector? = null
    private var mAppHandler: AppHandler? = null
    private var mOldPin: EditText? = null
    private var mNewPin: EditText? = null
    var newPinAgain: EditText? = null
    private var mLinearLayout: LinearLayout? = null
    var isFirstTime = false
    var pin = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)
        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.home_settings_change_pin)
        }
        isFirstTime = intent.getBooleanExtra("isFirstTime", false)
        if (isFirstTime) {
            tvOldPInSymbol.visibility = View.GONE
            tvOldPin.visibility = View.GONE
            oldPin.visibility = View.GONE
            pin = intent.getStringExtra("pin")
        }
        mCd = ConnectionDetector(AppController.getContext())
        mAppHandler = AppHandler.getmInstance(applicationContext)
        initView()
        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_SETTINGS_CHANGE_PIN_MENU)
    }

    private fun initView() {
        mLinearLayout = findViewById(R.id.linearLayout)
        mOldPin = findViewById(R.id.oldPin)
        mNewPin = findViewById(R.id.newPin)
        newPinAgain = findViewById(R.id.newPinAgain)
    }

    fun resetPin(v: View?) {
        var _oldPin = mOldPin!!.text.toString()
        val _newPin = mNewPin!!.text.toString()
        val _newPinAgain = newPinAgain!!.text.toString()

        if (!isFirstTime) {
            if (_oldPin.length == 0) {
                mOldPin!!.error = Html.fromHtml("<font color='red'>" + getString(R.string.old_pin_error_msg) + "</font>")
                return
            }
        } else {
            _oldPin = pin
        }
        if (_newPin.length == 0 || _oldPin.equals(_newPin, ignoreCase = true)) {
            mNewPin!!.error = Html.fromHtml("<font color='red'>" + getString(R.string.new_pin_error_msg) + "</font>")
            return
        }
        if (_newPinAgain != _newPin) {
            mNewPin!!.error = Html.fromHtml("<font color='red'>" + getString(R.string.new_pin_error_msg) + "</font>")
            newPinAgain!!.error = Html.fromHtml("<font color='red'>" + getString(R.string.new_pin_error_msg) + "</font>")
            return
        }
        if (!mCd!!.isConnectingToInternet) {
            AppHandler.showDialog(supportFragmentManager)
        } else {

            changePassword(_oldPin, _newPin)

        }
    }


    private fun changePassword(_oldPin: String, _newPin: String) {
        if (isInternetConnection) {
            callChangePasswordRequest(_oldPin, _newPin)
        } else {
            val noInternetConnectionMsgDialog = NoInternetConnectionMsgDialog(object : NoInternetConnectionMsgDialog.OnClickHandler {
                override fun onRetry() {
                    callChangePasswordRequest(_oldPin, _newPin)
                }
            })
            noInternetConnectionMsgDialog.show(supportFragmentManager, "noInternetConnectionMsgDialog")
        }
    }

    private fun callChangePasswordRequest(_oldPin: String, _newPin: String) {
        showProgressDialog()

        val m = RequestChangePin()
        m.username = AppHandler.getmInstance(applicationContext).userName
        m.deviceId = AppHandler.getmInstance(applicationContext).androidID
        m.format = "json"
        m.timestamp = "" + DateUtils.getCurrentTimestamp()
        m.oldPin = _oldPin
        m.newPin = _newPin


        ApiUtils.getAPIServiceV2().changePassword(m).enqueue(object : Callback<ReposeForgetPIn> {
            override fun onResponse(call: Call<ReposeForgetPIn>, response: Response<ReposeForgetPIn>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    response.body().let {
                        if (it?.apiStatus ?: 0 == 200) {
                            if (it?.responseDetails!!.status == 200) {


                                mAppHandler!!.initialChangePinStatus = "true"
                                AppHandler.getmInstance(applicationContext).appStatus = AppsStatusConstant.KEY_registered
                                AppHandler.getmInstance(applicationContext).setUserNeedToChangePassword(false)


                                val builder = AlertDialog.Builder(this@ChangePinActivity)
                                builder.setCancelable(false)
                                builder.setTitle("Result")
                                builder.setMessage(R.string.change_pin_status_msg)
                                builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
                                    dialogInterface.dismiss()
                                    if (isFirstTime) {
                                        val intent = Intent(applicationContext, AppLoadingActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        onBackPressed()
                                    }
                                }
                                val alert = builder.create()
                                alert.show()
                            } else {
                                val builder = AlertDialog.Builder(this@ChangePinActivity)
                                builder.setTitle("Result")
                                builder.setMessage(it.responseDetails?.statusName)
                                builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id -> dialogInterface.dismiss() }
                                val alert = builder.create()
                                alert.show()
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
    

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }
}