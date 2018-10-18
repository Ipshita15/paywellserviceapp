package com.ajkerdeal.app.android.api;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mitu on 6/14/17.
 */

public class TestRetrofitClient {
    private static Retrofit sRetrofit;

    private TestRetrofitClient() {
    }
    //
    public synchronized static Retrofit getInstance(final Context context) {
        if (sRetrofit == null) {
            createRetrofit(context);
        }
        return sRetrofit;
    }

    private static void createRetrofit(final Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .cache(new Cache(context.getCacheDir(), 10 * 1024 * 1024))
                .build();
        sRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://elastic.ajkerdeal.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
