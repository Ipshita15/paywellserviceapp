package com.cloudwell.paywell.services.utils

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-09.
 */
object StringUtility {


    public fun removeLastChar(str: String): String {
        return str.substring(0, str.length - 1)
    }
}