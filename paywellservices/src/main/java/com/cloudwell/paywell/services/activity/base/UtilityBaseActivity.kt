package com.cloudwell.paywell.services.activity.base

import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity
import com.cloudwell.paywell.services.app.AppHandler

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 30/1/19.
 */
open class UtilityBaseActivity : MVVMBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        switchToCzLocale(Locale.ENGLISH)
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







}
