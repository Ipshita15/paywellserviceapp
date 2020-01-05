package com.cloudwell.paywell.services.activity.base

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.CancellationStatusMessageFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.UserAcceptDialogFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model.ResCancellationMapping
import com.cloudwell.paywell.services.activity.eticket.airticket.dosInfoUpdate.UpdateDocOrInfomationRequestActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketCencel.model.ResSingleBooking
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.constant.AllConstant
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import com.google.android.material.snackbar.Snackbar
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
        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this)!!.rid)

        ApiUtils.getAPIService().getCancelMap(userName, bookingId,uniqueKey).enqueue(object : Callback<ResCancellationMapping> {
            override fun onResponse(call: Call<ResCancellationMapping>, response: Response<ResCancellationMapping>) {
                dismissProgressDialog()
                assert(response.body() != null)
                if (response.body()!!.status == 200) {
                    showUserCancelData(bookingId, reason, response.body(), typeOfRequest, item)
                } else {
                    showMsg(response.body()!!.message, response.body()!!.status)
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

                    showProgressDialog()

                    val userName = AppHandler.getmInstance(applicationContext).imeiNo

                    val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext)!!.rid)

                    ApiUtils.getAPIService().getSingleBooking(userName, bookingId,uniqueKey).enqueue(object : Callback<ResSingleBooking> {
                        override fun onResponse(call: Call<ResSingleBooking>, response: Response<ResSingleBooking>) {
                            dismissProgressDialog()
                            assert(response.body() != null)
                            if (response.body()!!.status == 200) {
                                val newIntent = UpdateDocOrInfomationRequestActivity.newIntent(applicationContext, response.body()!!.data.get(0))
                                startActivity(newIntent)
                            } else {
                                showMsg(response.body()!!.message, response.body()!!.status)
                            }
                        }
                        override fun onFailure(call: Call<ResSingleBooking>, t: Throwable) {
                            dismissProgressDialog()
                            showSnackMessageWithTextMessage(getString(R.string.please_try_again))
                        }
                    })

                } else if (typeOfRequest == AllConstant.Action_Refund) {
                    askForPin(bookingId, reason, typeOfRequest)
                } else if (typeOfRequest == AllConstant.Action_Void) {
                    askForPin(bookingId, reason, typeOfRequest)
                } else if (typeOfRequest == AllConstant.Action_reIssueTicket) {
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
                    } else if (typeOfRequest == AllConstant.Action_Refund) {
                        submitCancelTicketRequest(userName, PIN_NO, bookingId, cancelReason, "Refund", "json")
                    } else if (typeOfRequest == AllConstant.Action_reIssueTicket) {
                        reIssueTicket(userName, PIN_NO, bookingId, cancelReason)
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

    private fun reIssueTicket(userName: String, pass: String, bookingId: String, cancelReason: String) {
        showProgressDialog()

        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this)!!.rid)


        ApiUtils.getAPIService().reIssueTicket(userName, pass, bookingId, cancelReason,uniqueKey).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val message = jsonObject!!.get("message_details").asString
                        val status = jsonObject.get("status").asInt
                        if (status == 200) {
                            showMsg(message, status)
                        } else {
                            showMsg(message, status)
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

    private fun submitCancelRequest(userName: String, pass: String, bookingId: String, cancelReason: String, apiFormat: String) {

        showProgressDialog()
        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this).rid)

        ApiUtils.getAPIService().cancelBooking(userName, pass, bookingId, cancelReason, apiFormat, uniqueKey).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {

                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val message = jsonObject!!.get("message_details").asString
                        val status = jsonObject.get("status").asInt
                        if (status == 200) {
                            showMsg(message, status)
                        } else {
                            showMsg(message, status)
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
        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this)!!.rid)
        ApiUtils.getAPIService().cancelTicket(userName, pass, bookingId, cancelReason, cancelType, apiFormat, uniqueKey).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dismissProgressDialog()
                if (response.isSuccessful) {

                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val message = jsonObject!!.get("message_details").asString
                        val status = jsonObject.get("status").asInt

                        if (status == 200) {
                            showMsg(message, status)
                        } else {
                            showMsg(message, status)
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

    private fun showMsg(msg: String, status: Int) {

        val priceChangeFragment = CancellationStatusMessageFragment()
        CancellationStatusMessageFragment.message = msg
        CancellationStatusMessageFragment.status = status
        priceChangeFragment.setHandleOnClick(object : CancellationStatusMessageFragment.HandleOnClick {
            override fun onOkClick(status: Int) {
                if (status == 200) {
                    finish()
                }
            }

        })

        priceChangeFragment.show(supportFragmentManager, "dialog")

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main_air_ticket, menu)
        return true
    }


}
