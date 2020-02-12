package com.cloudwell.paywell.services.utils

import android.content.Context
import android.provider.Settings

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2020-01-29.
 */
object AndroidIDUtility {

    fun getAndroidID(applicationContext: Context): String {
        val androidId: String = Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId
    }
}