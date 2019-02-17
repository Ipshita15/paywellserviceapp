package com.cloudwell.paywell.services.retrofit;

import android.util.Base64;

import com.cloudwell.paywell.services.activity.utility.AllUrl;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.app.model.APIResposeGenerateToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
public class TokenAuthenticator implements Authenticator {
    private static final String TAG = TokenAuthenticator.class.getName();

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (response.code() == 401) {

            String userName = "paywell";
            String password = "PayWell@321";
            String base = userName + ":" + password;
            String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

            AppHandler mAppHandler = AppHandler.getmInstance(AppController.getContext());

            Map<String, String> params = new HashMap<>();
            params.put("skey", "fLdjl3VX_OPOx6zvadOGuCvq2Ay0civ6v-HUQeiLVRg");
            params.put("username", "" + mAppHandler.getRID());
            params.put("retailer_code", "" + mAppHandler.getRID());


            retrofit2.Response<APIResposeGenerateToken> execute = ApiUtils.getAPIService().callGenerateToken(AllUrl.HOST_URL_AUTHENTICATION, authHeader, params).execute();


            return response.request().newBuilder()
                    .header("Bearer", execute.body().getSecurityToken())
                    .build();
        }

        return null;
    }
}
