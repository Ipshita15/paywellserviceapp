package com.cloudwell.paywell.services.retrofit;

import com.cloudwell.paywell.services.activity.utility.AllUrl;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/29/18.
 */
public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = AllUrl.BASE_URL;
    private static final String BASE_URL_PHP7 = AllUrl.BASE_URL_PHP_7;

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }


    public static APIService getAPIServicePHP7() {
        return RetrofitClient.getClientPHP(BASE_URL_PHP7).create(APIService.class);
    }
}
