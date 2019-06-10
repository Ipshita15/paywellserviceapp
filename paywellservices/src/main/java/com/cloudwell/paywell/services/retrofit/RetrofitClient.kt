package com.cloudwell.paywell.services.retrofit


import com.cloudwell.paywell.services.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/29/18.
 */
object RetrofitClient {

    private var retrofit: Retrofit? = null
    private var retrofitPHP7: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit? {
        if (retrofit == null) {

            val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)


            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClient.addInterceptor(logging)

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
            httpClient.connectTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS)

            httpClient.addInterceptor(HeaderTokenInterceptor())

//            httpClient.addNetworkInterceptor(object : Interceptor {
//                @Throws(IOException::class)
//                override fun intercept(chain: Interceptor.Chain): Response {
//                    val request = chain.request()
//                    chain.proceed(request).use { response ->
//                        val jsonObject: JsonObject
//                        try {
//                            val root = JSONObject(Objects.requireNonNull<ResponseBody>(response.body).string().toString())
//                            val status = root.getInt("status")
//                            if (status.toString().startsWith("3")) {
//                                Logger.v("" + status)
//
//                            }
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                        }
//
//                        return response
//                    }
//                }
//            })


            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
                httpClient.addInterceptor(logging)
                httpClient.addNetworkInterceptor(StethoInterceptor())
            }
            httpClient.authenticator(TokenAuthenticator())


            val okHttpClient = httpClient.build()
            retrofitPHP7 = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
        return retrofitPHP7
    }
}
