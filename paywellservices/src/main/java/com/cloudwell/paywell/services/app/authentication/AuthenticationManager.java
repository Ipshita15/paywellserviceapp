package com.cloudwell.paywell.services.app.authentication;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.app.model.APIResposeGenerateToken;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.retrofit.ApiUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
public class AuthenticationManager {
    private static String TAG = AuthenticationManager.class.getName();

    public static void basicAuthentication(final Context context) {
        String userName = "paywell";
        String password = "PayWell@321";
        String base = userName + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        AppHandler mAppHandler = new AppHandler(context);

        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", "fLdjl3VX_OPOx6zvadOGuCvq2Ay0civ6v-HUQeiLVRg");
        params.put("username", "" + mAppHandler.getRID());
        params.put("retailer_code", "" + mAppHandler.getRID());

        ApiUtils.getAPIService().callGenerateToken(authHeader, params).enqueue(new Callback<APIResposeGenerateToken>() {
            @Override
            public void onResponse(Call<APIResposeGenerateToken> call, Response<APIResposeGenerateToken> response) {
                Log.d(TAG, "onResponse: " + response);
                AppStorageBox.put(context, AppStorageBox.Key.AUTHORIZATION_DATA, response.body());

            }

            @Override
            public void onFailure(Call<APIResposeGenerateToken> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }
}
