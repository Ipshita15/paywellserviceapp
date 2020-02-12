package com.cloudwell.paywell.services.retrofit

import android.content.Context
import com.cloudwell.paywell.services.activity.home.model.refreshToken.RequestRefreshToken
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.RSAUtilty.Companion.getTokenBaseOnRSAlgorithm
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

        if (toString.equals("https://agentapi.paywellonline.com/Authantication/PaywellAuth/getToken?") ||
                toString.equals("https://agentapi.paywellonline.com/Retailer/RetailerService/userServiceProfilingReg") ||
                toString.equals("https://agentapi.paywellonline.com/Authantication/PaywellAuth/resetPassword") ||
                toString.equals("https://agentapi.paywellonline.com/Authantication/PaywellAuth/refreshToken")
        ) {
            val newRequest = chain.request().newBuilder().build();
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
                    .post(requestBody)
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
//                    requestRefreshToken.timestamp = "" + getCurrentTimestamp()
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(Gson().toJson(requestRefreshToken))
                        val tokenBaseOnRSAlgorithm = getTokenBaseOnRSAlgorithm(jsonObject)
                        val response1 = ApiUtils.getAPIServiceV2().refreshToken(tokenBaseOnRSAlgorithm, requestRefreshToken).execute()
                        val m = response1.body()
                        if (response1.code() == 200) {
                            val token = m!!.token.securityToken
                            AppHandler.getmInstance(AppController.getContext()).setAppsSecurityToken(token);
                            var tokenBaseOnRSAlgorithm1 = ""
                            try {
                                val jsonObject1 = JSONObject(AppHandler.getmInstance(AppController.getContext()).previousRequestObject)
                                tokenBaseOnRSAlgorithm1 = getTokenBaseOnRSAlgorithm(jsonObject1)
                            } catch (e: Exception) {
                            }


                            val newProcessApplicationJsonRequestBody = processApplicationJsonRequestBody(requestBody!!)
                            val newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", authHeader)
                                    .post(newProcessApplicationJsonRequestBody)
                                    .build()
                            return chain.proceed(newRequest)
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }


            }

            // otherwise just pass the original response on
            return  response!!





    }


}


private fun processApplicationJsonRequestBody(requestBody: RequestBody): RequestBody? {
    val customReq: String? = bodyToString(requestBody)
    try {
        val mAppHandler = AppHandler.getmInstance(mContext)

        val obj = JSONObject(customReq)
        obj.put("deviceId", mAppHandler.androidID)
//            obj.put("timestamp",""+DateUtils.getCurrentTimestamp())
        obj.put("format", "json")
        obj.put("channel", "android")



        authHeader = getTokenBaseOnRSAlgorithm(obj)

        return RequestBody.create(requestBody.contentType(), obj.toString())
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
