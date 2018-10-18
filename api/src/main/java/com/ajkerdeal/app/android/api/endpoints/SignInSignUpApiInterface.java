package com.ajkerdeal.app.android.api.endpoints;

import com.ajkerdeal.app.android.api.models.SignUpModel;
import com.ajkerdeal.app.android.api.models.SigninModel;
import com.ajkerdeal.app.android.api.models.ThirdPartyLoginModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by has9 on 5/22/16.
 */
public interface SignInSignUpApiInterface {

    @POST("/CustomerAccess/Login/")
    Call<SigninModel> getSignin(@Body SigninModel signinModel);

    @POST("/CustomerAccess/SignUp/")
    Call<SignUpModel>getSignUp(@Body SignUpModel signUpModel);

    @POST("/CustomerAccess/SignUpLoginByFbGoogle/")
    Call<SigninModel>getThirdPartyLogin(@Body ThirdPartyLoginModel signinModel);
}
