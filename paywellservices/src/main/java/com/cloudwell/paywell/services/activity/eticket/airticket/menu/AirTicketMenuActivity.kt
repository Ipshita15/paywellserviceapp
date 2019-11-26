package com.cloudwell.paywell.services.activity.eticket.airticket.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketMainActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.BookingCancelActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.requestActivity.IssueTicketRequestActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.AirThicketTranslationLogActivity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.cloudwell.paywell.services.utils.LanuageConstant.KEY_BANGLA
import com.cloudwell.paywell.services.utils.LanuageConstant.KEY_ENGLISH
import kotlinx.android.synthetic.main.activity_air_ticket_main_contain.*
import java.util.*


class AirTicketMenuActivity : AirTricketBaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    val KEY_TAG = AirTicketMenuActivity::class.java.getName()
    val BOOKING_TAG = "BOOKING"
    val TRX_TAG = "TRX_LOG"

    private var mAppHandler: AppHandler? = null
    internal var radioButton_five: RadioButton? = null
    internal var radioButton_ten: RadioButton? = null
    internal var radioButton_twenty: RadioButton? = null
    internal var radioButton_fifty: RadioButton? = null
    internal var radioButton_hundred: RadioButton? = null
    internal var radioButton_twoHundred: RadioButton? = null
    var selectedLimit = 5
    lateinit var cd: ConnectionDetector


    companion object {
        val KEY_LIMIT = "LIMIT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAppHandler = AppHandler.getmInstance(applicationContext)
        val isEnglish = mAppHandler?.getAppLanguage().equals("en", ignoreCase = true)
        if (isEnglish) {
            switchToCzLocale(Locale(KEY_ENGLISH, ""))
        } else {
            switchToCzLocale(Locale(KEY_BANGLA, ""))
        }

        setContentView(R.layout.activity_air_tricket_menu)
        setToolbar(getString(R.string.home_eticket_air))
        btViewTricket.setOnClickListener(this)
        btCencel.setOnClickListener(this)
        btTransationLog.setOnClickListener(this)
        btIssueTicket.setOnClickListener(this)


        cd = ConnectionDetector(AppController.getContext())
        mAppHandler = AppHandler.getmInstance(applicationContext)


    }

    override fun onResume() {
        super.onResume()
        selectedLimit = 5
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btViewTricket -> {
                startActivity(Intent(applicationContext, AirTicketMainActivity::class.java))
            }

            R.id.btCencel -> {
                startActivity(Intent(applicationContext, BookingCancelActivity::class.java))
            }

            R.id.btIssueTicket -> {
                startActivity(Intent(applicationContext, IssueTicketRequestActivity::class.java))
            }

            R.id.btTransationLog -> {
                showLimitPrompt(TRX_TAG)

            }

            R.id.btBooking -> {

                showLimitPrompt(BOOKING_TAG)


            }
        }
    }


    private fun showReposeUI(response: BookingList) {
        val builder = AlertDialog.Builder(this@AirTicketMenuActivity)
        builder.setTitle("Result")
        builder.setMessage(response.message)
        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            dialogInterface.dismiss()
//            onBackPressed()
        }
        val alert = builder.create()
        alert.show()

    }

    private fun showLimitPrompt(tag: String) {
        val dialog = AppCompatDialog(this)
        if (tag.equals(BOOKING_TAG)) {
            dialog.setTitle(R.string.book)
        } else if (tag.equals(TRX_TAG)) {
            dialog.setTitle(R.string.booking_log_limit_title_msg)
        }

        dialog.setContentView(R.layout.dialog_trx_limit)

        val btn_okay = dialog.findViewById<Button>(R.id.buttonOk)
        val btn_cancel = dialog.findViewById<Button>(R.id.cancelBtn)

        radioButton_five = dialog.findViewById(R.id.radio_five)
        radioButton_ten = dialog.findViewById(R.id.radio_ten)
        radioButton_twenty = dialog.findViewById(R.id.radio_twenty)
        radioButton_fifty = dialog.findViewById(R.id.radio_fifty)
        radioButton_hundred = dialog.findViewById(R.id.radio_hundred)
        radioButton_twoHundred = dialog.findViewById(R.id.radio_twoHundred)

        radioButton_five?.setOnCheckedChangeListener(this)
        radioButton_ten?.setOnCheckedChangeListener(this)
        radioButton_twenty?.setOnCheckedChangeListener(this)
        radioButton_fifty?.setOnCheckedChangeListener(this)
        radioButton_hundred?.setOnCheckedChangeListener(this)
        radioButton_twoHundred?.setOnCheckedChangeListener(this)

        assert(btn_okay != null)
        btn_okay!!.setOnClickListener {
            dialog.dismiss()
            if (isInternetConnection) {
//                if (tag.equals(BOOKING_TAG)) {
//                    val intent = Intent(application, BookingStatusActivity::class.java)
//                    intent.putExtra(KEY_LIMIT, selectedLimit)
//                    startActivity(intent)
//                } else if (tag.equals(TRX_TAG)) {
                val intent = Intent(application, AirThicketTranslationLogActivity::class.java)
                intent.putExtra(KEY_LIMIT, selectedLimit)
                startActivity(intent)
//                }
            } else {
                showNoInternetConnectionFound()
            }


        }
        assert(btn_cancel != null)
        btn_cancel!!.setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(true)
        dialog.show()
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            if (buttonView.id == R.id.radio_five) {
                selectedLimit = 5
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_ten) {
                selectedLimit = 10
                radioButton_five?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_twenty) {
                selectedLimit = 20
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_fifty) {
                selectedLimit = 50
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_hundred) {
                selectedLimit = 100
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_twoHundred?.setChecked(false)
            }
            if (buttonView.id == R.id.radio_twoHundred) {
                selectedLimit = 200
                radioButton_five?.setChecked(false)
                radioButton_ten?.setChecked(false)
                radioButton_twenty?.setChecked(false)
                radioButton_fifty?.setChecked(false)
                radioButton_hundred?.setChecked(false)
            }
        }
    }

    override fun onBackPressed() {

        val isEnglish = mAppHandler?.getAppLanguage().equals("en", ignoreCase = true)
        if (isEnglish) {
            switchToCzLocale(Locale(KEY_ENGLISH, ""))
        } else {
            switchToCzLocale(Locale(KEY_BANGLA, ""))
        }


        super.onBackPressed()
    }





}
