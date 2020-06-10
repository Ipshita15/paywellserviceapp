package com.cloudwell.paywell.services.activity.base

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.ActionBar
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.BusErrorMsgDialog
import com.cloudwell.paywell.services.app.AppHandler
import java.util.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 30/1/19.
 */
open class BusTricketBaseActivity : MVVMBaseActivity() {
    internal open lateinit var mAppHandler: AppHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switchToCzLocale(Locale.ENGLISH)
        mAppHandler = AppHandler.getmInstance(applicationContext)

        changeAppTheme()

        assert(supportActionBar != null)

        if (supportActionBar != null) {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.bus_ticket_toolbar_backgroud_color)))
            setActionbarTextColor(supportActionBar!!, resources.getColor(R.color.bus_ticket_toolbar_title_text_color))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = resources.getColor(R.color.bus_ticket_status_color, this.theme)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = resources.getColor(R.color.bus_ticket_status_color)
            }
        }
    }

    fun setActionbarTextColor(actBar: ActionBar, color: Int) {
        val title = actBar.title.toString()
        val spannablerTitle = SpannableString(title)
        spannablerTitle.setSpan(ForegroundColorSpan(color), 0, spannablerTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        actBar.title = spannablerTitle

    }


    private fun changeAppTheme() {
        val isEnglish = mAppHandler.appLanguage.equals("en", ignoreCase = true)
        if (isEnglish) {
            setTheme(R.style.EnglishAppThemeBus)
        } else {
            setTheme(R.style.EnglishAppThemeBus)
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


    fun showBusTicketErrorDialog(message: String) {

        val errorMsgDialog = BusErrorMsgDialog(message)
        errorMsgDialog.show(supportFragmentManager, "oTPVerificationMsgDialog")

    }

    fun showBusTicketSuccessDialog(message: String) {

        val errorMsgDialog = BusErrorMsgDialog(message)
        errorMsgDialog.show(supportFragmentManager, "oTPVerificationMsgDialog")

    }

}
