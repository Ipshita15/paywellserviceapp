package com.cloudwell.paywell.services.activity.base

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.CancellationStatusMessageFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.UserAcceptDialogFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model.ResCancellationMapping
import com.cloudwell.paywell.services.activity.eticket.airticket.reIssueTicket.ReIssueTicketActivity
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.constant.AllConstant
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 30/1/19.
 */
open class AirTricketBaseActivity : MVVMBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switchToCzLocale(Locale.ENGLISH)
        changeAppTheme()

    }

    private fun changeAppTheme() {
        val isEnglish = AppHandler.getmInstance(applicationContext).appLanguage.equals("en", ignoreCase = true)
        if (isEnglish) {
            setTheme(R.style.EnglishAppTheme)
        } else {
            setTheme(R.style.EnglishAppTheme)
        }
    }


    fun changeAppBaseTheme() {
        setTheme(R.style.AppTheme)
    }


    override fun switchToCzLocale(locale: Locale) {
        val config = baseContext.resources.configuration
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        }
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    fun callCancelMapping(userName: String, bookingId: String, reason: String, typeOfRequest: String, item: Datum) {

        showProgressDialog()

        ApiUtils.getAPIService().getCancelMap(userName, bookingId).enqueue(object : Callback<ResCancellationMapping> {
            override fun onResponse(call: Call<ResCancellationMapping>, response: Response<ResCancellationMapping>) {
                dismissProgressDialog()
                assert(response.body() != null)
                if (response.body()!!.status == 200) {
                    showUserCancelData(bookingId, reason, response.body(), typeOfRequest, item)
                } else {
                    showSnackMessageWithTextMessage(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<ResCancellationMapping>, t: Throwable) {
                dismissProgressDialog()
                showSnackMessageWithTextMessage(getString(R.string.please_try_again))

            }


        })
    }

    private fun showUserCancelData(bookingId: String, reason: String, r: ResCancellationMapping?, typeOfRequest: String, item: Datum) {

        val priceChangeFragment = UserAcceptDialogFragment()
        UserAcceptDialogFragment.resCencelMaping = r!!
        UserAcceptDialogFragment.type = typeOfRequest

        priceChangeFragment.setOnClickHandlerTest(object : UserAcceptDialogFragment.OnClickHandler {
            override fun onClickActionIssueTicket(type: String) {

                hiddenSoftKeyboard()

                if (typeOfRequest == AllConstant.Action_DOCS_UPDATE) {

                    val newIntent = ReIssueTicketActivity.newIntent(applicationContext, item)
                    startActivity(newIntent)

                } else if (typeOfRequest == AllConstant.Action_REfund) {
                    askForPin(bookingId, reason, typeOfRequest)
                } else if (typeOfRequest == AllConstant.Action_Void) {
                    askForPin(bookingId, reason, typeOfRequest)
                }


            }

        })

        priceChangeFragment.show(supportFragmentManager, "dialog")


    }


    open fun askForPin(bookingId: String, cancelReason: String, typeOfRequest: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.pin_no_title_msg)

        val pinNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        pinNoET.gravity = Gravity.CENTER_HORIZONTAL
        pinNoET.layoutParams = lp
        pinNoET.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_PASSWORD
        pinNoET.transformationMethod = PasswordTransformationMethod.getInstance()
        builder.setView(pinNoET)

        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)

            if (pinNoET.text.toString().length != 0) {
                dialogInterface.dismiss()
                val PIN_NO = pinNoET.text.toString()
                if (isInternetConnection) {

                    val userName = AppHandler.getmInstance(applicationContext).imeiNo
                    if (typeOfRequest == AllConstant.Action_Void) {
                        submitCancelTicketRequest(userName, PIN_NO, bookingId, cancelReason, "Void", "json")
                    } else if (typeOfRequest == AllConstant.Action_REfund) {
                        submitCancelTicketRequest(userName, PIN_NO, bookingId, cancelReason, "Refund", "json")
                    }
                } else {
                    val snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                    snackbar.show()
                }
            } else {
                val snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.pin_no_error_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }
        builder.setNegativeButton(R.string.cancel_btn) { dialogInterface, i -> dialogInterface.dismiss() }
        val alert = builder.create()
        alert.show()
    }

    private fun submitCancelRequest(userName: String, pass: String, bookingId: String, cancelReason: String, apiFormat: String) {

        showProgressDialog()

        ApiUtils.getAPIService().cancelBooking(userName, pass, bookingId, cancelReason, apiFormat).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {

                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val message = jsonObject!!.get("message_details").asString
                        if (jsonObject.get("status").asInt == 200) {
                            showMsg(message)
                        } else {
                            showMsg(message)
                        }

                    }
                }
                dismissProgressDialog()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Network error!!!", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        })
    }


    public fun submitCancelTicketRequest(userName: String, pass: String, bookingId: String, cancelReason: String, cancelType: String, apiFormat: String) {
        showProgressDialog()

        ApiUtils.getAPIService().cancelTicket(userName, pass, bookingId, cancelReason, cancelType, apiFormat).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val message = jsonObject!!.get("message_details").asString
                        if (jsonObject.get("status").asInt == 200) {
                            showMsg(message)

                        } else {
                            showMsg(message)
                        }

                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Network error!!!", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        })
    }

    private fun showMsg(msg: String) {

        val priceChangeFragment = CancellationStatusMessageFragment()
        CancellationStatusMessageFragment.message = msg

        priceChangeFragment.show(supportFragmentManager, "dialog")

    }


}
