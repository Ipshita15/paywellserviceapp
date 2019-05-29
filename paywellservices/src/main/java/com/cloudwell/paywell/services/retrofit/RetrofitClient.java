package com.cloudwell.paywell.services.retrofit;


import android.support.annotation.NonNull;

import com.cloudwell.paywell.services.BuildConfig;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/29/18.
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static Retrofit retrofitPHP7 = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {

            OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS);


            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClient.addInterceptor(logging);

            }

            OkHttpClient build = okHttpClient.build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(build)
                    .build();
        }
        return retrofit;
    }

//    public static Retrofit getClientPHP(String baseUrl) {
//        if (retrofitPHP7 == null) {
//
//            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                    .authenticator(new Authenticator() {
//                        @Override
//                        public Request authenticate(Route route, Response response) throws IOException {
//                            if (response.code() == 401) {
//
//                                String userName = "paywell";
//                                String password = "PayWell@321";
//                                String base = userName + ":" + password;
//                                String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
//
//                                AppHandler mAppHandler = AppHandler.getmInstance(AppController.getContext());
//
//                                Map<String, String> params = new HashMap<>();
//                                params.put("skey", "fLdjl3VX_OPOx6zvadOGuCvq2Ay0civ6v-HUQeiLVRg");
//                                params.put("username", "" + mAppHandler.getRID());
//                                params.put("retailer_code", "" + mAppHandler.getRID());
//
//
//                                retrofit2.Response<APIResposeGenerateToken> execute = ApiUtils.getAPIService().callGenerateToken(AllUrl.HOST_URL_AUTHENTICATION, authHeader, params).execute();
//
//
//                                return response.request().newBuilder()
//                                        .header("Bearer", execute.body().getSecurityToken())
//                                        .build();
//                            }
//
//                            return null;
//                        }
//                    })
//                    .connectTimeout(100, TimeUnit.SECONDS)
//                    .readTimeout(100, TimeUnit.SECONDS)
//                    .writeTimeout(100, TimeUnit.SECONDS).build();
////                    .addInterceptor(new TestNetworkInterceptors());
//
//
////            if (BuildConfig.DEBUG) {
////                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
////                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
////                okHttpClient.addInterceptor(logging);
////
////            }
//
//
//            retrofitPHP7 = new Retrofit.Builder()
//                    .baseUrl(baseUrl)
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(okHttpClient)
//                    .build();
//
//            return retrofitPHP7;
//
//        }
//        return retrofitPHP7;
//    }


    public static Retrofit getClientPHP(String baseUrl) {
        if (retrofitPHP7 == null) {
            final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


            httpClient.addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {

                    String token = (String) AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.SECURITY_TOKEN);

                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            });

            httpClient.connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                httpClient.addInterceptor(logging);
            }


            httpClient.authenticator(new TokenAuthenticator());

//            httpClient.addNetworkInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//
//
//                    Request request = chain.request();
//
//                    long t1 = System.nanoTime();
//                    Logger.v(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
//
//                    Response response = chain.proceed(request);
//
//                    try {
//
//                        String data = response.body().string();
//                        JSONObject jsonObject = new JSONObject(data);
//                        int status = jsonObject.getInt("status");
//                        Logger.v("status : " + status);
//
//                        if (status == 401) {
//
//                            String userName = "paywell";
//                            String password = "PayWell@321";
//                            String base = userName + ":" + password;
//                            String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
//
//                            AppHandler mAppHandler = AppHandler.getmInstance(AppController.getContext());
//
//                            Map<String, String> params = new HashMap<>();
//                            params.put("skey", "fLdjl3VX_OPOx6zvadOGuCvq2Ay0civ6v-HUQeiLVRg");
//                            params.put("username", "" + mAppHandler.getRID());
//                            params.put("retailer_code", "" + mAppHandler.getRID());
//
//
//                            retrofit2.Response<APIResposeGenerateToken> execute = ApiUtils.getAPIService().callGenerateToken(AllUrl.HOST_URL_AUTHENTICATION, authHeader, params).execute();
//
//                            request.newBuilder()
//                                    .header("Bearer", execute.body().getSecurityToken())
//                                    .build();
//
//                            chain.proceed(request);
//
//                        }
//
//
//                    } catch (Exception ignored) {
//
//                    }
//
//
//                    return response;
//                }
//            });

            OkHttpClient okHttpClient = httpClient.build();
            retrofitPHP7 = new Retrofit.Builder().baseUrl(baseUrl).
                    addConverterFactory(GsonConverterFactory.create()).
                    client(okHttpClient).
                    build();
        }
        return retrofitPHP7;
    }
}
