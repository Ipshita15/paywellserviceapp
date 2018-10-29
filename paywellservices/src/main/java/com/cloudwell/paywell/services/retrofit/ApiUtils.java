package com.cloudwell.paywell.services.retrofit;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/29/18.
 */
public class ApiUtils {
    private ApiUtils() {
    }

    public static final String BASE_URL = "http://php7.paywellonline.com/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
