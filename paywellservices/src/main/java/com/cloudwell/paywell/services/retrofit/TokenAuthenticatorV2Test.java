package com.cloudwell.paywell.services.retrofit;

import com.cloudwell.paywell.services.activity.home.model.ResposeAppsAuth;
import com.cloudwell.paywell.services.activity.home.model.refreshToken.RequestRefreshToken;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2020-02-10.
 */
public class TokenAuthenticatorV2Test implements Authenticator {

    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {


        if (response.code() == 401) {

            synchronized (this) {

                String userName = AppHandler.getmInstance(AppController.getContext()).getUserName();
                String androidId = AppHandler.getmInstance(AppController.getContext()).getAndroidID();

                RequestRefreshToken requestRefreshToken = new RequestRefreshToken();
                requestRefreshToken.setUsername(userName);
                requestRefreshToken.setChannel("android");
                requestRefreshToken.setDeviceId(androidId);
                requestRefreshToken.setFormat("json");
//                requestRefreshToken.setTimestamp("" + DateUtils.INSTANCE.getCurrentTimestamp());


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(requestRefreshToken));

                    String tokenBaseOnRSAlgorithm = RSAUtilty.Companion.getTokenBaseOnRSAlgorithm(jsonObject);


                    if (tokenBaseOnRSAlgorithm.equals(response.request().header("Authorization"))) {
                        return null; // If we already failed with these credentials, don't retry.
                    }


                    retrofit2.Response<ResposeAppsAuth> response1 = ApiUtils.getAPIServiceV2().refreshToken(tokenBaseOnRSAlgorithm, requestRefreshToken).execute();
                    ResposeAppsAuth m = response1.body();
                    if (response1.code() == 200) {

                        String token = m.getToken().getSecurityToken();
                        //AppHandler.getmInstance(AppController.getContext()).setAppsSecurityToken(token);

                        String tokenBaseOnRSAlgorithm1 = "";
                        try {
                            JSONObject jsonObject1 = new JSONObject(AppHandler.getmInstance(AppController.getContext()).getPreviousRequestObject());
                            tokenBaseOnRSAlgorithm1 = RSAUtilty.Companion.getTokenBaseOnRSAlgorithm(jsonObject1);

                        } catch (Exception e) {

                        }


                        return response.request().newBuilder()
                                .header("Authorization", tokenBaseOnRSAlgorithm1)
                                .build();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

            return null;



    }
}
