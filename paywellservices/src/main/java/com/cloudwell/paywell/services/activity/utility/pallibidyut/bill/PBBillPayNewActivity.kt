package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Html
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.topup.adapter.MyRecyclerViewAdapter
import com.cloudwell.paywell.services.activity.topup.model.MobileOperator
import com.cloudwell.paywell.services.activity.utility.ivac.DrawableClickListener
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.cloudwell.paywell.services.utils.UniversalRecyclerViewAdapter
import com.cloudwell.paywell.services.utils.UniversalRecyclerViewAdapter.OnRecyclerViewItemDualViewSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_merchant_type_verify.*
import kotlinx.android.synthetic.main.activity_pbbill_pay_new.*
import kotlinx.android.synthetic.main.activity_pbbill_pay_new.view.*
import kotlinx.android.synthetic.main.activity_topup_layout.view.*
import kotlinx.android.synthetic.main.pallibidyut_action_view.view.*
import kotlinx.android.synthetic.main.pallibidyut_billpay_view.*
import kotlinx.android.synthetic.main.pallibidyut_billpay_view.view.*
import java.util.*
import kotlin.collections.ArrayList

class PBBillPayNewActivity : AppCompatActivity() {
    lateinit var cd: ConnectionDetector
    private var addNoFlag: Int = 0
    lateinit var universalRecyclerViewAdapter:UniversalRecyclerViewAdapter
    var billPayList:ArrayList<Any> =ArrayList<Any>()
    private val slideInAnim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f)
    private val slideOutAnim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1f,
            Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pbbill_pay_new)


        cd = ConnectionDetector(AppController.getContext())
        imageAddIV.setOnClickListener(View.OnClickListener {

            if (addNoFlag < AppHandler.MULTIPLE_TOPUP_LIMIT) {
                addAnotherNo()
            } else {
                val snackbar = Snackbar.make(PBBillPayMain, R.string.topup_limit_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        })
        submitButton.setOnClickListener {
            if (cd.isConnectingToInternet()) {
                showCurrentTopupLog()
            } else {
                val snackbar = Snackbar.make(PBBillPayMain, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }


        addAnotherNo()
    }

    private fun addAnotherNo() {
        ++addNoFlag
        val topUpView = layoutInflater.inflate(R.layout.pallibidyut_billpay_view, null)

        if (addNoFlag == 1) {
            topUpView.billViewRemoveImage.visibility=View.GONE
        } else {

        }

        topUpView.billViewRemoveImage.setOnClickListener(View.OnClickListener {
            --addNoFlag
            slideOutAnim
                    .setAnimationListener(object : Animation.AnimationListener {

                        override fun onAnimationStart(animation: Animation) {}

                        override fun onAnimationRepeat(animation: Animation) {}

                        override fun onAnimationEnd(animation: Animation) {
                            topUpView.postDelayed({ billMainLL.removeView(topUpView) }, 100)
                        }
                    })
            topUpView.startAnimation(slideOutAnim)
        })


        billMainLL.addView(topUpView)
        topUpView.startAnimation(slideInAnim)
    }

    private fun showCurrentTopupLog() {
        if (billMainLL.getChildCount() > 0) {
            val reqStrBuilder = StringBuilder()
            for (i in 0 until billMainLL.getChildCount()) {
                val singleTopUpView = billMainLL.getChildAt(i)
                if (singleTopUpView != null) {

                    val billNoET = singleTopUpView!!.pbBillNumberET
                    val amountET = singleTopUpView!!.pbBillAmountET

                    val billNoString = billNoET.getText().toString()
                    val amountString = amountET.getText().toString()

                    if (amountString.length < 1) {
                        amountET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"))
                        return
                    }
                    reqStrBuilder.append((i + 1).toString() + ". " + "Bill Number :" + " " + billNoString
                            + "\n " + getString(R.string.amount_des) + " " + amountString + getString(R.string.tk))

                }
            }
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.conf_topup_title_msg)
            builder.setMessage(reqStrBuilder)
            builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
                dialogInterface.dismiss()
                askForPin()
            }
            val alert = builder.create()
            alert.show()

        } else {
            val snackbar = Snackbar.make(PBBillPayMain, R.string.add_number_amount_msg, Snackbar.LENGTH_LONG)
            snackbar.setActionTextColor(Color.parseColor("#ffffff"))
            val snackBarView = snackbar.view
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
            snackbar.show()
        }
    }

    private fun askForPin() {
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

                if (cd.isConnectingToInternet) {
                    // new TopUpAsync().execute();
//                    handleTopupAPIValidation(pinNoET.text.toString())
                } else {
                    val snackbar = Snackbar.make(PBBillPayMain, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                    snackbar.show()
                }
            } else {
                val snackbar = Snackbar.make(PBBillPayMain, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }
        val alert = builder.create()
        alert.show()
    }


}
