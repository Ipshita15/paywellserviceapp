package com.ajkerdeal.app.android.api;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sumaya on 12-Oct-17.
 */

public class PaywellSingleton {
    private static Context sContext;
    private static Retrofit sRetrofit;


    private PaywellSingleton() {

    }

    public synchronized static Retrofit getInstance(Context context) {
        sContext = context;

        if (sRetrofit == null) {
            createRetrofit();
        }
        return sRetrofit;
    }


    private static void createRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .cache(new Cache(sContext.getCacheDir(), 10 * 1024 * 1024))
                .addInterceptor(new BasicAuthInterceptor("Paywell", "_R4[5cHWSc"))
                .build();
        sRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://bridge.ajkerdeal.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static class BasicAuthInterceptor implements Interceptor {

        private String credentials;

        public BasicAuthInterceptor(String user, String password) {
            this.credentials = Credentials.basic(user, password);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build();
            return chain.proceed(authenticatedRequest);
        }

    }
}
