package com.cloudwell.paywell.services.retrofit;

import com.cloudwell.paywell.services.activity.utility.AllUrl;

import okhttp3.OkHttpClient;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/29/18.
 */
public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = AllUrl.BASE_URL;
    private static final String BASE_URL_PHP7 = AllUrl.BASE_URL_PHP_7;


    public static final String KEY_SKEY = "fLdjl3VX_OPOx6zvadOGuCvq2Ay0civ6v-HUQeiLVRg";


    public static APIService getAPIService() {
        return RetrofitClient.INSTANCE.getClient(BASE_URL).create(APIService.class);
    }


    public static APIService getAPIServicePHP7() {
        return RetrofitClient.INSTANCE.getClientPHP(BASE_URL_PHP7).create(APIService.class);
    }


    public static APIService getAPIServiceV2() {
        return RetrofitClient.INSTANCE.getServiceV2(BASE_URL_PHP7).create(APIService.class);
    }

    public static APIService getAPITest(String url) {
        return RetrofitClient.INSTANCE.getServiceV2(url).create(APIService.class);
    }

    public static OkHttpClient getClient() {
        return RetrofitClient.INSTANCE.getClient();
    }
}
