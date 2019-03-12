package com.cloudwell.paywell.services.utils

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/8/19.
 */
class AssetHelper {
    fun loadJSONFromAsset(context: Context, fileName: String): String {
        var json: String? = null
        try {
            val file = context.getAssets().open(fileName)
            val size = file.available()
            val buffer = ByteArray(size)
            file.read(buffer)
            file.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }
}