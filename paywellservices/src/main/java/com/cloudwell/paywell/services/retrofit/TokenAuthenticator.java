package com.cloudwell.paywell.services.retrofit;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.cloudwell.paywell.services.activity.utility.AllUrl;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.app.model.APIResposeGenerateToken;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
public class TokenAuthenticator implements Authenticator {
    private static final String TAG = TokenAuthenticator.class.getName();


    @Override
    public Request authenticate(Route route, @NonNull Response response) throws IOException {

        Log.e("authenticate", "authenticate");
        if (response.code() == 401) {


            synchronized (this) {
                String userName = "paywell";
                String password = "PayWell@321";
                String base = userName + ":" + password;
                String authorization = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

                AppHandler mAppHandler = AppHandler.getmInstance(AppController.getContext());

                Map<String, String> params = new HashMap<>();
                params.put("sKey", "fLdjl3VX_OPOx6zvadOGuCvq2Ay0civ6v-HUQeiLVRg");
//            params.put("username", "" + mAppHandler.getRID());
//            params.put("retailer_code", "" + mAppHandler.getRID());
//
                params.put("username", "cwntcl");
                params.put("retailer_code", "cwntcl");


                Call<APIResposeGenerateToken> apiResposeGenerateTokenCall = ApiUtils.getAPIService().callGenerateToken(AllUrl.HOST_URL_AUTHENTICATION, authorization, params);
                retrofit2.Response<APIResposeGenerateToken> response1 = apiResposeGenerateTokenCall.execute();
                APIResposeGenerateToken apiResposeGenerateToken = response1.body();

                if (apiResposeGenerateToken.getStatus() == 200) {
                    String securityToken = apiResposeGenerateToken.getToken().getSecurityToken();

                    AppStorageBox.put(AppController.getContext(), AppStorageBox.Key.SECURITY_TOKEN, securityToken);

                    return response.request().newBuilder()
                            .header("Bearer", securityToken)
                            .build();
                }


            }
            return null;
        }

        return null;

    }


}


