package com.cloudwell.paywell.services.retrofit

import android.content.Context
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.RSAUtilty.Companion.getTokenBaseOnRSAlgorithm
import com.cloudwell.paywell.services.utils.DateUtils
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class AppsAuthHeaderTokenInterceptor(val mContext: AppController?) : Interceptor {

    var authHeader:String = ""

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
                ){
            val newRequest = chain.request().newBuilder().build();
            return chain.proceed(newRequest)
        }else{
            val subtype: String = requestBody?.contentType()?.subtype() ?:""
            if (subtype.contains("json")) {
                //modify every json request body
                val newProcessApplicationJsonRequestBody = processApplicationJsonRequestBody(requestBody!!)
                requestBody = newProcessApplicationJsonRequestBody
            }

            val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", authHeader)
                    .post(requestBody)
                    .build()
            return chain.proceed(newRequest)
        }


    }


    private fun processApplicationJsonRequestBody(requestBody: RequestBody):  RequestBody? {
        val customReq: String? = bodyToString(requestBody)
        try {
            val mAppHandler = AppHandler.getmInstance(mContext)

            val obj = JSONObject(customReq)
            obj.put("deviceId", mAppHandler.androidID)
            obj.put("timestamp",""+DateUtils.getCurrentTimestamp())
            obj.put("format","json")
            obj.put("channel","android")



            authHeader =  getTokenBaseOnRSAlgorithm(obj)

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
