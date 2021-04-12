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
open class HealthInsuranceBaseActivity : MVVMBaseActivity() {
    internal open lateinit var mAppHandler: AppHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAppHandler = AppHandler.getmInstance(applicationContext)

        changeAppTheme()

        if (supportActionBar != null) {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.toolbar_background_color_health)))
            setActionbarTextColor(supportActionBar!!, resources.getColor(R.color.toolbar_font_color_health))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = resources.getColor(R.color.statubar_color_health, this.theme)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = resources.getColor(R.color.statubar_color_health)
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
            setTheme(R.style.EnglishAppThemeHealthInsurance)
        } else {
            setTheme(R.style.BanglaAppThemeHealthInsurance)
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


    fun showBusTicketErrorDialog(message: String, needFinishedActivity: Boolean = false) {

        val errorMsgDialog = BusErrorMsgDialog(message, needFinishedActivity)
        errorMsgDialog.show(supportFragmentManager, "oTPVerificationMsgDialog")

    }

    fun showBusTicketSuccessDialog(message: String, needFinishedActivity: Boolean = false) {

        val errorMsgDialog = BusErrorMsgDialog(message, needFinishedActivity)
        errorMsgDialog.show(supportFragmentManager, "oTPVerificationMsgDialog")

    }


}
