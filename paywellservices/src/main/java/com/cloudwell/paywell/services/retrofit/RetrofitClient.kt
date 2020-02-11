package com.cloudwell.paywell.services.retrofit


import com.cloudwell.paywell.services.BuildConfig
import com.cloudwell.paywell.services.activity.home.model.refreshToken.RequestRefreshToken
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.RSAUtilty.Companion.getTokenBaseOnRSAlgorithm
import com.cloudwell.paywell.services.utils.DateUtils.getCurrentTimestamp
import com.google.gson.Gson
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/29/18.
 */
object RetrofitClient {

    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null
    private var retrofitPHP7: Retrofit? = null
    private var retrofitV2: Retrofit? = null


    fun getClient(baseUrl: String): Retrofit? {
        if (retrofit == null) {

            val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)


            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClient.addInterceptor(logging)
//                okHttpClient.addNetworkInterceptor(StethoInterceptor())
                okHttpClient.addInterceptor(OkHttpProfilerInterceptor())

            }

            val build = okHttpClient.build()


            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(build)
                    .build()
        }
        return retrofit
    }


    fun getClientPHP(baseUrl: String): Retrofit? {
        if (retrofitPHP7 == null) {
            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(90, TimeUnit.SECONDS).readTimeout(90, TimeUnit.SECONDS).writeTimeout(90, TimeUnit.SECONDS)

            httpClient.addInterceptor(HeaderTokenInterceptor(AppController.getContext()))

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(logging)
//                httpClient.addNetworkInterceptor(StethoInterceptor())
                httpClient.addInterceptor(OkHttpProfilerInterceptor())
            }
            httpClient.authenticator(TokenAuthenticator())


            okHttpClient = httpClient.build()
            retrofitPHP7 = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
        }
        return retrofitPHP7
    }


    fun getServiceV2(baseUrl: String): Retrofit? {
        if (retrofitV2 == null) {
            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(90, TimeUnit.SECONDS).readTimeout(90, TimeUnit.SECONDS).writeTimeout(90, TimeUnit.SECONDS)

            httpClient.addInterceptor(AppsAuthHeaderTokenInterceptor(AppController.getContext()))

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(logging)
                httpClient.addInterceptor(OkHttpProfilerInterceptor())
            }
//            httpClient.authenticator(TokenAuthenticatorV2Test())

            httpClient.interceptors().add(Interceptor { chain ->
                val request: Request = chain.request()
                var response: Response? = null
                var responseOK = false
                        response = chain.proceed(request)
                        val code = response.code()

                        if (code == 401){


                            synchronized(this) {
                                val userName = AppHandler.getmInstance(AppController.getContext()).userName
                                val androidId = AppHandler.getmInstance(AppController.getContext()).androidID
                                val requestRefreshToken = RequestRefreshToken()
                                requestRefreshToken.username = userName
                                requestRefreshToken.channel = "android"
                                requestRefreshToken.deviceId = androidId
                                requestRefreshToken.format = "json"
                                requestRefreshToken.timestamp = "" + getCurrentTimestamp()
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

                                        response =   chain.proceed(response!!.request().newBuilder()
                                              .header("Authorization", tokenBaseOnRSAlgorithm1)
                                              .build())
                                    }

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }


                        }

                // otherwise just pass the original response on
                response!!
            });



            okHttpClient = httpClient.build()
            retrofitV2 = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
        }
        return retrofitV2
    }



    fun getClient(): OkHttpClient? {
        return okHttpClient



    }
}
