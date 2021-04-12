package com.cloudwell.paywell.services.retrofit

import android.content.Context
import android.content.Intent
import com.cloudwell.paywell.services.activity.home.HomeActivity
import com.cloudwell.paywell.services.activity.home.model.refreshToken.RequestRefreshToken
import com.cloudwell.paywell.services.activity.utility.AllUrl
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.RSAUtilty.Companion.getTokenBaseOnRSAlgorithm
import com.cloudwell.paywell.services.utils.AppsStatusConstant
import com.cloudwell.paywell.services.utils.DateUtils
import com.cloudwell.paywell.services.utils.DateUtils.getCurrentTimestamp
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class AppsAuthHeaderTokenInterceptor(val mContext: AppController?) : Interceptor {

    var authHeader: String = ""

    fun HeaderTokenInterceptor(context: Context) {

    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var requestBody = chain.request().body()
        val toString = chain.request().url().toString()

        if (toString.equals(AllUrl.gettoken) ||
                toString.equals(AllUrl.ProfilingReg) ||
                toString.equals(AllUrl.resetPassword) ||
                toString.equals(AllUrl.refreshToken)
        ) {
            val newRequest = chain.request().newBuilder().build()
            return chain.proceed(newRequest)
        } else {
            val subtype: String = requestBody?.contentType()?.subtype() ?: ""
            if (subtype.contains("json")) {
                //modify every json request body
                val newProcessApplicationJsonRequestBody = processApplicationJsonRequestBody(requestBody!!)
                requestBody = newProcessApplicationJsonRequestBody
            }

            val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", authHeader)
                    .post(requestBody!!)
                    .build()

            val response = chain.proceed(newRequest)
            val code = response.code()
            if (code == 401) {


                synchronized(this) {
                    val userName = AppHandler.getmInstance(AppController.getContext()).userName
                    val androidId = AppHandler.getmInstance(AppController.getContext()).androidID
                    val requestRefreshToken = RequestRefreshToken()
                    requestRefreshToken.username = userName
                    requestRefreshToken.channel = "android"
                    requestRefreshToken.deviceId = androidId
                    requestRefreshToken.format = "json"
                    requestRefreshToken.timestamp = "" + getCurrentTimestamp()

                    val jsonObject: JSONObject?
                    try {
                        jsonObject = JSONObject(Gson().toJson(requestRefreshToken))
                        val tokenBaseOnRSAlgorithm = getTokenBaseOnRSAlgorithm(jsonObject)
                        val response1 = ApiUtils.getAPIServiceV2().refreshToken(tokenBaseOnRSAlgorithm, requestRefreshToken).execute()
                        val m = response1.body()
                        if (response1.code() == 200) {
                            val token = m!!.token.securityToken
                            AppHandler.getmInstance(AppController.getContext()).appsSecurityToken = token

                            // generate new Authorization token and post original request body
                            val newProcessApplicationJsonRequestBody = processApplicationJsonRequestBody(requestBody)
                            val newRequest = newProcessApplicationJsonRequestBody?.let {
                                chain.request().newBuilder()
                                        .addHeader("Authorization", authHeader)
                                        .post(it)
                                        .build()
                                return chain.proceed(newRequest)

                            }
                        }else{
                            AppHandler.getmInstance(AppController.getContext()).appStatus = AppsStatusConstant.KEY_logout
                            val intent = Intent(AppController.getContext(), HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            AppController.getContext().startActivity(intent)
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()

                        AppHandler.getmInstance(AppController.getContext()).appStatus = AppsStatusConstant.KEY_logout
                        val intent = Intent(AppController.getContext(), HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        AppController.getContext().startActivity(intent)
                    }
                }
            }

            // otherwise just pass the original response on
            return response





    }


}


private fun processApplicationJsonRequestBody(requestBody: RequestBody): RequestBody? {
    val customReq: String? = bodyToString(requestBody)
    try {
        val mAppHandler = AppHandler.getmInstance(mContext)

        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(mContext)!!.rid)


        val obj = JSONObject(customReq)
        obj.put("deviceId", mAppHandler.androidID)
        obj.put("timestamp", "" + DateUtils.getCurrentTimestamp())
        obj.put("format", "json")
        obj.put("channel", "android")
        obj.put("ref_id", uniqueKey)
        obj.put("username", AppHandler.getmInstance(AppController.getContext()).userName)





        authHeader = getTokenBaseOnRSAlgorithm(obj)

        val create = RequestBody.create(requestBody.contentType(), obj.toString());

        return create;
//        return obj.toString().toRequestBody(requestBody.contentType())
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    return null
}


private fun bodyToString(request: RequestBody): String? {
    return try {
        val buffer = Buffer()
        if (request != null) request.writeTo(buffer) else return ""
        buffer.readUtf8()
    } catch (e: IOException) {
        "did not work"
    }
}

}
