package com.cloudwell.paywell.services.retrofit

import android.util.Base64
import android.util.Log
import com.cloudwell.paywell.services.activity.utility.AllUrl
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus
import com.cloudwell.paywell.services.eventBus.model.MessageToBottom
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
class TokenAuthenticator : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {


        Log.e("authenticate", "authenticate")
        if (response.code() == 401) {
            val userName = "paywell"
            val password = "PayWell@321"
            val base = "$userName:$password"
            val authorization = "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)

            val mAppHandler = AppHandler.getmInstance(AppController.getContext())


            val params = HashMap<String, String>()
            params["sKey"] = ApiUtils.KEY_SKEY
            params["username"] = "" + mAppHandler.userName
            params["retailer_code"] = "" + mAppHandler.rid




            val apiResposeGenerateTokenCall = ApiUtils.getAPIService().callGenerateToken(AllUrl.HOST_URL_AUTHENTICATION, authorization, params)
            val response1 = apiResposeGenerateTokenCall.execute()
            val apiResposeGenerateToken = response1.body()

            if (apiResposeGenerateToken!!.status == 200) {

                val securityToken = apiResposeGenerateToken.token!!.securityToken
                AppHandler.getmInstance(AppController.getContext()).token = securityToken





                return response.request().newBuilder()
                        .header("Authorization", "Bearer $securityToken")
                        .build()


            } else if (apiResposeGenerateToken.status.toString().startsWith("3")) {
                Log.e("", "")
                GlobalApplicationBus.getBus().post(MessageToBottom(apiResposeGenerateToken.message))

            }

        }
        return null
    }




}


