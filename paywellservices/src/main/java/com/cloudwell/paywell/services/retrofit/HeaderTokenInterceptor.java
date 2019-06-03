package com.cloudwell.paywell.services.retrofit;

import android.support.annotation.NonNull;

import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-05-29.
 */
public class HeaderTokenInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String token = (String) AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.SECURITY_TOKEN);
        Request newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();
        return chain.proceed(newRequest);
    }
}

