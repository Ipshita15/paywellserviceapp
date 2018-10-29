package com.cloudwell.paywell.services.retrofit;


import com.cloudwell.paywell.services.app.model.APIResposeGenerateToken;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/29/18.
 */
public interface APIService {


    @POST("paywellapi/PaywellAuthentication/GenerateToken")
    @FormUrlEncoded
    Call<APIResposeGenerateToken> callGenerateToken(@Header("Authorization") String AuthorizationKey, @FieldMap Map<String, String> params);

}


