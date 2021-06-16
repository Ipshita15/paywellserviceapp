package com.cloudwell.paywell.services.retrofit


import com.cloudwell.paywell.services.BuildConfig
import com.cloudwell.paywell.services.app.AppController
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
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
                httpClient.addInterceptor(OkHttpProfilerInterceptor())
               // httpClient.addInterceptor(ChuckInterceptor(AppController.getContext()))

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

            val spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                    .supportsTlsExtensions(true)
                    .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                    .cipherSuites(
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                            CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
                    .build()


            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(90, TimeUnit.SECONDS).readTimeout(90, TimeUnit.SECONDS).writeTimeout(90, TimeUnit.SECONDS)

            httpClient.addInterceptor(AppsAuthHeaderTokenInterceptor(AppController.getContext()))

            httpClient.connectionSpecs(Collections.singletonList(spec))
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(logging)
                httpClient.addInterceptor(OkHttpProfilerInterceptor())
                // httpClient.addInterceptor(ChuckInterceptor(AppController.getContext()))

            }

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
